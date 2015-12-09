package taxiapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.taxiapp.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import taxiapp.constants.URLConstants;
import taxiapp.parsers.XMLPullParserForCities;
import taxiapp.structures.GeoNameCity;

/**
 * Created by hassanjamil on 2015-12-05.
 */
public class AdapterAutoCompleteGeoNamePlaces extends ArrayAdapter<GeoNameCity> implements Filterable {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GeoNameCity> resultList;
    private XMLPullParserForCities xmlPullParserForCities = new XMLPullParserForCities();

    private static final String LOG_TAG = AdapterAutoCompleteGeoNamePlaces.class.getSimpleName();

    public AdapterAutoCompleteGeoNamePlaces(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.mContext = context;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public GeoNameCity getItem(int index) {
        return resultList.get(index);
    }

    private ViewHolder holder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.tvText = (TextView) view.findViewById(R.id.tv_item_text);

            view.setTag(holder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        //Setting values
        GeoNameCity dataObj = getItem(position);

        holder.tvText.setText(dataObj.getToponymName() + ", " + dataObj.getCountryCode() +
                ", " + dataObj.getCountryName());

        return view;
    }

    private static class ViewHolder {
        private TextView tvText;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString(), 5, "hassaanjamil");
                    //resultList = performFetchCitiesTask(constraint.toString(), 5, "hassaanjamil");

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private ArrayList<GeoNameCity> autocomplete(String text, int numOfResults, String username) {
        ArrayList<GeoNameCity> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder result = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(URLConstants.SERVICE_URL_GET_CITIES);
            sb.append("?name_startsWith="+ URLEncoder.encode(text, "utf8"));
            sb.append("&maxRows="+numOfResults);
            sb.append("&username="+username);

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                result.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            Log.i(LOG_TAG, "performFetchCitiesTask() Response: " + result);
            resultList = xmlPullParserForCities
                    .parse(new ByteArrayInputStream(result.toString().getBytes()));
        } catch (Exception e) {
            Log.e(LOG_TAG, "ERROR:", e);
        }

        return resultList;
    }
}

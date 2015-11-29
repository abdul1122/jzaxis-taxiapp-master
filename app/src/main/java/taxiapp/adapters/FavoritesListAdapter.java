package taxiapp.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.taxiapp.R;

import java.util.List;

import taxiapp.structures.Favorites;

/**
 * Created by Nisar on 11/29/2015.
 */
public class FavoritesListAdapter extends BaseAdapter{

    private Context context;
    private int layoutResId;
    private List<Favorites> listFav;
    private ViewHolder holder;

    public FavoritesListAdapter(Context context, List<Favorites> listFav)
    {
        this.context = context;
        this.listFav = listFav;
        this.layoutResId = R.layout.item_favorite;
    }

    @Override
    public int getCount()
    {
        return listFav.size();
    }

    @Override
    public Favorites getItem(int position)
    {
        return listFav.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResId, parent, false);

            holder = new ViewHolder();
            holder.tvFavPlaceName = (TextView) row.findViewById(R.id.tv_item_fav_place_name);
            holder.tvFavPlaceAddress = (TextView) row.findViewById(R.id.tv_item_favorite_place_address);
            holder.ivFavIcon=(ImageView)row.findViewById(R.id.iv_item_fav_icon);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }

        //Setting values
        Favorites dataObj = getItem(position);

        holder.tvFavPlaceName.setText(dataObj.placeName);
        holder.tvFavPlaceAddress.setText(dataObj.placeAddress);
        //holder.ivFavIcon.setImageResource(R.drawable.logo);
        return row;
    }

    private static class ViewHolder
    {

        private TextView tvFavPlaceName,tvFavPlaceAddress;
        private ImageView ivFavIcon;
    }
}

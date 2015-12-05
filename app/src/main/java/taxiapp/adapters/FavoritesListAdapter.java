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

import taxiapp.structures.FavoriteItem;
import taxiapp.structures.Favorites;

/**
 * Created by Nisar on 11/29/2015.
 */
public class FavoritesListAdapter extends BaseAdapter{

    private Context mContext;
    private int layoutResId;
    private Favorites mFavorites;
    private ViewHolder holder;

    public FavoritesListAdapter(Context context, Favorites favorites) {
        this.mContext = context;
        this.mFavorites = favorites;
        this.layoutResId = R.layout.item_favorite;
    }

    @Override
    public int getCount() {
        return mFavorites.listFavItems.size();
    }

    @Override
    public FavoriteItem getItem(int position)
    {
        return mFavorites.listFavItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layoutResId, parent, false);

            holder = new ViewHolder();
            holder.tvFavPlaceName = (TextView) view.findViewById(R.id.tv_item_fav_place_name);
            holder.tvFavPlaceAddress = (TextView) view.findViewById(R.id.tv_item_favorite_place_address);
            holder.ivFavIcon=(ImageView)view.findViewById(R.id.iv_item_fav_icon);

            view.setTag(holder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        //Setting values
        FavoriteItem dataObj = getItem(position);

        holder.tvFavPlaceName.setText(dataObj.placeName);

        if(dataObj.placeIdentifier == 100) {
            holder.tvFavPlaceAddress.setVisibility(View.GONE);
            holder.ivFavIcon.setImageResource(android.R.drawable.ic_menu_add);
        } else {
            holder.tvFavPlaceAddress.setText(dataObj.placeAddress);
            holder.tvFavPlaceAddress.setVisibility(View.VISIBLE);
            holder.ivFavIcon.setImageResource(android.R.drawable.star_big_off);
        }

        return view;
    }

    private static class ViewHolder {
        private TextView tvFavPlaceName,tvFavPlaceAddress;
        private ImageView ivFavIcon;
    }
}

package com.example.taxiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import taxiapp.adapters.FavoritesListAdapter;
import taxiapp.constants.AppConstants;
import taxiapp.structures.FavoriteItem;
import taxiapp.structures.Favorites;
import taxiapp.structures.SimpleModel;
import taxiapp.utils.CommonUtilities;

/**
 * Created by hassanjamil on 2015-11-28.
 */
public class ActivityFavorites extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private Button btnGoToMap;
    private ListView lvFavorite;
    public static final String TAG = ActivityFavorites.class.getSimpleName();

    private FavoritesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        init();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareListView();
    }

    private void init() {
        btnGoToMap = (Button) findViewById(R.id.btn_fav_go_to_map);
        lvFavorite = (ListView) findViewById(R.id.lv_favorite);
    }

    private void initListeners() {
        btnGoToMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_fav_go_to_map:
                CommonUtilities.toastShort(ActivityFavorites.this, "Sorry under development");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        FavoriteItem favoriteItem = (FavoriteItem) adapterView.getItemAtPosition(position);
        if(favoriteItem.placeIdentifier == 100) {
            Intent intent = new Intent(ActivityFavorites.this, ActivityAddFavorite.class);
            intent.putExtra("dataObj", favoriteItem);
            startActivity(intent);
        } else {
            CommonUtilities.toastShort(ActivityFavorites.this, "Pickup screen is under development");
        }
    }

    private void prepareListView() {
        Favorites favorites = MyApplication.getInstance().getAppPreferences().getFavorites();
        // "Add more" item on run time
        favorites.listFavItems.add(new FavoriteItem().setItemId(-1).setPlaceIdentifier(100)
                .setPlaceName(AppConstants.STR_FAV_ITEM_ADD_MORE));

        if(favorites != null) {
            adapter = new FavoritesListAdapter(this, favorites);
            lvFavorite.setAdapter(adapter);
            lvFavorite.setOnItemClickListener(this);
        }
    }
}

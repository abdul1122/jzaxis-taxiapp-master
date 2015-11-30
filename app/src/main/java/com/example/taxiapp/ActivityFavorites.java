package com.example.taxiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import taxiapp.adapters.FavoritesListAdapter;
import taxiapp.structures.FavoriteItem;
import taxiapp.structures.Favorites;
import taxiapp.utils.CommonUtilities;

/**
 * Created by hassanjamil on 2015-11-28.
 */
public class ActivityFavorites extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private Button btnAddFavorite;
    private ListView lvFavorite;
    public static final String TAG = ActivityFavorites.class.getSimpleName();

    private FavoritesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        init();
        initListeners();
        prepareListView();
    }

    private void init() {
        btnAddFavorite = (Button) findViewById(R.id.btn_fav_add_favorite);
        lvFavorite = (ListView) findViewById(R.id.lv_favorite);
    }

    private void initListeners() {
        btnAddFavorite.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_fav_add_favorite:
                startActivity(new Intent(ActivityFavorites.this, ActivityAddFavorite.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FavoriteItem favoriteItem = (FavoriteItem) view.getTag();
        if(favoriteItem.placeIdentifier == 100) {
            Intent intent = new Intent(ActivityFavorites.this, ActivityAddFavorite.class);
            intent.putExtra("placeName", favoriteItem.placeName);
            intent.putExtra("placeAddress", favoriteItem.placeAddress);
            intent.putExtra("placeIdentifier", favoriteItem.placeIdentifier);
            startActivity(intent);
        } else {
            CommonUtilities.toastShort(ActivityFavorites.this, "Pickup screen is under development");
        }
    }

    private void prepareListView() {
        // TODO need to replace sample data with real time user inputs
        adapter = new FavoritesListAdapter(this, setDummyList());
        lvFavorite.setAdapter(adapter);
        lvFavorite.setOnItemClickListener(this);
    }

    private Favorites setDummyList() {
        Favorites favorites = new Favorites();

        FavoriteItem obj1 = new FavoriteItem();
        obj1.placeIdentifier = 100;
        obj1.placeName = "Home";
        obj1.placeAddress = "";
        favorites.listFavItems.add(obj1);
        FavoriteItem obj2 = new FavoriteItem();
        obj2.placeIdentifier = 200;
        obj2.placeName = "Office";
        obj2.placeAddress = "h/12 Islamabad";
        favorites.listFavItems.add(obj2);
        FavoriteItem obj3 = new FavoriteItem();
        obj3.placeIdentifier = 200;
        obj3.placeName = "School";
        obj3.placeAddress = "Street #35 i/8 islamabad";
        favorites.listFavItems.add(obj3);
        FavoriteItem last = new FavoriteItem();
        last.placeIdentifier = 100;
        last.placeName = "Add More";
        last.placeAddress = "";
        favorites.listFavItems.add(last);
        return favorites;
    }
}

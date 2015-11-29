package com.example.taxiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import taxiapp.adapters.FavoritesListAdapter;
import taxiapp.structures.Favorites;

/**
 * Created by hassanjamil on 2015-11-28.
 */
public class ActivityFavorites extends Activity implements View.OnClickListener {

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
         List<Favorites> listFav=setDummyList();
        adapter = new FavoritesListAdapter(this, listFav);
        lvFavorite.setAdapter(adapter);
    }
private List<Favorites> setDummyList(){
    List<Favorites> list=new ArrayList<>();
    Favorites obj1=new Favorites();
    obj1.placeIdentifier=1;
    obj1.placeName="Home";
    obj1.placeAddress="i/10 Islamabad";
    list.add(obj1);
    Favorites obj2=new Favorites();
    obj2.placeIdentifier=1;
    obj2.placeName="Office";
    obj2.placeAddress="h/12 Islamabad";
    list.add(obj2);
    Favorites obj3=new Favorites();
    obj3.placeIdentifier=2;
    obj3.placeName="School";
    obj3.placeAddress="Street #35 i/8 islamabad";
    list.add(obj3);
    return list;

}
    private void init() {
        btnAddFavorite = (Button) findViewById(R.id.btn_fav_add_favorite);
        lvFavorite=(ListView)findViewById(R.id.lv_favorite);
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
}

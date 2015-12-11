package com.example.taxiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleMap map;
    private ImageButton ibMoreFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initListeners();
    }

    private void init() {
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_pickup_location)).getMap();
        ibMoreFavorite = (ImageButton) findViewById(R.id.ib_home_fav);
    }

    private void initListeners() {
        ibMoreFavorite.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ib_home_fav:
                startActivity(new Intent(MainActivity.this, ActivityFavorites.class));
                break;
        }
    }
}

package com.example.taxiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by hassanjamil on 2015-11-28.
 */
public class ActivityAddFavorite extends Activity {

    EditText etPlaceName,etLat,etLon;
    Button btnSave,btnShowOnMap;
    int placeIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);

        init();
        Intent intent = getIntent();
        if(intent!=null){
            String placeName = intent.getStringExtra("placeName");
            String placeAddress = intent.getStringExtra("placeAddress");
            placeIdentifier= intent.getIntExtra("placeIdentifier",-1);
            etPlaceName.setText(placeName);
        }

    }

    private void init(){
        etPlaceName=(EditText)findViewById(R.id.et_add_fav_placeName);
        etLat=(EditText)findViewById(R.id.et_add_fav_lat);
        etLon=(EditText)findViewById(R.id.et_add_fav_lon);
        btnSave=(Button)findViewById(R.id.btn_add_fav_save);
        btnShowOnMap=(Button)findViewById(R.id.btn_add_fav_showOnMap);
    }
}

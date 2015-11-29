package com.example.taxiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by hassanjamil on 2015-11-28.
 */
public class ActivityFavorites extends Activity implements View.OnClickListener{

    private Button btnAddFavorite;
    public static final String TAG = ActivityFavorites.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        init();
        initListeners();
    }

    private void init() {
        btnAddFavorite = (Button) findViewById(R.id.btn_fav_add_favorite);
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

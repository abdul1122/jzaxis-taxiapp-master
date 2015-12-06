package com.example.taxiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import taxiapp.constants.AppConstants;
import taxiapp.handlers.GeocodeHandler;
import taxiapp.structures.FavoriteItem;
import taxiapp.utils.AppPreferences;
import taxiapp.utils.CommonUtilities;
import taxiapp.utils.EditTextUtils;
import taxiapp.utils.GeocoderUtils;

/**
 * Created by hassanjamil on 2015-11-28.
 */
public class ActivityAddFavorite extends Activity implements View.OnClickListener{

    private EditText etPlaceName, etLat, etLon;
    private Button btnSave, btnShowOnMap;
    private TextView tvAddress;

    private FavoriteItem mFavoriteItem;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);

        init();
        getIntentExtras();
        initListeners();
    }

    private void init(){
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_add_favorite)).getMap();

        etPlaceName = (EditText) findViewById(R.id.et_add_fav_placeName);
        etLat = (EditText) findViewById(R.id.et_add_fav_lat);
        etLon = (EditText) findViewById(R.id.et_add_fav_lon);
        tvAddress = (TextView) findViewById(R.id.tv_add_fav_address);
        btnSave = (Button) findViewById(R.id.btn_add_fav_save);
        btnShowOnMap = (Button) findViewById(R.id.btn_add_fav_showOnMap);
    }

    private void initListeners() {
        btnSave.setOnClickListener(this);
        btnShowOnMap.setOnClickListener(this);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (map != null)
                    map.clear();

                etLat.setText("" + String.format("%.4f", latLng.latitude));
                etLon.setText("" + String.format("%.4f", latLng.longitude));

                addMarkerToLocation(latLng);
                getAddressForLocation(latLng);
            }
        });
    }

    private void addMarkerToLocation(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Location");
        map.addMarker(markerOptions);
    }

    private void zoomToLocation(LatLng latLng, int zoomLevel) {
        CameraUpdate cameraUpdatePosition= CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate cameraUpdateZoom = CameraUpdateFactory.zoomTo(zoomLevel);

        map.moveCamera(cameraUpdatePosition);
        map.animateCamera(cameraUpdateZoom);
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        if(intent != null) {
            mFavoriteItem = (FavoriteItem) intent.getParcelableExtra("dataObj");
            etPlaceName.setText(mFavoriteItem.placeName);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_add_fav_save:
                boolean isValid = createFavoriteItemModel();
                if(isValid) {
                    saveButtonTask();
                }
                break;
            case R.id.btn_add_fav_showOnMap:
                try {
                    if (!EditTextUtils.isFieldEmpty(etLat) && !EditTextUtils.isFieldEmpty(etLon)) {
                        etLat.setError(null);
                        etLon.setError(null);

                        if(map != null)
                            map.clear();

                        LatLng latLng = new LatLng(Double.valueOf(etLat.getText().toString()),
                                Double.valueOf(etLon.getText().toString()));
                        addMarkerToLocation(latLng);
                        getAddressForLocation(latLng);
                        zoomToLocation(latLng, 13);
                    }
                } catch (NumberFormatException e) {
                    CommonUtilities.toastShort(ActivityAddFavorite.this, "Invalid location parameters");
                }

                break;
        }
    }

    private FavoriteItem favoriteItem;
    private boolean createFavoriteItemModel() {
        favoriteItem = new FavoriteItem();
        favoriteItem.itemId = mFavoriteItem.itemId;
        favoriteItem.placeIdentifier = 200;
        boolean isValid = false;

        if(EditTextUtils.isFieldEmpty(etPlaceName)) {
            return isValid;
        } else {
            etPlaceName.setError(null);
            favoriteItem.placeName = etPlaceName.getText().toString();
        }

        if(EditTextUtils.isFieldEmpty(etLat) && EditTextUtils.isFieldEmpty(etLon)) {
            return isValid;
        } else {
            etLat.setError(null);
            etLon.setError(null);
            favoriteItem.placeLocation = new LatLng(Double.valueOf(etLat.getText().toString()),
                    Double.valueOf(etLon.getText().toString()));
        }

        if(TextUtils.isEmpty(tvAddress.getText().toString())) {
            CommonUtilities.toastShort(ActivityAddFavorite.this, "Address for location is not valid");
            return isValid;
        } else {
            isValid = true;
            favoriteItem.placeAddress = tvAddress.getText().toString();
        }

        return isValid;
    }

    private void saveButtonTask() {
        String favItemPlaceName = favoriteItem.placeName.trim();
        AppPreferences appPreferences = MyApplication.getInstance().getAppPreferences();

        if(!(favItemPlaceName.equalsIgnoreCase(AppConstants.STR_FAV_ITEM_HOME))
                && !(favItemPlaceName.equalsIgnoreCase(AppConstants.STR_FAV_ITEM_OFFICE))
                && (mFavoriteItem.itemId == -1)) {
            // Add more item's scenario
            if (!appPreferences.isFavoriteItemExistsForName(appPreferences.getFavorites(), favoriteItem.placeName)) {
                appPreferences.addFavoriteItem(favoriteItem.setItemId(System.currentTimeMillis()));
                CommonUtilities.toastShort(this, "Item added successfully");
                finish();
            } else
                CommonUtilities.toastLong(this, "You already have an item with the above mentioned name");
        }
        else
            if(((favItemPlaceName.equalsIgnoreCase(AppConstants.STR_FAV_ITEM_HOME))
                    || (favItemPlaceName.equalsIgnoreCase(AppConstants.STR_FAV_ITEM_OFFICE)))
                    && (mFavoriteItem.itemId != -1)) {
                // Add home or office item's scenario
                if (!appPreferences.isFavoriteItemExistsForName(appPreferences.getFavorites(), favoriteItem.placeName)) {
                    appPreferences.addFavoriteItem(favoriteItem);
                    CommonUtilities.toastShort(this, "Item added successfully");
                    finish();
                } else
                    CommonUtilities.toastLong(this, "You already have an item with the above mentioned name");
            }
        else
            CommonUtilities.toastLong(this, "You cannot use this name");
    }

    private void getAddressForLocation(LatLng latLng) {
        if (latLng != null) {
            GeocoderUtils geocoderUtils = new GeocoderUtils();
            geocoderUtils.getAddressFromLocation(latLng.latitude, latLng.longitude,
                    getApplicationContext(), new GeocodeHandler(tvAddress));
        }
    }
}

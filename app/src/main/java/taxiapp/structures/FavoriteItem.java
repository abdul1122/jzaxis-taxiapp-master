package taxiapp.structures;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Nisar on 11/29/2015.
 */
public class FavoriteItem {
    /** 100 for Add favorite <br/>
     * 200 for Pickup screen */
    public int placeIdentifier;
    public String placeName;
    public String placeAddress;
    public LatLng placeLocation;

    public FavoriteItem() {
        this.placeIdentifier = 0;
        this.placeName = "";
        this.placeAddress = "";
        this.placeLocation = new LatLng(-1, -1);
    }

    public int getPlaceIdentifier() {
        return placeIdentifier;
    }

    public FavoriteItem setPlaceIdentifier(int placeIdentifier) {
        this.placeIdentifier = placeIdentifier;
        return this;
    }

    public String getPlaceName() {
        return placeName;
    }

    public FavoriteItem setPlaceName(String placeName) {
        this.placeName = placeName;
        return this;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public FavoriteItem setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
        return this;
    }

    public LatLng getPlaceLocation() {
        return placeLocation;
    }

    public FavoriteItem setPlaceLocation(LatLng placeLocation) {
        this.placeLocation = placeLocation;
        return this;
    }
}

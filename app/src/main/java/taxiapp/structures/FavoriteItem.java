package taxiapp.structures;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Nisar on 11/29/2015.
 */
public class FavoriteItem {
    /** 100 for Item to be added <br/>
     * 200 for Already added */
    public int placeIdentifier;
    public String placeName;
    public String placeAddress;
    public LatLng placeLocation;
}

package taxiapp.structures;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Nisar on 11/29/2015.
 */
public class FavoriteItem implements Parcelable {
    /** 100 for Add favorite <br/>
     * 200 for Pickup screen */
    public int placeIdentifier;
    public String placeName;
    public String placeAddress;
    public LatLng placeLocation;

    public FavoriteItem() {
        this.placeIdentifier = -1;
        this.placeName = "";
        this.placeAddress = "";
        this.placeLocation = new LatLng(-1, -1);
    }

    public FavoriteItem(Parcel in) {
        placeIdentifier = in.readInt();
        placeName =in.readString();
        placeAddress =in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(placeIdentifier);
        dest.writeString(placeName);
        dest.writeString(placeAddress);
    }

    public static final Parcelable.Creator<FavoriteItem> CREATOR = new Parcelable.Creator<FavoriteItem>() {
        public FavoriteItem createFromParcel(Parcel in){
            return new FavoriteItem(in);
        }

        public FavoriteItem[] newArray(int size){
            return new FavoriteItem[size];
        }
    };

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

package taxiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.example.taxiapp.MainActivity;
import com.google.gson.Gson;

import taxiapp.constants.AppConstants;
import taxiapp.structures.FavoriteItem;
import taxiapp.structures.Favorites;
import taxiapp.structures.UserDetails;

public class AppPreferences {
	private SharedPreferences mPreferences;
	private Editor mEditor;
	private Context mContext;

	public static final String PREFERENCE_NAME = MainActivity.class.getPackage().getName() + "_pref";

	private static final String KEY_USER_ID = "pref_key_user_id";
	private static final String KEY_USER_FULL_NAME = "pref_key_user_full_name";
	private static final String KEY_USER_EMAIL = "pref_key_user_email";
	private static final String KEY_USER_PHONE = "pref_key_user_phone";
	private static final String KEY_USER_CITY = "pref_key_user_city";
	private static final String KEY_USER_CITY_LAT = "pref_key_user_city_lat";
	private static final String KEY_USER_CITY_LON = "pref_key_user_city_lon";
	private static final String KEY_USER_REFERRAL_CODE = "pref_key_user_referral_code";
	private static final String KEY_USER_LOGGED_IN = "pref_key_user_logged_in";

	private static final String KEY_FAVORITES = "pref_key_favorites_item";

	public AppPreferences(Context context) {
		mContext= context;

		mPreferences= (SharedPreferences) mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		mEditor= mPreferences.edit();
		mEditor.commit();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////// LOGIN ///////////////////////////////////////////////////////////////////////////////////

	public void setLoginSession(UserDetails userDetails) {
		mEditor.putString(KEY_USER_ID, userDetails.user_id);
		mEditor.putString(KEY_USER_FULL_NAME, userDetails.full_name);
		mEditor.putString(KEY_USER_EMAIL, userDetails.email);
		mEditor.putString(KEY_USER_PHONE, userDetails.mobile);
		mEditor.putString(KEY_USER_CITY, userDetails.city);
		mEditor.putFloat(KEY_USER_CITY_LAT, (float) userDetails.cityLat);
		mEditor.putFloat(KEY_USER_CITY_LON, (float) userDetails.cityLon);
		mEditor.putString(KEY_USER_REFERRAL_CODE, userDetails.referal_code);
		mEditor.putBoolean(KEY_USER_LOGGED_IN, true);
		mEditor.commit();
	}

	public boolean isUserLoggedIn() {
		return mPreferences.getBoolean(KEY_USER_LOGGED_IN, false);
	}

	public UserDetails getLoginSession() {
		UserDetails userDetails = new UserDetails();
		userDetails.user_id = mPreferences.getString(KEY_USER_ID, null);
		userDetails.full_name = mPreferences.getString(KEY_USER_FULL_NAME, null);
		userDetails.email = mPreferences.getString(KEY_USER_EMAIL, null);
		userDetails.mobile = mPreferences.getString(KEY_USER_PHONE, null);
		userDetails.city = mPreferences.getString(KEY_USER_CITY, null);
		userDetails.cityLat = mPreferences.getFloat(KEY_USER_CITY_LAT, -1);
		userDetails.cityLon = mPreferences.getFloat(KEY_USER_CITY_LON, -1);
		userDetails.referal_code = mPreferences.getString(KEY_USER_REFERRAL_CODE, null);
		return userDetails;
	}

	public void logoutUser() {
		mEditor.remove(KEY_USER_ID);
		mEditor.remove(KEY_USER_FULL_NAME);
		mEditor.remove(KEY_USER_EMAIL);
		mEditor.remove(KEY_USER_PHONE);
		mEditor.remove(KEY_USER_CITY);
		mEditor.remove(KEY_USER_CITY_LAT);
		mEditor.remove(KEY_USER_CITY_LON);
		mEditor.remove(KEY_USER_REFERRAL_CODE);
		mEditor.remove(KEY_USER_LOGGED_IN);
		mEditor.commit();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	////////// FAVORITES ////////////////////////////////////////////////////////////////////////////////

	public void addFavoriteItem(FavoriteItem favoriteItem) {
		Favorites favoritesObj = null;
		if(mPreferences.contains(KEY_FAVORITES)) {
			String strFavorites = mPreferences.getString(KEY_FAVORITES, null);
			if(strFavorites != null) {
				favoritesObj = (new Gson()).fromJson(strFavorites, Favorites.class);
			}
		} else {
			favoritesObj = new Favorites();
		}

		if(favoritesObj != null) {
			favoritesObj = replaceOrAddFavItem(favoritesObj, favoriteItem);
			String strObj = (new Gson()).toJson(favoritesObj);
			mEditor.putString(KEY_FAVORITES, strObj);
			mEditor.commit();
		}
	}

	private Favorites replaceOrAddFavItem(Favorites destFavorites, FavoriteItem srcFavItem) {
		boolean isReplaced = false;
		for(int i = 0; i < destFavorites.listFavItems.size(); i++) {
			if(destFavorites.listFavItems.get(i).itemId == srcFavItem.itemId) {
				destFavorites.listFavItems.get(i).setFavoriteItemObject(srcFavItem);
				isReplaced = true;
				break;
			}
		}

		if(!isReplaced)
			destFavorites.listFavItems.add(srcFavItem);

		return destFavorites;
	}

	public boolean isFavoriteItemExistsForName(Favorites destFavorites, String name) {
		boolean isExist = false;
		for(int i = 0; i < destFavorites.listFavItems.size(); i++) {
			if(destFavorites.listFavItems.get(i).placeName.trim().equalsIgnoreCase(name.trim())
					&& destFavorites.listFavItems.get(i).placeIdentifier != 100) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}

	public Favorites getFavorites() {
		Favorites favoritesObj = null;
		if(!mPreferences.contains(KEY_FAVORITES)) {
			// Return 2 basic items as on application start
			addFavoriteItem(new FavoriteItem().setItemId(System.currentTimeMillis()).setPlaceIdentifier(100)
					.setPlaceName(AppConstants.STR_FAV_ITEM_HOME));
			addFavoriteItem(new FavoriteItem().setItemId(System.currentTimeMillis()).setPlaceIdentifier(100)
					.setPlaceName(AppConstants.STR_FAV_ITEM_OFFICE));
		}

		if(mPreferences.contains(KEY_FAVORITES)) {
			String strObject = mPreferences.getString(KEY_FAVORITES, null);
			if(strObject != null)
				favoritesObj = (new Gson()).fromJson(strObject, Favorites.class);
		}
		return favoritesObj;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////// OTHERS ////////////////////////////////////////////////////////////////////////////////

	public void clearPreferences() {
		mEditor.clear();
		mEditor.commit();
	}
}

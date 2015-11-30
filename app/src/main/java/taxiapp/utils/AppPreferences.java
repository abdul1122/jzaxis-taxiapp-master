package taxiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.example.taxiapp.MainActivity;
import com.google.gson.Gson;

import taxiapp.structures.FavoriteItem;
import taxiapp.structures.Favorites;
import taxiapp.structures.UserDetails;

public class AppPreferences {
	private SharedPreferences mPreferences;
	private Editor mEditor;
	private Context mContext;

	public static final String PREFERENCE_NAME = MainActivity.class.getPackage().getName() + "_pref";

	private static final String KEY_USER_ID = "pref_key_user_id";
	private static final String KEY_USER_FIRST_NAME = "pref_key_user_first_name";
	private static final String KEY_USER_LAST_NAME = "pref_key_user_first_name";
	private static final String KEY_USER_EMAIL = "pref_key_user_email";
	private static final String KEY_USER_PHONE = "pref_key_user_phone";
	private static final String KEY_USER_CITY = "pref_key_user_city";
	private static final String KEY_USER_REFERRAL_CODE = "pref_key_user_referral_code";
	private static final String KEY_USER_LOGGED_IN = "pref_key_user_logged_in";

	private static final String KEY_FAVORITES = "pref_key_favorites_item";

	public AppPreferences(Context context) {
		mContext= context;

		mPreferences= (SharedPreferences) mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		mEditor= mPreferences.edit();
		mEditor.commit();
	}

	public void setLoginSession(UserDetails userDetails) {
		mEditor.putString(KEY_USER_ID, userDetails.user_id);
		mEditor.putString(KEY_USER_FIRST_NAME, userDetails.first_name);
		mEditor.putString(KEY_USER_LAST_NAME, userDetails.last_name);
		mEditor.putString(KEY_USER_EMAIL, userDetails.email);
		mEditor.putString(KEY_USER_PHONE, userDetails.mobile);
		mEditor.putString(KEY_USER_CITY, userDetails.city);
		mEditor.putString(KEY_USER_REFERRAL_CODE, userDetails.referal_code);
		mEditor.putBoolean(KEY_USER_LOGGED_IN, true);
		mEditor.commit();
	}

	public void addFavorites(FavoriteItem favoriteItem) {
		if(mPreferences.contains(KEY_FAVORITES)) {
			String strObject = mPreferences.getString(KEY_FAVORITES, null);
			if(strObject != null) {
				Favorites favoritesObj = (new Gson()).fromJson(strObject, Favorites.class);
				favoritesObj.listFavItems.add(favoriteItem);
				String strObj = (new Gson()).toJson(favoritesObj);
				mEditor.putString(KEY_FAVORITES, strObj);
			}
		} else {
			Favorites favoritesObj = new Favorites();
			favoritesObj.listFavItems.add(favoriteItem);
			String strObj = (new Gson()).toJson(favoritesObj);
			mEditor.putString(KEY_FAVORITES, strObj);
		}
	}

	public Favorites getFavorites() {
		Favorites favoritesObj = null;
		if(mPreferences.contains(KEY_FAVORITES)) {
			String strObject = mPreferences.getString(KEY_FAVORITES, null);
			if(strObject != null)
				favoritesObj = (new Gson()).fromJson(strObject, Favorites.class);
		} else {
			favoritesObj = null;
		}
		return null;
	}

	public UserDetails getLoginSession() {
		UserDetails userDetails = new UserDetails();
		userDetails.user_id = mPreferences.getString(KEY_USER_ID, null);
		userDetails.first_name = mPreferences.getString(KEY_USER_FIRST_NAME, null);
		userDetails.last_name = mPreferences.getString(KEY_USER_LAST_NAME, null);
		userDetails.email = mPreferences.getString(KEY_USER_EMAIL, null);
		userDetails.mobile = mPreferences.getString(KEY_USER_PHONE, null);
		userDetails.city = mPreferences.getString(KEY_USER_CITY, null);
		userDetails.referal_code = mPreferences.getString(KEY_USER_REFERRAL_CODE, null);
		return userDetails;
	}

	public boolean isUserLoggedIn() {
		return mPreferences.getBoolean(KEY_USER_LOGGED_IN, false);
	}

	public void logoutUser() {
		mEditor.remove(KEY_USER_ID);
		mEditor.remove(KEY_USER_FIRST_NAME);
		mEditor.remove(KEY_USER_LAST_NAME);
		mEditor.remove(KEY_USER_EMAIL);
		mEditor.remove(KEY_USER_PHONE);
		mEditor.remove(KEY_USER_CITY);
		mEditor.remove(KEY_USER_REFERRAL_CODE);
		mEditor.remove(KEY_USER_LOGGED_IN);
		mEditor.commit();
	}

	public void clearPreferences() {
		mEditor.clear();
		mEditor.commit();
	}
}

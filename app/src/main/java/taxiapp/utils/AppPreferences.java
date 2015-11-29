package taxiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.example.taxiapp.MainActivity;

import taxiapp.structures.UserDetails;

// TODO Working in progress by Hassan Jamil
public class AppPreferences {
	private SharedPreferences preferences;
	private Editor editor;
	private Context context;

	public static final String PREFERENCE_NAME = MainActivity.class.getPackage().getName() + "_pref";
	private static final String KEY_DEFAULT_LOCATION = "default_location";

	private static final String KEY_USER_ID = "pref_key_user_id";
	private static final String KEY_USER_FIRST_NAME = "pref_key_user_first_name";
	private static final String KEY_USER_LAST_NAME = "pref_key_user_first_name";
	private static final String KEY_USER_EMAIL = "pref_key_user_email";
	private static final String KEY_USER_PHONE = "pref_key_user_phone";
	private static final String KEY_USER_CITY = "pref_key_user_city";
	private static final String KEY_USER_REFERRAL_CODE = "pref_key_user_referral_code";
	private static final String KEY_USER_LOGGED_IN = "pref_key_user_logged_in";

	public AppPreferences(Context context)
	{
		this.context = context;

		preferences = (SharedPreferences) context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		editor = preferences.edit();
		editor.commit();
	}

	public void saveLoginSession(UserDetails userDetails) {
		editor.putString(KEY_USER_ID, userDetails.user_id);
		editor.putString(KEY_USER_FIRST_NAME, userDetails.first_name);
		editor.putString(KEY_USER_LAST_NAME, userDetails.last_name);
		editor.putString(KEY_USER_EMAIL, userDetails.email);
		editor.putString(KEY_USER_PHONE, userDetails.mobile);
		editor.putString(KEY_USER_CITY, userDetails.city);
		editor.putString(KEY_USER_REFERRAL_CODE, userDetails.referal_code);
		editor.putBoolean(KEY_USER_LOGGED_IN, true);
		editor.commit();
	}


	/**
	 * To set user default location's preferences
	 * @param strLocation like "33.84647,74.9847646"
	 */
	public void setUserDefaultLocation(String strLocation) {
		editor.putString(KEY_DEFAULT_LOCATION, strLocation);
		editor.commit();
	}



	
	public String getDefaultLocation() { return preferences.getString(KEY_DEFAULT_LOCATION, null); }
	public String getUserId() { return preferences.getString(KEY_USER_ID, null); }
	public String getUserEmail() { return preferences.getString(KEY_USER_EMAIL, null); }
	public boolean isUserLoggedIn() { return preferences.getBoolean(KEY_USER_LOGGED_IN, false); }




	public void logoutUser() {
		editor.remove(KEY_USER_ID);
		editor.remove(KEY_USER_EMAIL);
		editor.putBoolean(KEY_USER_LOGGED_IN, false);
		editor.commit();
	}

	public void clearPreferences() {
		editor.clear();
		editor.commit();
	}
}

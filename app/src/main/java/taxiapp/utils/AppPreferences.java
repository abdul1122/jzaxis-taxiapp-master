package taxiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.example.taxiapp.MainActivity;

public class AppPreferences {
	private SharedPreferences preferences;
	private Editor editor;
	private Context context;

	public static final String PREFERENCE_NAME = MainActivity.class.getPackage().getName() + "_pref";
	private static final String KEY_DEFAULT_LOCATION = "default_location";

	private static final String KEY_USER_NAME = "user_name";
	private static final String KEY_USER_EMAIL = "user_email";
	private static final String KEY_USER_ID = "user_id";
	private static final String KEY_USER_LOGGED_IN = "user_logged_in";

	public AppPreferences(Context context)
	{
		this.context = context;

		preferences = (SharedPreferences) context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		editor = preferences.edit();
		editor.commit();
	}

	public void saveLoginSession(String userId, String name, String email,
								 boolean isUserLoggedIn) {
		editor.putString(KEY_USER_ID, userId);
		editor.putString(KEY_USER_NAME, name);
		editor.putString(KEY_USER_EMAIL, email);
		editor.putBoolean(KEY_USER_LOGGED_IN, isUserLoggedIn);
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
	public String getUsername() { return preferences.getString(KEY_USER_NAME, null); }
	public String getUserEmail() { return preferences.getString(KEY_USER_EMAIL, null); }
	public boolean isUserLoggedIn() { return preferences.getBoolean(KEY_USER_LOGGED_IN, false); }




	public void logoutUser() {
		editor.remove(KEY_USER_ID);
		editor.remove(KEY_USER_NAME);
		editor.remove(KEY_USER_EMAIL);
		editor.putBoolean(KEY_USER_LOGGED_IN, false);
		editor.commit();
	}

	public void clearPreferences() {
		editor.clear();
		editor.commit();
	}
}

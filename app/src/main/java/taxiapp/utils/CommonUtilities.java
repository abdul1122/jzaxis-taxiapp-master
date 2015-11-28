package taxiapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class CommonUtilities {

	private static final String TAG = CommonUtilities.class.getSimpleName();

	/**
	 * Shows toast for short duration
	 * @param context
	 * @param msg
	 */
	public static void toastShort(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Shows toast for long duration
	 * @param context
	 * @param msg
	 */
	public static void toastLong(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}


    /**
     * Use to hide soft keyboard from screen whenever its call.
     * @param context
     */
    /*public static void hideSoftKeyboard(Context context){
    	//InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
        
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }*/

	/**
	 * Used to hide/show keyboard instance attached to a view.
	 * @param activity
	 * @param view
	 * @param shouldHide
	 */
	public static void hideShowSoftKeyboardFromView(Activity activity, View view, boolean shouldHide) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(shouldHide)
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		else
			imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	}

	/**
	 * Used to hide/show keyboard by getting focused view.
	 * @param activity
	 * @param shouldHide
	 */
	public static void hideShowSoftKeyboardFromFocusedView(Activity activity, boolean shouldHide) {
		// Check if no view has focus:
		View view = activity.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if(shouldHide)
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			else
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		}
	}
    
    /**
     * Use to get the app's current version code as mentioned in tag <version code> in manifest
     * @return int
     */
    public static int getAppVersion(Activity activity)
	{
		try{
			PackageInfo packageInfo = activity.getPackageManager()
					.getPackageInfo(activity.getPackageName(), 0);
			return packageInfo.versionCode;
		}
		catch(NameNotFoundException e){
			e.printStackTrace();
			return Integer.MIN_VALUE;
		}
	}

	/**
	 * To generate a callback to dialler of phone via ACTION_CALL.
	 * @param context
	 * @param phone
	 */
	public static void call(Context context, String phone) {
		if(!TextUtils.isEmpty(phone)) {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + phone));
			context.startActivity(callIntent);
		}
		else
			toastShort(context, "Phone number is not valid");
	}

	/**
	 * Function used to share text througn other applications.
	 * @param context
	 * @param text
	 * @param subject
	 * @param title
	 * @param dialogHeaderText
	 */
	public static void share(Context context, String text, String subject, String title, String dialogHeaderText) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		intent.putExtra(Intent.EXTRA_TITLE, title);
		context.startActivity(Intent.createChooser(intent, dialogHeaderText));
	}

	public static void sendEmail(Context context, String to, String subject, String body, String dialogHeaderText) {
		try {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("message/rfc822");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
			intent.putExtra(Intent.EXTRA_SUBJECT, subject);
			intent.putExtra(Intent.EXTRA_TEXT   , body);
			context.startActivity(Intent.createChooser(intent, dialogHeaderText));
		}
		catch (Exception ex) {
			toastLong(context, "There are no email clients installed.");
		}
	}
    
    /**
	 * Function will show a RELEASE HASHKEY, 
	 * we used in our facebook app project to get registered our app.
	 * Use this key to register your project on Facebook Developer
	 * @param context
	 */
    public static void showReleaseHashKeyForFacebook(Context context)
	{
		try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
					PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key=Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.v("Hash key:", key);
                Toast.makeText(context, "Release Hashkey:\n"+key, Toast.LENGTH_LONG).show();
            }
		} catch (NameNotFoundException e) {
		} catch (NoSuchAlgorithmException e) {
		}
	}

	/**
	 * You could create an Intent object with necessary component info and then check if the intent
	 * is callable or not.
	 * @param context
	 * @param intent with necessary component info associated with intent object
	 * @return boolean
	 */
	public static boolean isCallable(Context context, Intent intent) {
		List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
}

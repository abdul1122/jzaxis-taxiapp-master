package com.example.taxiapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.net.URLEncoder;

import taxiapp.adapters.AdapterAutoCompleteGeoNamePlaces;
import taxiapp.constants.URLConstants;
import taxiapp.structures.GeoNameCity;
import taxiapp.structures.UserDetails;
import taxiapp.utils.CommonUtilities;
import taxiapp.utils.EditTextUtils;
import taxiapp.utils.GenericTextWatcher;

public class ActivitySignUp extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText etFullName, etPhoneNumber, etEmail, etPassword, etReferralNo;
    private AutoCompleteTextView actvCity;
    private Button btnSignUp;

    private UserDetails userDetails = new UserDetails();
    public static String TAG = ActivitySignUp.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
        initListeners();
    }

    private void init() {
        etFullName = (EditText) findViewById(R.id.et_signup_fullName);
        etPhoneNumber = (EditText) findViewById(R.id.et_signup_phoneNumber);
        etEmail = (EditText) findViewById(R.id.et_signup_email);
        etPassword = (EditText) findViewById(R.id.et_signup_password);
        etReferralNo = (EditText) findViewById(R.id.et_signup_referralCode);
        btnSignUp = (Button) findViewById(R.id.btn_signup_signUp);

        actvCity = (AutoCompleteTextView) findViewById(R.id.actv_signup_city);
        actvCity.setAdapter(new AdapterAutoCompleteGeoNamePlaces(this, R.layout.layout_item_dropdown_list));
        actvCity.setOnItemClickListener(this);
    }

    private void initListeners() {
        btnSignUp.setOnClickListener(this);
        etFullName.addTextChangedListener(new GenericTextWatcher(etFullName));
        etPhoneNumber.addTextChangedListener(new GenericTextWatcher(etPhoneNumber));
        etEmail.addTextChangedListener(new GenericTextWatcher(etEmail));
        etPassword.addTextChangedListener(new GenericTextWatcher(etPassword));
        actvCity.addTextChangedListener(new GenericTextWatcher(actvCity));
        etReferralNo.addTextChangedListener(new GenericTextWatcher(etReferralNo));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_signup_signUp:
                Boolean isValid = getSignUpInputModel();
                if (isValid) {
                    performSignUpTask(userDetails);
                }
                break;
        }
    }

    private Boolean getSignUpInputModel() {
        userDetails = new UserDetails();

        Boolean isValid = false;

        if (EditTextUtils.isFieldEmpty(etFullName)) {
            return isValid;
        } else {
            etFullName.setError(null);
            userDetails.full_name = etFullName.getText().toString();
        }

        if (EditTextUtils.isFieldEmpty(etPhoneNumber)) {
            return isValid;
        } else {
            etPhoneNumber.setError(null);
            userDetails.mobile = etPhoneNumber.getText().toString();
        }

        if (EditTextUtils.isFieldEmpty(etEmail)) {
            return isValid;
        } else {
            etEmail.setError(null);
            userDetails.email = etEmail.getText().toString();
        }

        if (EditTextUtils.isFieldEmpty(etPassword)) {
            return isValid;
        } else {
            etPassword.setError(null);
            userDetails.password = etPassword.getText().toString();
        }

        if (geoNameCity == null) {
            actvCity.requestFocus();
            actvCity.setError(actvCity.getHint().toString() + " is not selected");
            return isValid;
        } else {
            actvCity.setError(null);
            isValid = true;
            userDetails.city = actvCity.getText().toString();
            userDetails.city_lat = geoNameCity.getLat();
            userDetails.city_lon = geoNameCity.getLng();
        }

        if (!EditTextUtils.isFieldEmpty(etReferralNo)) {
            userDetails.referal_code = etReferralNo.getText().toString();
        }

        return isValid;
    }


    private UserDetails getUserDetailsObject(JSONObject jsonObject) {
        try {
            UserDetails userDetails = new UserDetails();
            userDetails.user_id = jsonObject.getString("user_id");
            userDetails.full_name = jsonObject.getString("full_name");
            userDetails.email = jsonObject.getString("email");
            userDetails.mobile = jsonObject.getString("mobile");
            userDetails.city = jsonObject.getString("city");
            userDetails.city_lat = Double.valueOf(jsonObject.getString("city_lat"));
            userDetails.city_lon = Double.valueOf(jsonObject.getString("city_lon"));
            userDetails.referal_code = jsonObject.getString("referal_code");
            return userDetails;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ActivitySignUp.this, ActivitySplash.class));
        finish();
    }

    /**
     * Function is used to call by a UI component
     * @param view
     */
    public void onBackPressed(View view) {
        finish();
        startActivity(new Intent(ActivitySignUp.this, ActivitySplash.class));
    }

    private GeoNameCity geoNameCity = null;
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        geoNameCity = (GeoNameCity) adapterView.getItemAtPosition(position);
        actvCity.setText(geoNameCity.getToponymName() +", "+geoNameCity.getCountryCode() +
                ", " + geoNameCity.getCountryName());
    }

    /**
     * Tag used to associate with the request to server,
     * also we can cancel the request associated with this tag.
     */
    public static final String SERVICE_REQ_TAG_SIGN_UP = "request_server_for_sign_up";

    private void performSignUpTask(UserDetails userDetails) {
        String SERVICE_URL_SIGNUP = null;
        try {
            SERVICE_URL_SIGNUP = URLConstants.SERVICE_URL_SIGNUP + "?txt_full_name=" + URLEncoder.encode(userDetails.full_name, "utf8") +
                    "&txt_email=" + URLEncoder.encode(userDetails.email, "utf8") + "&txt_mobile=" + URLEncoder.encode(userDetails.mobile, "utf8") +
                    "&txt_city=" + URLEncoder.encode(userDetails.city, "utf8") + "&txt_city_lat=" + userDetails.city_lat +
                    "&txt_city_lon=" + userDetails.city_lon + "&txt_password=" + URLEncoder.encode(userDetails.password, "utf8");
            if(userDetails.referal_code != null) {
                SERVICE_URL_SIGNUP += "&txt_referal_code=" + URLEncoder.encode(userDetails.referal_code, "utf8");
            }
            Log.i(TAG, "performSignUpTask() URL: " + SERVICE_URL_SIGNUP);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        // Cancelling all pending requests for the tag
        // Doing it for suggestions because on every typed alphabet the call will be generated
        // to the server so its better to cancel all previous calls before generating next.
        MyApplication.getInstance().getRequestQueue().cancelAll(SERVICE_REQ_TAG_SIGN_UP);

        // Making request to server for results
        StringRequest request = new StringRequest(Request.Method.POST, SERVICE_URL_SIGNUP,
                //On response success
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            response = response.trim();
                            int success = 0;

                            if (response.startsWith("{") && response.endsWith("}")) {
                                JSONObject jsonObject = new JSONObject(response);
                                success = jsonObject.getInt("success");
                                if (success == 0) {
                                    CommonUtilities.toastShort(ActivitySignUp.this, "Sign up failed");
                                } else {
                                    /*UserDetails userDetails = (new Gson()).fromJson(jsonObject.getJSONArray("apps")
                                                    .get(0).toString(), UserDetails.class);*/
                                    JSONObject userObj = (JSONObject) jsonObject.getJSONArray("apps").get(0);
                                    UserDetails userDetails = getUserDetailsObject(userObj);
                                    CommonUtilities.toastShort(ActivitySignUp.this, userDetails.full_name
                                            + " registered successfully");

                                    // Saving preferences for login session
                                    MyApplication.getInstance().getAppPreferences().setLoginSession(userDetails);

                                    // Redirecting user
                                    finish();
                                    startActivity(new Intent(ActivitySignUp.this, ActivityFavorites.class));
                                }
                            }
                            Log.i(TAG, "performSignUpTask() - Response: " + response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                },
                // On response failure
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "performSignUpTask() - ERROR: " + error.getMessage());
                        pDialog.hide();
                    }
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(5000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Setting response to be cached or not
        request.setShouldCache(false);
        // Adding the request to request queue associated with a tag
        MyApplication.getInstance().addToRequestQueue(request, SERVICE_REQ_TAG_SIGN_UP);
    }
}

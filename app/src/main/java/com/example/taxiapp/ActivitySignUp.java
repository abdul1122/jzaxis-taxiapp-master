package com.example.taxiapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import taxiapp.constants.URLConstants;
import taxiapp.structures.SignUp;
import taxiapp.structures.UserDetails;
import taxiapp.utils.CommonUtilities;
import taxiapp.utils.EditTextUtils;
import taxiapp.utils.GenericTextWatcher;

public class ActivitySignUp extends Activity implements View.OnClickListener {

    private String firstName, lastName, phoneNumber, email, password, city, referralCode;
    private EditText etFirstName, etLastName, etPhoneNumber, etEmail, etPassword, etCity, etReferralNo;
    Button btnSignUp;
    private SignUp signUp = new SignUp();
    public static String TAG = ActivitySignUp.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
        initListeners();
    }

    private void init() {
        etFirstName = (EditText) findViewById(R.id.et_signup_firstName);
        etLastName = (EditText) findViewById(R.id.et_signup_lastName);
        etPhoneNumber = (EditText) findViewById(R.id.et_signup_phoneNumber);
        etEmail = (EditText) findViewById(R.id.et_signup_email);
        etPassword = (EditText) findViewById(R.id.et_signup_password);
        etCity = (EditText) findViewById(R.id.et_signup_city);
        etReferralNo = (EditText) findViewById(R.id.et_signup_referralCode);
        btnSignUp = (Button) findViewById(R.id.btn_signup_signUp);
    }

    private void initListeners() {
        btnSignUp.setOnClickListener(this);
        etFirstName.addTextChangedListener(new GenericTextWatcher(etFirstName));
        etLastName.addTextChangedListener(new GenericTextWatcher(etLastName));
        etPhoneNumber.addTextChangedListener(new GenericTextWatcher(etPhoneNumber));
        etEmail.addTextChangedListener(new GenericTextWatcher(etEmail));
        etPassword.addTextChangedListener(new GenericTextWatcher(etPassword));
        etCity.addTextChangedListener(new GenericTextWatcher(etCity));
        etReferralNo.addTextChangedListener(new GenericTextWatcher(etReferralNo));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_signup_signUp:
                Boolean isValid = createSignUpModel();
                if (isValid) {
                    performSignUpTask(signUp);
                }
                break;
        }
    }

    private Boolean createSignUpModel() {
        signUp = new SignUp();

        Boolean isValid = false;

        if (EditTextUtils.isFieldEmpty(etFirstName)) {
            return isValid;
        } else {
            etFirstName.setError(null);
            signUp.firstName = etFirstName.getText().toString();
        }

        if (EditTextUtils.isFieldEmpty(etLastName)) {
            return isValid;
        } else {
            etLastName.setError(null);
            signUp.lastName = etLastName.getText().toString();
        }

        if (EditTextUtils.isFieldEmpty(etPhoneNumber)) {
            return isValid;
        } else {
            etPhoneNumber.setError(null);
            signUp.phoneNumber = etPhoneNumber.getText().toString();
        }

        if (EditTextUtils.isFieldEmpty(etEmail)) {
            return isValid;
        } else {
            etEmail.setError(null);
            signUp.email = etEmail.getText().toString();
        }

        if (EditTextUtils.isFieldEmpty(etPassword)) {
            return isValid;
        } else {
            etPassword.setError(null);
            signUp.password = etPassword.getText().toString();
        }

        if (EditTextUtils.isFieldEmpty(etCity)) {
            return isValid;
        } else {
            etCity.setError(null);
            signUp.city = etCity.getText().toString();
        }

        if (EditTextUtils.isFieldEmpty(etReferralNo)) {
            etReferralNo.requestFocus();
            etReferralNo.setError(etReferralNo.getHint().toString() + " is missing");
            return isValid;
        } else {
            etReferralNo.setError(null);
            isValid = true;
            signUp.referralCode = etReferralNo.getText().toString();
        }

        return isValid;

    }

    /**
     * Tag used to associate with the request to server,
     * also we can cancel the request associated with this tag.
     */
    public static final String SERVICE_REQ_TAG_SIGN_UP = "request_server_for_sign_up";
    public static String SERVICE_URL_SIGNUP = null;

    private void performSignUpTask(SignUp signUp) {
        SERVICE_URL_SIGNUP = URLConstants.SERVICE_URL_SIGNUP + "?txt_first_name=" + signUp.firstName +
                "&txt_last_name=" + signUp.lastName + "&txt_email=" + signUp.email + "&txt_mobile=" + signUp.phoneNumber +
                "&txt_city=" + signUp.city + "&txt_password=" + signUp.password + "&txt_referal_code=" + signUp.referralCode;
        Log.i(TAG, "performSignUpTask() URL: " + SERVICE_URL_SIGNUP);

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
                                    CommonUtilities.toastShort(ActivitySignUp.this, userDetails.last_name
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

    private UserDetails getUserDetailsObject(JSONObject jsonObject) {
        try {
            UserDetails userDetails = new UserDetails();
            userDetails.user_id = jsonObject.getString("user_id");
            userDetails.first_name = jsonObject.getString("first_name");
            userDetails.last_name = jsonObject.getString("last_name");
            userDetails.email = jsonObject.getString("email");
            userDetails.mobile = jsonObject.getString("mobile");
            userDetails.city = jsonObject.getString("city");
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

    public void onBackPressed(View view) {
        finish();
    }
}

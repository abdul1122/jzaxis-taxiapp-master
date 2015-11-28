package com.example.taxiapp;

import android.app.Activity;
import android.app.ProgressDialog;
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

import taxiapp.constants.URLConstants;
import taxiapp.structures.SignUp;

public class ActivitySignUp extends Activity {

    String firstName,lastName,phoneNumber,email,password,city,referralCode;
    EditText etFirstName, etLastName, etPhoneNumber, etEmail, etPassword, etCity, etReferralNo;
    Button btnSignUp;
    public static String TAG = ActivitySignUp.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        initListeners();
    }

    private void init(){
        etFirstName =(EditText)findViewById(R.id.et_signup_firstName);
        etLastName =(EditText)findViewById(R.id.et_signup_lastName);
        etPhoneNumber =(EditText)findViewById(R.id.et_signup_phoneNumber);
        etEmail =(EditText)findViewById(R.id.et_signup_email);
        etPassword =(EditText)findViewById(R.id.et_signup_password);
        etCity =(EditText)findViewById(R.id.et_signup_city);
        etReferralNo =(EditText)findViewById(R.id.et_signup_referralCode);
        btnSignUp =(Button) findViewById(R.id.btn_signup_btnSignUp);
    }

    private void initListeners() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp signUp = new SignUp();
                signUp.firstName = etFirstName.getText().toString();
                signUp.lastName = etLastName.getText().toString();
                signUp.email = etEmail.getText().toString();
                signUp.phoneNumber = etPhoneNumber.getText().toString();
                signUp.password = etPassword.getText().toString();
                signUp.city = etCity.getText().toString();
                signUp.referralCode = etReferralNo.getText().toString();
                performSignUpTask(signUp);
            }
        });
    }

    /** Tag used to associate with the request to server,
     * also we can cancel the request associated with this tag. */
    public static final String SERVICE_REQ_TAG_SIGN_UP = "request_server_for_sign_up";
    public static String SERVICE_URL_SIGNUP = null;
    private void performSignUpTask(SignUp signUp) {
        SERVICE_URL_SIGNUP = URLConstants.SERVICE_URL_SIGNUP + "?txt_first_name="+signUp.firstName+
                "&txt_last_name="+signUp.lastName+"&txt_email="+signUp.email+"&txt_mobile="+signUp.phoneNumber+
                "&txt_city="+signUp.city+"&txt_password="+signUp.password+"&txt_referal_code="+signUp.referralCode;
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
                        Log.i(TAG, "performSignUpTask() - Response: " + response);
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

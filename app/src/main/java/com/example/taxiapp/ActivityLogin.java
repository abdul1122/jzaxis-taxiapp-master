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

import taxiapp.constants.URLConstants;
import taxiapp.structures.Login;

public class ActivityLogin extends Activity implements View.OnClickListener {

    EditText etEmail, etPassword;
    Button btnLogin, btnSignup;
    public static String TAG = ActivityLogin.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        initListeners();
    }

    private void init() {
        etEmail =(EditText)findViewById(R.id.et_login_email);
        etPassword =(EditText)findViewById(R.id.et_login_password);
        btnLogin =(Button) findViewById(R.id.btn_login_login);
        btnSignup =(Button) findViewById(R.id.btn_login_signup);
    }

    private void initListeners() {
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id  = view.getId();
        switch(id) {
            case R.id.btn_login_login:
                Login login = new Login();
                login.email = etEmail.getText().toString();
                login.password = etPassword.getText().toString();
                performLoginTask(login);
                break;
            case R.id.btn_login_signup:
                startActivity(new Intent(ActivityLogin.this, ActivitySignUp.class));
                break;
        }
    }

    /** Tag used to associate with the request to server,
     * also we can cancel the request associated with this tag. */
    public static final String SERVICE_REQ_TAG_LOGIN = "request_server_for_login";
    public static String SERVICE_URL_LOGIN = null;
    private void performLoginTask(Login login) {
        SERVICE_URL_LOGIN = URLConstants.SERVICE_URL_LOGIN + "?txt_email="+login.email+"&txt_password="
                + login.password;
        Log.i(TAG, "performLoginTask() URL: " + SERVICE_URL_LOGIN);

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        // Cancelling all pending requests for the tag
        // Doing it for suggestions because on every typed alphabet the call will be generated
        // to the server so its better to cancel all previous calls before generating next.
        MyApplication.getInstance().getRequestQueue().cancelAll(SERVICE_REQ_TAG_LOGIN);

        // Making request to server for results
        StringRequest request = new StringRequest(Request.Method.POST, SERVICE_URL_LOGIN,
                //On response success
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "performLoginTask() - Response: " + response);
                        pDialog.hide();
                    }
                },
                // On response failure
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "performLoginTask() - ERROR: " + error.getMessage());
                        pDialog.hide();
                    }
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(5000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Setting response to be cached or not
        request.setShouldCache(false);
        // Adding the request to request queue associated with a tag
        MyApplication.getInstance().addToRequestQueue(request, SERVICE_REQ_TAG_LOGIN);
    }
}

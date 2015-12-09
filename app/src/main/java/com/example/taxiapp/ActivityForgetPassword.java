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

import java.net.URLEncoder;

import taxiapp.constants.URLConstants;
import taxiapp.structures.Login;
import taxiapp.structures.UserDetails;
import taxiapp.utils.CommonUtilities;
import taxiapp.utils.EditTextUtils;
import taxiapp.utils.GenericTextWatcher;

public class ActivityForgetPassword extends Activity implements View.OnClickListener {

    EditText etEmail;
    Button btnSend;
    public static String TAG = ActivityForgetPassword.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        init();
        initListeners();
    }

    private void init() {
        etEmail = (EditText) findViewById(R.id.et_forget_password_email);
        btnSend = (Button) findViewById(R.id.btn_forget_password_send);
    }

    private void initListeners() {
        btnSend.setOnClickListener(this);
        etEmail.addTextChangedListener(new GenericTextWatcher(etEmail));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_forget_password_send:
                sendButtonTasks();
                break;
        }
    }

    /**
     * Function is used to call by a UI component
     * @param view
     */
    public void onBackPressed(View view) {
        finish();
        startActivity(new Intent(ActivityForgetPassword.this, ActivitySplash.class));
    }

    private void sendButtonTasks() {
        if (!EditTextUtils.isFieldEmpty(etEmail)) {
            performForgetPasswordTask(etEmail.getText().toString());
        }
    }

    /**
     * Tag used to associate with the request to server,
     * also we can cancel the request associated with this tag.
     */
    public static final String SERVICE_REQ_TAG_FORGET_PASSWORD = "request_server_for_forget_password";
    private void performForgetPasswordTask(String email) {
        String SERVICE_URL_FORGET_PASSWORD = null;
        try {
            SERVICE_URL_FORGET_PASSWORD = URLConstants.SERVICE_URL_FORGET_PASSWORD + "?txt_email=" + URLEncoder.encode(email, "utf8");
            Log.i(TAG, "performForgetPasswordTask() URL: " + SERVICE_URL_FORGET_PASSWORD);
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
        MyApplication.getInstance().getRequestQueue().cancelAll(SERVICE_REQ_TAG_FORGET_PASSWORD);

        // Making request to server for results
        StringRequest request = new StringRequest(Request.Method.POST, SERVICE_URL_FORGET_PASSWORD,
                // On response success
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            response = response.trim();
                            String message = null;

                            if (response.startsWith("{") && response.endsWith("}")) {
                                JSONObject jsonObject = new JSONObject(response);
                                message = jsonObject.getString("message");
                                if (message != null) {
                                    CommonUtilities.toastShort(ActivityForgetPassword.this, message);
                                    if(message.contains("check your email account")) {
                                        finish();
                                    }
                                }
                            }
                            Log.i(TAG, "performForgetPasswordTask() - Response: " + response);
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
                        Log.i(TAG, "performForgetPasswordTask() - ERROR: " + error.getMessage());
                        pDialog.hide();
                    }
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(5000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Setting response to be cached or not
        request.setShouldCache(false);
        // Adding the request to request queue associated with a tag
        MyApplication.getInstance().addToRequestQueue(request, SERVICE_REQ_TAG_FORGET_PASSWORD);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        // startActivity(new Intent(ActivityForgetPassword.this, ActivitySplash.class));
    }
}

package com.example.taxiapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URLEncoder;

import taxiapp.constants.URLConstants;
import taxiapp.structures.Login;
import taxiapp.structures.UserDetails;
import taxiapp.utils.CommonUtilities;
import taxiapp.utils.EditTextUtils;
import taxiapp.utils.GenericTextWatcher;

public class ActivityLogin extends Activity implements View.OnClickListener {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvForgetPassword;
    public static String TAG = ActivityLogin.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        initListeners();
    }

    private void init() {
        etEmail = (EditText) findViewById(R.id.et_login_email);
        etPassword = (EditText) findViewById(R.id.et_login_password);
        btnLogin = (Button) findViewById(R.id.btn_login_login);
        tvForgetPassword = (TextView) findViewById(R.id.tv_login_forget_password);
    }

    private void initListeners() {
        btnLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        etEmail.addTextChangedListener(new GenericTextWatcher(etEmail));
        etPassword.addTextChangedListener(new GenericTextWatcher(etPassword));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_login_login:
                loginButtonTasks();
                break;
            case R.id.tv_login_forget_password:
                startActivity(new Intent(ActivityLogin.this, ActivityForgetPassword.class));
                break;
        }
    }

    /**
     * Function is used to call by a UI component
     * @param view
     */
    public void onBackPressed(View view) {
        finish();
        startActivity(new Intent(ActivityLogin.this, ActivitySplash.class));
    }

    private void loginButtonTasks() {
        Login login = new Login();

        if (!EditTextUtils.isFieldEmpty(etEmail))
            login.email = etEmail.getText().toString();

        if (!EditTextUtils.isFieldEmpty(etPassword))
            login.password = etPassword.getText().toString();

        performLoginTask(login);
    }

    /**
     * Tag used to associate with the request to server,
     * also we can cancel the request associated with this tag.
     */
    public static final String SERVICE_REQ_TAG_LOGIN = "request_server_for_login";
    public static String SERVICE_URL_LOGIN = null;

    private void performLoginTask(Login login) {
        try {
            SERVICE_URL_LOGIN = URLConstants.SERVICE_URL_LOGIN + "?txt_email=" + URLEncoder.encode(login.email, "utf8") +
                    "&txt_password=" + URLEncoder.encode(login.password, "utf8");
            Log.i(TAG, "performLoginTask() URL: " + SERVICE_URL_LOGIN);
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
        MyApplication.getInstance().getRequestQueue().cancelAll(SERVICE_REQ_TAG_LOGIN);

        // Making request to server for results
        StringRequest request = new StringRequest(Request.Method.POST, SERVICE_URL_LOGIN,
                // On response success
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
                                    CommonUtilities.toastShort(ActivityLogin.this, "Login failed");
                                } else {
                                    JSONObject userObj = (JSONObject) jsonObject.getJSONArray("apps").get(0);
                                    UserDetails userDetails = getUserDetailsObject(userObj);
                                    CommonUtilities.toastShort(ActivityLogin.this, userDetails.full_name
                                            + " logged in successfully");

                                    // Saving preferences for login session
                                    MyApplication.getInstance().getAppPreferences().setLoginSession(userDetails);

                                    // Redirecting user
                                    finish();
                                    startActivity(new Intent(ActivityLogin.this, ActivityFavorites.class));
                                }
                            }
                            Log.i(TAG, "performLoginTask() - Response: " + response);
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

        finish();
        startActivity(new Intent(ActivityLogin.this, ActivitySplash.class));
    }
}

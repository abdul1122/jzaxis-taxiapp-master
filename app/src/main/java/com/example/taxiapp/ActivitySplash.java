package com.example.taxiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ActivitySplash extends Activity implements View.OnClickListener
{
	private static final String TAG = ActivitySplash.class.getSimpleName();
	ImageButton btnSignIn, btnSignup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		init();
		initListeners();

		if(MyApplication.getInstance().getAppPreferences().isUserLoggedIn()) {
			startActivity(new Intent(ActivitySplash.this, ActivityFavorites.class));
			finish();
		}
	}
	private void init(){
		btnSignIn=(ImageButton)findViewById(R.id.btn_splash_signin);
		btnSignup=(ImageButton)findViewById(R.id.btn_splash_signup);
	}
	private void initListeners() {
		btnSignIn.setOnClickListener(this);
		btnSignup.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int id  = view.getId();
		switch(id) {
			case R.id.btn_splash_signin:
				finish();
				startActivity(new Intent(ActivitySplash.this, ActivityLogin.class));
				break;
			case R.id.btn_splash_signup:
				finish();
				startActivity(new Intent(ActivitySplash.this, ActivitySignUp.class));
				break;
		}
	}
}

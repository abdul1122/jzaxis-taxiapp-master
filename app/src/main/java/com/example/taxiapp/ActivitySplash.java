package com.example.taxiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import taxiapp.structures.SignUp;

public class ActivitySplash extends Activity
{
	private static final String TAG = ActivitySplash.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new Thread(new Runnable() {
			@Override
			public void run() 
			{
				try {
					Thread.sleep(3000);

					//if(MyApplication.getInstance().getAppPreferences().isUserLoggedIn())
						startActivity(new Intent(ActivitySplash.this, ActivitySignUp.class));
					/*else
						startActivity(new Intent(ActivitySplash.this, ActivityLoginSignup.class));*/

					finish();
					
				} catch (InterruptedException e) {
					e.printStackTrace();
					
					return;
				}
			}
		}).start();
	}
}

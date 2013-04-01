package com.example.solarpanelmanager;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.Constants;

public class SplashActivity extends SherlockActivity {
	// Holds length of time to keep splash screen up
	private int SPLASH_DELAY = 1000; // 1000 = 1 sec

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		// This helps us schedule the transition to MainActivity
		TimerTask timer_task = new TimerTask() {
			@Override
			public void run() {
				String deviceId = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getString(
						Constants.CURRENT_DEVICE, null);
				if (deviceId == null) {
					// The user hasn't chosen a device to manage yet, so send
					// them to the screen to choose one
					Intent mainIntent = new Intent().setClass(SplashActivity.this, ConnectActivity.class);
					startActivity(mainIntent);
				} else {
					// Send them straight to the actual details screen.
					Intent mainIntent = new Intent().setClass(SplashActivity.this, BatteryActivity.class);
					startActivity(mainIntent);
				}
				finish();
			}
		};

		Timer timer = new Timer();
		timer.schedule(timer_task, SPLASH_DELAY);
	}
}

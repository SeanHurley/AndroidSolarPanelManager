package com.example.solarpanelmanager;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.actionbarsherlock.app.SherlockActivity;

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
				Intent mainIntent = new Intent().setClass(SplashActivity.this, MainActivity.class);
				startActivity(mainIntent);
				finish();
			}
		};

		Timer timer = new Timer();
		timer.schedule(timer_task, SPLASH_DELAY);
	}
}

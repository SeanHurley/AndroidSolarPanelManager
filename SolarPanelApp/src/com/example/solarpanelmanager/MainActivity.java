package com.example.solarpanelmanager;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.Constants;
import com.example.bluetooth.Callback;
import com.example.bluetooth.HistoryHandler;
import com.example.solarpanelmanager.api.responses.HistoryResponse;

public class MainActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// BUTTON: Connect to existing device
		final Button buttonConnect = (Button) findViewById(R.id.button_connect_device);
		buttonConnect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, ConnectActivity.class);
				startActivity(i);
			}
		});

		// Settings
		final Button buttonSettings = (Button) findViewById(R.id.button_settings);
		buttonSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mainIntent = new Intent().setClass(MainActivity.this, BatteryActivity.class);
				MainActivity.this.startActivity(mainIntent);
			}
		});

		// About
		final Button buttonAbout = (Button) findViewById(R.id.button_about);
		buttonAbout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		final Button buttonHistoricalData = (Button) findViewById(R.id.button_historical_data);
		buttonHistoricalData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Do a basic call to the device for testing purposes.

				String deviceId = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString(
						Constants.CURRENT_DEVICE, null);
				if (deviceId == null) {
					// TODO - Tell the user that something is wrong
				}

				// TODO use the deviceid when calling the handler
				HistoryHandler call = new HistoryHandler(new Callback<HistoryResponse>() {

					@Override
					public void onComplete(HistoryResponse response) {
						if (response == null) {
							// TODO Something went wrong Alert the user
							return;
						}

						LineGraph lineGraph = new LineGraph();
						/**
						 * TODO: Pass a collection of SnapshotResponses to
						 * display in our graph
						 */
						Intent lineGraphIntent = lineGraph.getIntent(MainActivity.this, response.getHistoryData());
						startActivity(lineGraphIntent);
					}

				}, "14:10:9F:E7:CA:93");
				call.performAction();
			}
		});
	}
}

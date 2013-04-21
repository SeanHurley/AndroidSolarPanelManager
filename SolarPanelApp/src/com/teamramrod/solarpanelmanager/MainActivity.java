package com.teamramrod.solarpanelmanager;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.solarpanelmanager.R;
import com.teamramrod.Constants;
import com.teamramrod.bluetooth.Callback;
import com.teamramrod.bluetooth.HistoryHandler;
import com.teamramrod.solarpanelmanager.api.responses.HistoryResponse;

public class MainActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

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

				String pass = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString(
						Constants.PASS_PHRASE_PREFERENCE, null);
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
						//Intent lineGraphIntent = lineGraph.getView(MainActivity.this, response.getHistoryData());
						//startActivity(lineGraphIntent);
					}

				}, deviceId, pass);
				call.performAction();
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		
		switch (item.getItemId()) {
		case R.id.menu_history:
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_schedule:
			//TODO: start schedule activity
			return true;
		case R.id.menu_device_settings:
			//TODO: start device settings activity
			return true;
		case R.id.menu_change_device:
			intent = new Intent(this, ConnectActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_settings:
			intent = new Intent(this, PreferencesActivity.class);
			startActivity(intent);
			return true;
		case android.R.id.home:
			onBackPressed();
//			intent = new Intent(this, BatteryActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

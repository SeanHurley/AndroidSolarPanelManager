package com.example.solarpanelmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.bluetooth.Callback;
import com.example.bluetooth.HistoryHandler;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.HistoryResponse;

public class MainActivity extends Activity {
	private final int REQUEST_ENABLE_BT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// BUTTON: Connect to existing device
		final Button buttonConnect = (Button) findViewById(R.id.button_connect_device);
		buttonConnect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// Add a new device
		final Button buttonAddDevice = (Button) findViewById(R.id.button_add_device);
		buttonAddDevice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// Settings
		final Button buttonSettings = (Button) findViewById(R.id.button_settings);
		buttonSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// About
		final Button buttonAbout = (Button) findViewById(R.id.button_about);
		buttonAbout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// Historical Data
		final Button buttonHistoricalData = (Button) findViewById(R.id.button_historical_data);
		buttonHistoricalData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Do a basic call to the device for testing purposes.
				HistoryHandler call = new HistoryHandler(new Callback() {
					@Override
					public void onComplete(BaseResponse json) {
						if (json == null) {
							// TODO Something went wrong Alert the user
							return;
						}
						HistoryResponse response = (HistoryResponse) json;
						LineGraph lineGraph = new LineGraph();
						/**
						 * TODO: Pass a collection of SnapshotResponses to
						 * display in our graph
						 */
						Intent lineGraphIntent = lineGraph.getIntent(MainActivity.this, response.getHistoryData());
						startActivity(lineGraphIntent);
					}
				});
				call.performAction();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

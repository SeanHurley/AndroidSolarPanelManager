package com.example.solarpanelmanager;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.bluetooth.Callback;
import com.example.bluetooth.SnapshotHandler;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Do a basic call to the device for testing purposes.
		SnapshotHandler call = new SnapshotHandler(new Callback() {

			@Override
			public void onComplete(JSONObject json) {
				System.out.println("---Finished---");
				Toast.makeText(MainActivity.this, json.toJSONString(), Toast.LENGTH_LONG).show();
			}
		});
		call.performAction();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

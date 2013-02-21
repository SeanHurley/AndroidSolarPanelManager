package com.example.solarpanelmanager;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.example.bluetooth.Callback;
import com.example.bluetooth.HistoryCaller;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		super.onStart();
		test.run();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	Thread test = new Thread(new Runnable() {

		@Override
		public void run() {

			HistoryCaller call = new HistoryCaller(new Callback() {

				@Override
				public void onComplete(JSONObject json) {
					System.out.println(json.toString());
				}
			});
			call.performAction();
		}
	});

}

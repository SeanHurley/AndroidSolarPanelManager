package com.example.solarpanelmanager;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.Constants;
import com.example.bluetooth.Callback;
import com.example.bluetooth.SetChargeConstraintsHandler;
import com.example.bluetooth.ViewChargeConstraintsHandler;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.ViewChargeConstraintsResponse;

public class BatteryActivity extends SherlockActivity {
	private int minVal;
	private int maxVal;

	private SeekBar min;
	private SeekBar max;
	private TextView minvalue;
	private TextView maxvalue;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battery);

		minVal = 0;
		maxVal = 100;

		minvalue = (TextView) findViewById(R.id.minView);
		maxvalue = (TextView) findViewById(R.id.maxView);
		min = (SeekBar) findViewById(R.id.minbar);
		max = (SeekBar) findViewById(R.id.maxbar);

		String deviceId = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.CURRENT_DEVICE, null);
		if (deviceId == null) {
			// TODO - Tell the user that something is wrong
		}

		// TODO use the deviceid when calling the handler
		ViewChargeConstraintsHandler handler = new ViewChargeConstraintsHandler(
				new Callback<ViewChargeConstraintsResponse>() {

					@Override
					public void onComplete(ViewChargeConstraintsResponse response) {
						if (response.getResult() == 200) {
							minVal = response.getMin();
							maxVal = response.getMax();
							System.out.println("start: " + minVal + ", " + maxVal);
							min.setProgress(minVal);
							min.refreshDrawableState();
							max.setProgress(maxVal);
							max.refreshDrawableState();
							View v = findViewById(R.id.activityIndicator);
							v.setVisibility(View.GONE);

							v = findViewById(R.id.maxbar);
							v.setVisibility(View.VISIBLE);

							v = findViewById(R.id.minbar);
							v.setVisibility(View.VISIBLE);

						} else {
							System.out.println("failure in communication");
						}
					}

				}, "14:10:9F:E7:CA:93");

		handler.performAction();

		minvalue.setText("Minimum:" + minVal);
		maxvalue.setText("Maximum:" + maxVal);

		min.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				minvalue.setText("Minimum: " + progress);
				minVal = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				System.out.println("min: " + minVal + ", max: " + maxVal);
				updateLevels();
			}
		});

		max.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				maxvalue.setText("Maximum: " + progress);
				maxVal = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				System.out.println("min: " + minVal + ", max: " + maxVal);
				updateLevels();
			}
		});
	}

	private void updateLevels() {

		SetChargeConstraintsHandler call = new SetChargeConstraintsHandler(new Callback<BaseResponse>() {
			@Override
			public void onComplete(BaseResponse response) {
				System.out.println(response.getResult());
			}
		}, "14:10:9F:E7:CA:93", maxVal, minVal);

		call.performAction();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_battery_settings_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_change_device) {
			Intent intent = new Intent(this, ConnectActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.menu_settings) {
			Intent intent = new Intent(this, PreferencesActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}

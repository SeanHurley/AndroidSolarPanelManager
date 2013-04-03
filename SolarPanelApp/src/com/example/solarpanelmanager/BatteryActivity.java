package com.example.solarpanelmanager;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.bluetooth.Callback;
import com.example.bluetooth.SetChargeConstraintsHandler;
import com.example.bluetooth.SnapshotHandler;
import com.example.bluetooth.ViewChargeConstraintsHandler;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;
import com.example.solarpanelmanager.api.responses.ViewChargeConstraintsResponse;

public class BatteryActivity extends Activity {
	private int minVal;
	private int maxVal;
	private SeekBar min;
	private SeekBar max;
	private int level;
	private TextView minvalue;
	private TextView maxvalue;
	private TextView snapshot;
	private BatteryLevel bl;
	private double battery_voltage;
	private double battery_current;
	private double pvcurrent;
	private double pvvoltage;
	private long timestamp;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battery);

		minVal = 0;
		maxVal = 100;
		level = 1;
		battery_voltage = 0;
		battery_current = 0;
		pvcurrent = 0;
		pvvoltage = 0;
		timestamp = 0;

		minvalue = (TextView) findViewById(R.id.minView);
		maxvalue = (TextView) findViewById(R.id.maxView);
		snapshot = (TextView) findViewById(R.id.snapshot);
		min = (SeekBar) findViewById(R.id.minbar);
		max = (SeekBar) findViewById(R.id.maxbar);

		ViewChargeConstraintsHandler i = new ViewChargeConstraintsHandler(
				new Callback<ViewChargeConstraintsResponse>() {

					@Override
					public void onComplete(
							ViewChargeConstraintsResponse response) {
						if (response.getResult() == 200) {
							minVal = response.getMin();
							maxVal = response.getMax();
							System.out.println("start: " + minVal + ", "
									+ maxVal);
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

				});

		i.performAction();

		SnapshotHandler s = new SnapshotHandler(
				new Callback<SnapshotResponse>() {

					@Override
					public void onComplete(SnapshotResponse response) {
						if (response.getResult() == 200) {
							level = response.getBatteryPercent();
							battery_voltage = response.getBatteryVoltage();
							battery_current = response.getBatteryCurrent();
							pvvoltage = response.getPVVoltage();
							pvcurrent = response.getPVCurrent();
							timestamp = response.getTimestamp();
						} else {
							System.out.println("failure in communication");
						}
					}

				});

		s.performAction();

		minvalue.setText("Minimum Voltage:" + minVal);
		maxvalue.setText("Maximum Voltage:" + maxVal);
		snapshot.setText("Battery Voltage: " + battery_voltage
				+ " Battery Current: " + battery_current + "\n PV Voltage: "
				+ pvvoltage + " PV Current: " + pvcurrent + "\n Timestamp: "
				+ timestamp);
		bl = new BatteryLevel(getApplicationContext(),
				BatteryLevel.SIZE_NOTIFICATION);
		bl.setLevel(level);
		ImageView battery_image = (ImageView) findViewById(R.id.currVoltage);
		battery_image.setImageBitmap(bl.getBitmap());

		min.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
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
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
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

		SetChargeConstraintsHandler call = new SetChargeConstraintsHandler(
				new Callback<BaseResponse>() {
					@Override
					public void onComplete(BaseResponse response) {
						// System.out.println(response.getResult());
					}
				}, maxVal, minVal);

		call.performAction();

	}
}

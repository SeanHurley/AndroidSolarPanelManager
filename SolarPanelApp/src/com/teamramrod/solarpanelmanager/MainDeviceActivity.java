package com.teamramrod.solarpanelmanager;

import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.example.solarpanelmanager.R;
import com.teamramrod.Constants;
import com.teamramrod.bluetooth.Callback;
import com.teamramrod.bluetooth.SnapshotHandler;
import com.teamramrod.solarpanelmanager.api.responses.SnapshotResponse;

/**
 * This class is used to get information about the battery and display it on the
 * screen depending on what mode has been selected (Power-user or Normal).
 */
public class MainDeviceActivity extends BaseActivity {

	private int minVal;
	private int maxVal;
	private int level;
	private View activityIndicator;
	private TextView minChargeLevel;
	private TextView maxChargeLevel;
	private TextView snapshot;
	private TextView snapshot_powered;
	private TextView batteryleveltext;
	private ImageView battery_image;
	private BatteryImageCreator batteryLevel;
	private double battery_voltage;
	private double battery_current;
	private double pvcurrent;
	private double pvvoltage;
	private long timestamp;
	private boolean powerUserEnabled;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_device);

		minVal = 0;
		maxVal = 100;
		level = 1;
		battery_voltage = 0;
		battery_current = 0;
		pvcurrent = 0;
		pvvoltage = 0;
		timestamp = 0;
		batteryLevel = new BatteryImageCreator(getApplicationContext(), BatteryImageCreator.SIZE_NOTIFICATION);

		getUI();
	}

	/**
	 * This method gets all the text and image fields to be displayed.
	 */
	private void getUI() {
		activityIndicator = findViewById(R.id.activityIndicator);
		minChargeLevel = (TextView) findViewById(R.id.min_charge_text);
		maxChargeLevel = (TextView) findViewById(R.id.max_charge_text);
		snapshot = (TextView) findViewById(R.id.snapshot);
		snapshot_powered = (TextView) findViewById(R.id.snapshot_powered);
		batteryleveltext = (TextView) findViewById(R.id.currVoltageText);
		battery_image = (ImageView) findViewById(R.id.currVoltage);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getData();
	}

	/**
	 * Displays the image and text fields depending on user mode selected.
	 */
	private void showUI() {
		powerUserEnabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				Constants.POWER_USER_PREFERENCE, false);

		activityIndicator.setVisibility(View.GONE);
		battery_image.setVisibility(View.VISIBLE);
		batteryleveltext.setVisibility(View.VISIBLE);

		if (!powerUserEnabled) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			battery_image.setLayoutParams(params);
			snapshot.setVisibility(View.VISIBLE);
			snapshot_powered.setVisibility(View.INVISIBLE);

		} else {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			battery_image.setLayoutParams(params);
			snapshot.setVisibility(View.VISIBLE);
			snapshot_powered.setVisibility(View.VISIBLE);
			maxChargeLevel.setVisibility(View.VISIBLE);
			minChargeLevel.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void setupActionBar() {
		ActionBar actionBar = getSupportActionBar();
		String deviceName = PreferenceManager.getDefaultSharedPreferences(this).getString(
				Constants.CURRENT_DEVICE_NAME, "Unknown Device");
		actionBar.setTitle(R.string.menu_activity_main_device_title);
		actionBar.setSubtitle(deviceName);
	}

	/**
	 * Hides all the image and text fields during the loading process of the
	 * data during updates.
	 */
	private void hideUI() {
		activityIndicator.setVisibility(View.VISIBLE);
		battery_image.setVisibility(View.INVISIBLE);
		batteryleveltext.setVisibility(View.INVISIBLE);
		snapshot_powered.setVisibility(View.INVISIBLE);
		snapshot.setVisibility(View.INVISIBLE);
		maxChargeLevel.setVisibility(View.INVISIBLE);
		minChargeLevel.setVisibility(View.INVISIBLE);
	}

	/**
	 * Communicates with the device to get information about the battery using
	 * the SnapshotHandler.
	 */
	private void getData() {
		powerUserEnabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				Constants.POWER_USER_PREFERENCE, false);
		String deviceId = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.CURRENT_DEVICE, null);
		String pass = PreferenceManager.getDefaultSharedPreferences(this).getString(
				Constants.PASS_PHRASE_PREFERENCE + deviceId, null);
		if (deviceId == null) {
			showDeviceNotAvailable();
		}
		hideUI();
		SnapshotHandler snapshotHandler = getSnapshotResponse(deviceId, pass);

		snapshotHandler.performAction();
	}

	/**
	 * Power User mode contains detailed battery information while normal user
	 * mode only contains the power drawn and suppied levels as well as current
	 * battery charge level.
	 */
	private SnapshotHandler getSnapshotResponse(String deviceId, String pass) {
		SnapshotHandler snapshotHandler = new SnapshotHandler(new Callback<SnapshotResponse>() {

			@Override
			public void onComplete(SnapshotResponse response) {
				if (response == null) {
					showDeviceNotAvailable();
					return;
				}

				showUI();

				if (response.getResult() == 200) {
					Resources res = assignSnapshotResponse(response);
					if (!powerUserEnabled) {
						snapshot.setText(res.getString(R.string.battery_in) + "\n"
								+ res.getString(R.string.battery_out) + "\n"
								+ res.getString(R.string.battery_estimatedtime));
					} else {
						snapshot.setText(res.getString(R.string.battery_voltage) + ":" + round(battery_voltage, 2)
								+ "\n" + res.getString(R.string.battery_curr) + ":" + round(battery_current, 2) + "\n"
								+ res.getString(R.string.PV_voltage) + ":" + round(pvvoltage, 2) + "\n"
								+ res.getString(R.string.PV_current) + ":" + round(pvcurrent, 2) + "\n"
								+ res.getString(R.string.Timestamp) + ":" + round(timestamp, 2));
					}
					batteryleveltext.setText("" + level);
					battery_image.setImageBitmap(batteryLevel.getBitmap());
					minVal = response.getMin();
					maxVal = response.getMax();
					minChargeLevel.setText(res.getString(R.string.min_charge_label) + ": " + minVal);
					maxChargeLevel.setText(res.getString(R.string.max_charge_label) + ": " + maxVal);
				} else if (response.getResult() == 403) {
					showForbiddenErrorDialog();
				} else {
					System.out.println("failure in communication, response: " + response.getResult());
				}
			}

		}, deviceId, pass);
		return snapshotHandler;
	}

	/**
	 * Helper function to get and set the battery information from the snapshot
	 * handler.s
	 */
	private Resources assignSnapshotResponse(SnapshotResponse response) {
		level = response.getBatteryPercent();
		battery_voltage = response.getBatteryVoltage();
		battery_current = response.getBatteryCurrent();
		pvvoltage = response.getPVVoltage();
		pvcurrent = response.getPVCurrent();
		timestamp = response.getTimestamp();
		batteryLevel.setLevel(level);
		battery_image.setImageBitmap(batteryLevel.getBitmap());
		Resources res = getResources();
		minChargeLevel.setText(res.getString(R.string.min_charge_label) + ":" + minVal);
		maxChargeLevel.setText(res.getString(R.string.max_charge_label) + ":" + maxVal);
		snapshot_powered.setText(res.getString(R.string.battery_in) + "\n" + res.getString(R.string.battery_out) + "\n"
				+ res.getString(R.string.battery_estimatedtime));
		return res;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_main_device_menu, menu);
		return true;
	}

	/**
	 * Helper function to round of values received from snapshot to 2 decimal
	 * places.
	 */
	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
}

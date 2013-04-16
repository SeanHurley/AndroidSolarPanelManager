package com.example.solarpanelmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.Constants;
import com.example.bluetooth.Callback;
import com.example.bluetooth.LocationUpdateHandler;
import com.example.bluetooth.PINUpdateHandler;
import com.example.bluetooth.SetChargeConstraintsHandler;
import com.example.bluetooth.TimeUpdateHandler;
import com.example.bluetooth.ViewChargeConstraintsHandler;
import com.example.solarpanelmanager.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.ViewChargeConstraintsResponse;

/**
 * @author seanhurley
 * 
 *         This activity will be used to allow the user to set device specific
 *         activities
 */
public class DevicePreferencesActivity extends SherlockActivity {

	private View passRow;
	private View minMaxRow;
	private View timeRow;
	private View locationRow;
	private View activityIndicator;
	private View scroller;
	private int minValue = 0;
	private int maxValue = 100;
	private String oldPassPhrase;
	private String deviceId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_settings);

		getUI();
		getData();
		setupUI();
	}

	private void getUI() {
		activityIndicator = findViewById(R.id.activityIndicator);
		passRow = findViewById(R.id.row_password_pref);
		timeRow = findViewById(R.id.row_time_update);
		locationRow = findViewById(R.id.row_location_update);
		minMaxRow = findViewById(R.id.row_min_max_values);
		scroller = findViewById(R.id.scroll_view);
	}

	private void setupUI() {
		passRow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(DevicePreferencesActivity.this);

				alert.setTitle(R.string.set_pass_phrase_title);
				alert.setMessage(R.string.set_pass_phrase_message);

				// Set an EditText view to get user input
				final EditText input = new EditText(DevicePreferencesActivity.this);
				alert.setView(input);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						final String value = input.getText().toString();
						PINUpdateHandler handler = new PINUpdateHandler(new Callback<BaseResponse>() {

							@Override
							public void onComplete(BaseResponse response) {
								hideLoadingSpinner();
								if (response.getResult() == 200) {
									oldPassPhrase = value;
									PreferenceManager.getDefaultSharedPreferences(DevicePreferencesActivity.this)
											.edit().putString(Constants.PASS_PHRASE_PREFERENCE + deviceId, value)
											.commit();
								} else {
									// TODO Something went wrong, tell the user
									System.out.println("FAILED SETTING NEW PASS");
								}
							}
						}, deviceId, oldPassPhrase, value);
						showLoadingSpinner();
						handler.performAction();

					}
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

				alert.show();
			}
		});

		minMaxRow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// add RangeSeekBar to pre-defined layout
				LayoutInflater inflater = getLayoutInflater();
				View dialoglayout = inflater.inflate(R.layout.dialog_min_max, (ViewGroup) getCurrentFocus());
				LinearLayout seekArea = (LinearLayout) dialoglayout.findViewById(R.id.range_seek_area);
				final TextView minValueText = (TextView) dialoglayout.findViewById(R.id.min_value);
				final TextView maxValueText = (TextView) dialoglayout.findViewById(R.id.max_value);

				final RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0, 100, DevicePreferencesActivity.this);
				minValueText.setText(minValue + "");
				maxValueText.setText(maxValue + "");

				seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
					@Override
					public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
						minValueText.setText(minValue + "");
						maxValueText.setText(maxValue + "");
					}
				});
				seekBar.setNotifyWhileDragging(true);
				seekBar.setSelectedMinValue(minValue);
				seekBar.setSelectedMaxValue(maxValue);

				seekArea.addView(seekBar);
				AlertDialog.Builder builder = new AlertDialog.Builder(DevicePreferencesActivity.this);
				builder.setTitle(R.string.set_min_max_title);
				builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						SetChargeConstraintsHandler handler = new SetChargeConstraintsHandler(
								new Callback<BaseResponse>() {

									@Override
									public void onComplete(BaseResponse response) {
										hideLoadingSpinner();
										if (response.getResult() == 200) {
											minValue = seekBar.getSelectedMinValue();
											maxValue = seekBar.getSelectedMaxValue();
										} else {
											// TODO Tell the user something went
											// wrong
										}
									}
								}, deviceId, oldPassPhrase, seekBar.getSelectedMaxValue(), seekBar
										.getSelectedMinValue());
						showLoadingSpinner();
						handler.performAction();
					}
				});
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
					}
				});
				builder.setView(dialoglayout);
				builder.show();
			}
		});

		timeRow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(DevicePreferencesActivity.this);
				builder.setTitle(R.string.set_time_title);
				builder.setMessage(R.string.set_time_message);
				builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						TimeUpdateHandler handler = new TimeUpdateHandler(new Callback<BaseResponse>() {

							@Override
							public void onComplete(BaseResponse response) {
								hideLoadingSpinner();
								if (response.getResult() != 200) {
									// TODO Tell the user
								}
							}
						}, deviceId, oldPassPhrase, System.currentTimeMillis());
						showLoadingSpinner();
						handler.performAction();
					}
				});
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				builder.create().show();
			}
		});

		locationRow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(DevicePreferencesActivity.this);
				builder.setTitle(R.string.set_location_update_title);
				builder.setMessage(R.string.set_location_update_message);
				builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						LocationUpdateHandler handler = new LocationUpdateHandler(new Callback<BaseResponse>() {

							@Override
							public void onComplete(BaseResponse response) {
								hideLoadingSpinner();
								if (response.getResult() != 200) {
									// TODO Tell the user
								}
							}
							// TODO Get the real values
						}, deviceId, oldPassPhrase, 0, 0);
						showLoadingSpinner();
						handler.performAction();
					}
				});
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				builder.create().show();
			}
		});

		locationRow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void getData() {
		deviceId = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.CURRENT_DEVICE, null);
		oldPassPhrase = PreferenceManager.getDefaultSharedPreferences(this).getString(
				Constants.PASS_PHRASE_PREFERENCE + deviceId, null);
		if (deviceId == null) {
			// TODO - Tell the user that something is wrong
		}

		ViewChargeConstraintsHandler handler = new ViewChargeConstraintsHandler(
				new Callback<ViewChargeConstraintsResponse>() {

					@Override
					public void onComplete(ViewChargeConstraintsResponse response) {
						hideLoadingSpinner();
						if (response.getResult() == 200) {
							minValue = response.getMin();
							maxValue = response.getMax();
						}
					}
				}, deviceId, oldPassPhrase);
		showLoadingSpinner();
		handler.performAction();
	}

	private void showLoadingSpinner() {
		this.activityIndicator.setVisibility(View.VISIBLE);
		this.scroller.setVisibility(View.GONE);
	}

	private void hideLoadingSpinner() {
		this.activityIndicator.setVisibility(View.GONE);
		this.scroller.setVisibility(View.VISIBLE);
	}
}

package com.example.solarpanelmanager;
import com.example.solarpanelmanager.R;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.Constants;
import com.example.bluetooth.BaseResponseHandler;
import com.example.bluetooth.BluetoothScanner;
import com.example.bluetooth.BluetoothScanner.BluetoothDeviceWrapper;
import com.example.bluetooth.Callback;
import com.example.bluetooth.GenericCallback;
import com.example.bluetooth.HandshakeHandler;
import com.example.solarpanelmanager.api.responses.BaseResponse;

public class ConnectActivity extends SherlockActivity {
	private BluetoothScanner scanner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		setupActionBar();

		final ArrayAdapter<BluetoothDeviceWrapper> arrayAdapter = new ArrayAdapter<BluetoothDeviceWrapper>(this,
				android.R.layout.simple_list_item_1);
		ListView deviceList = (ListView) findViewById(R.id.mainListView);
		deviceList.setAdapter(arrayAdapter);
		deviceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				final String device = ((BluetoothDeviceWrapper) parent.getItemAtPosition(position)).address;
				final String deviceName = ((BluetoothDeviceWrapper) parent.getItemAtPosition(position)).name;

				final ProgressDialog dialog = new ProgressDialog(ConnectActivity.this);
				dialog.setTitle(R.string.Loading);
				dialog.setMessage(getString(R.string.Communicating));
				dialog.show();

				String pass = PreferenceManager.getDefaultSharedPreferences(ConnectActivity.this).getString(
						Constants.PASS_PHRASE_PREFERENCE, null);
				BaseResponseHandler handler = new HandshakeHandler(new Callback<BaseResponse>() {

					@Override
					public void onComplete(BaseResponse json) {
						boolean itWorked = true;

						dialog.dismiss();
						if (itWorked) {
							SharedPreferences prefs = PreferenceManager
									.getDefaultSharedPreferences(ConnectActivity.this);
							prefs.edit().putString(Constants.CURRENT_DEVICE, device).commit();
							prefs.edit().putString(Constants.CURRENT_DEVICE_NAME, deviceName).commit();

							Intent intent = new Intent(ConnectActivity.this, BatteryActivity.class);
							startActivity(intent);
							ConnectActivity.this.finish();
						} else {
							new AlertDialog.Builder(ConnectActivity.this).setTitle(R.string.failed_communication)
									.setMessage(getString(R.string.connection_error))
									.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											finish();
										}

									}).show();
						}
					}

				}, device, pass);
				handler.performAction();

			}
		});

		final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			// TODO: handle case of bluetooth not supported
		}

		final View indicator = findViewById(R.id.bluetooth_loading);

		scanner = new BluetoothScanner(this, new Callback<BaseResponse>() {

			@Override
			public void onComplete(BaseResponse json) {
				indicator.setVisibility(View.VISIBLE);
			}
		}, new Callback<BaseResponse>() {

			@Override
			public void onComplete(BaseResponse json) {
				indicator.setVisibility(View.INVISIBLE);
			}
		}, new GenericCallback<BluetoothScanner.BluetoothDeviceWrapper>() {

			@Override
			public void onComplete(BluetoothScanner.BluetoothDeviceWrapper device) {
				arrayAdapter.add(device);
			}
		});

		((Button) findViewById(R.id.button_bluetooth_scan)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (scanner.scan()) {
					arrayAdapter.clear();
				}
			}
		});

		scanner.scan();
	}
	
	private void setupActionBar() {
		String deviceId = PreferenceManager.getDefaultSharedPreferences(ConnectActivity.this).getString(
				Constants.CURRENT_DEVICE, null);
		if (deviceId != null) {	
			ActionBar actionBar = getSupportActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		scanner.destroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		int code = scanner.handleResult(requestCode, resultCode);
		if (code == BluetoothScanner.BLUETOOTH_READY) {
			scanner.scan();
		} else if (code == BluetoothScanner.BLUETOOTH_DISABLED) {
			Toast.makeText(this, R.string.bluetooth_warning, Toast.LENGTH_SHORT).show();
		}
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

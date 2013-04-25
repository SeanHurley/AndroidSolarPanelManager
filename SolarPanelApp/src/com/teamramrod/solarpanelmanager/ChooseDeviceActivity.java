package com.teamramrod.solarpanelmanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Window;
import com.example.solarpanelmanager.R;
import com.teamramrod.Constants;
import com.teamramrod.bluetooth.BaseResponseHandler;
import com.teamramrod.bluetooth.BluetoothScanner;
import com.teamramrod.bluetooth.BluetoothScanner.BluetoothDeviceWrapper;
import com.teamramrod.bluetooth.Callback;
import com.teamramrod.bluetooth.GenericCallback;
import com.teamramrod.bluetooth.HandshakeHandler;
import com.teamramrod.solarpanelmanager.api.responses.BaseResponse;

/**
 * @author mikecandido
 * 
 */
public class ChooseDeviceActivity extends BaseActivity {
	private BluetoothScanner scanner;
	private ArrayAdapter<BluetoothDeviceWrapper> arrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_connect);

		arrayAdapter = new ArrayAdapter<BluetoothDeviceWrapper>(this,
				android.R.layout.simple_list_item_1);
		ListView deviceList = (ListView) findViewById(R.id.mainListView);
		deviceList.setAdapter(arrayAdapter);
		deviceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				final String device = ((BluetoothDeviceWrapper) parent.getItemAtPosition(position)).address;
				final String deviceName = ((BluetoothDeviceWrapper) parent.getItemAtPosition(position)).name;

				final ProgressDialog dialog = new ProgressDialog(ChooseDeviceActivity.this);
				dialog.setTitle(R.string.Loading);
				dialog.setMessage(getString(R.string.Communicating));
				dialog.show();

				String pass = PreferenceManager.getDefaultSharedPreferences(ChooseDeviceActivity.this).getString(
						Constants.PASS_PHRASE_PREFERENCE, null);
				BaseResponseHandler handler = new HandshakeHandler(new Callback<BaseResponse>() {

					@Override
					public void onComplete(BaseResponse json) {
						boolean itWorked = true;

						dialog.dismiss();
						if (itWorked) {
							SharedPreferences prefs = PreferenceManager
									.getDefaultSharedPreferences(ChooseDeviceActivity.this);
							prefs.edit().putString(Constants.CURRENT_DEVICE, device).commit();
							prefs.edit().putString(Constants.CURRENT_DEVICE_NAME, deviceName).commit();

							Intent intent = new Intent(ChooseDeviceActivity.this, MainDeviceActivity.class);
							startActivity(intent);
							ChooseDeviceActivity.this.finish();
						} else {
							new AlertDialog.Builder(ChooseDeviceActivity.this).setTitle(R.string.failed_communication)
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
				scan();
//				indicator.setVisibility(View.VISIBLE);
			}
		}, new Callback<BaseResponse>() {

			@Override
			public void onComplete(BaseResponse json) {
				done();
//				indicator.setVisibility(View.INVISIBLE);
			}
		}, new GenericCallback<BluetoothScanner.BluetoothDeviceWrapper>() {

			@Override
			public void onComplete(BluetoothScanner.BluetoothDeviceWrapper device) {
				update(device);
//				arrayAdapter.add(device);
			}
		});

//		((Button) findViewById(R.id.button_bluetooth_scan)).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				if (scanner.scan()) {
//					arrayAdapter.clear();
//				}
//			}
//		});

		scanner.scan();
	}
	
	private void scan() {
		setProgressBarIndeterminateVisibility(Boolean.TRUE);
		findViewById(R.id.bluetooth_loading).setVisibility(View.VISIBLE);
	}
	
	private void done() {
		setProgressBarIndeterminateVisibility(Boolean.FALSE);
		findViewById(R.id.bluetooth_loading).setVisibility(View.INVISIBLE);
	}
	
	private void update(BluetoothScanner.BluetoothDeviceWrapper device) {
		arrayAdapter.add(device);
	}

	@Override
	protected void setupActionBar() {
		String deviceId = PreferenceManager.getDefaultSharedPreferences(ChooseDeviceActivity.this).getString(
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		
		String deviceId = PreferenceManager.getDefaultSharedPreferences(ChooseDeviceActivity.this).getString(
				Constants.CURRENT_DEVICE, null);
		
		if (deviceId != null) {
			inflater.inflate(R.menu.activity_choose_device_menu, menu);
		} else {
			inflater.inflate(R.menu.activity_choose_device_menu_first, menu);
		}
		
		return true;
	}
	
	@Override
	protected void refresh() {
		if (scanner.scan()) {
			arrayAdapter.clear();
		}
	}
}


package com.teamramrod.solarpanelmanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.example.solarpanelmanager.R;
import com.teamramrod.Constants;
import com.teamramrod.bluetooth.BluetoothScanner;
import com.teamramrod.bluetooth.Callback;
import com.teamramrod.bluetooth.HandshakeHandler;
import com.teamramrod.solarpanelmanager.api.responses.BaseResponse;

/**
 * Activity for choosing the controller device to work with and making the
 * initial bluetooth connection.
 * 
 * @author mikecandido
 */
public class ChooseDeviceActivity extends BaseActivity {
	private BluetoothScanner scanner;
	private ArrayAdapter<BluetoothDeviceWrapper> arrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);

		arrayAdapter = new ArrayAdapter<BluetoothDeviceWrapper>(this,
				android.R.layout.simple_list_item_1);
		ListView deviceList = (ListView) findViewById(R.id.mainListView);
		deviceList.setAdapter(arrayAdapter);
		deviceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				BluetoothDeviceWrapper item = ((BluetoothDeviceWrapper) parent.getItemAtPosition(position));
				negotiateDevice(item.address, item.name);
			}
		});

		setupScanner(findViewById(R.id.bluetooth_loading));
		scanner.scan();
	}
	
	/**
	 * Attempt to connect to a device and determine if it is a compatible
	 * solar panel controller.
	 * 
	 * @param deviceAddress   bluetooth address of device
	 * @param deviceName      bluetooth network name of device
	 */
	private void negotiateDevice(final String deviceAddress, final String deviceName) {
		final ProgressDialog dialog = new ProgressDialog(ChooseDeviceActivity.this);
		dialog.setTitle(R.string.Loading);
		dialog.setMessage(getString(R.string.Communicating));
		dialog.show();

		String pass = PreferenceManager.getDefaultSharedPreferences(ChooseDeviceActivity.this).getString(
				Constants.PASS_PHRASE_PREFERENCE, null);
		(new HandshakeHandler(new Callback<BaseResponse>() {

			@Override
			public void onComplete(BaseResponse response) {
				dialog.dismiss();
				if (response.getResult() == 200) {
					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(ChooseDeviceActivity.this);
					prefs.edit().putString(Constants.CURRENT_DEVICE, deviceAddress).commit();
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

		}, deviceAddress, pass)).performAction();
	}
	
	/**
	 * Initialize the BluetoothScanner that will be used to find potential
	 * devices to connect to.
	 * 
	 * @param indicator   a view to toggle visibility of when loading
	 */
	private void setupScanner(final View indicator) {
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
		}, new BluetoothScanner.ScannerCallback() {

			@Override
			public void onComplete(String address, String name, boolean isBonded) {
				arrayAdapter.add(new BluetoothDeviceWrapper(address, name, isBonded));
			}
		});
	}
	
	@Override
	protected void refresh() {
		if(scanner.scan()) {
			arrayAdapter.clear();
		}
	}

	@Override
	protected void setupActionBar() {
		ActionBar actionBar = getSupportActionBar();
		String deviceId = PreferenceManager.getDefaultSharedPreferences(ChooseDeviceActivity.this).getString(
				Constants.CURRENT_DEVICE, null);
		if (deviceId != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		actionBar.setTitle(R.string.menu_activity_choose_device_title);
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
	
	/**
	 * Wraps information about a BluetoothDevice for user friendly display
	 * in an Android ArrayAdapter.
	 */
	private class BluetoothDeviceWrapper {
		public final String address;
		public final String name;
		public boolean isBonded;

		public BluetoothDeviceWrapper(String address, String name, boolean isBonded) {
			this.address = address;
			this.name = name;
			this.isBonded = isBonded;
		}

		@Override
		public String toString() {
			String bonded = isBonded ? " (paired)" : "";
			return String.format("%s\n%s%s", name, address, bonded);
		}
	}
}
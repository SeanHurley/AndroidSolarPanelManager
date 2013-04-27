package com.teamramrod.bluetooth;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.teamramrod.solarpanelmanager.api.responses.BaseResponse;

/**
 * Interfaces with the bluetooth hardware to obtain the list of paired
 * bluetooth devices and scan for nearby devices.
 * 
 * @author Michael Candido
 */
public class BluetoothScanner {

	public final static int NOT_HANDLED = 0;
	public final static int BLUETOOTH_READY = 1;
	public final static int BLUETOOTH_DISABLED = 2;
	
	private static final int REQUEST_ENABLE_BT = 242423423;

	private BroadcastReceiver receiver;
	private Activity context;
	private Callback<BaseResponse> doneCallback;
	private Callback<BaseResponse> startCallback;
	private ScannerCallback updateCallback;

	private Set<String> added = new HashSet<String>();
	private boolean isRegistered = false;

	/**
	 * Create the BluetoothScanner.
	 * 
	 * @param context            Context (usually an Activity) that the
	 *                           scanner will be working with
	 * @param startCallback      hook for when the scanner begins scanning
	 * @param doneCallback       hook for when the scanner finishes scanning
	 * @param updateCallback     hook for when the scanner finds a device
	 */
	public BluetoothScanner(Activity context, Callback<BaseResponse> startCallback,
			Callback<BaseResponse> doneCallback, ScannerCallback updateCallback) {
		this.context = context;
		this.doneCallback = doneCallback;
		this.startCallback = startCallback;
		this.updateCallback = updateCallback;
	}

	/**
	 * Begin scanning for devices. Will fail if a scan is already in progress
	 * or if bluetooth is disabled.
	 * 
	 * @return   true if the scan was started, false otherwise
	 */
	public boolean scan() {
		register();
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter.isEnabled()) {
			if (!adapter.isDiscovering()) {
				adapter.startDiscovery();
				return false;
			} else {
				added.clear();
				addBonded();
				return true;
			}
		} else {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			return false;
		}
	}

	/**
	 * Handles bluetooth-related results from onActivityResult. Activities
	 * using BluetoothScanner are responsible for passing unhandled results
	 * to this method.
	 * 
	 * @param requestCode   the request code
	 * @param resultCode    the result code
	 * @return              a constant communicating the state of the
	 *                      bluetooth interface
	 */
	public int handleResult(int requestCode, int resultCode) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				return BLUETOOTH_READY;
			} else {
				return BLUETOOTH_DISABLED;
			}
		}
		return NOT_HANDLED;
	}

	/**
	 * Clean up by unregistering this scanner's receiver.
	 */
	public void destroy() {
		context.unregisterReceiver(receiver);
	}
	
	/**
	 * Initialize by the scanner to registering a receiver to receive
	 * bluetooth events.
	 */
	private void register() {
		if (isRegistered)
			return;
		
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
					startCallback.onComplete(null);
				} else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
					doneCallback.onComplete(null);
				} else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (!added.contains(device.getAddress())) {
						updateCallback.onComplete(device.getAddress(), device.getName(), device.getBondState() == BluetoothDevice.BOND_BONDED);
						added.add(device.getAddress());
					}
				}
			}
		};

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		context.registerReceiver(receiver, filter);
		
		isRegistered = true;
	}

	/**
	 * Fetch the paired bluetooth devices and track them so they are not
	 * double added during a scan.
	 */
	private void addBonded() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		for (BluetoothDevice d : adapter.getBondedDevices()) {
			if (!added.contains(d.getAddress())) {
				updateCallback.onComplete(d.getAddress(), d.getName(), d.getBondState() == BluetoothDevice.BOND_BONDED);
				added.add(d.getAddress());
			}
		}
	}
	
	/**
	 * Three argument callback for bluetooth device information.
	 */
	public interface ScannerCallback {
		public void onComplete(String address, String name, boolean isBonded);
	}
}

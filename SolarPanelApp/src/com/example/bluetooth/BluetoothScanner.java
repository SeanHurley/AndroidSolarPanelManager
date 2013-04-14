package com.example.bluetooth;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.solarpanelmanager.api.responses.BaseResponse;

public class BluetoothScanner {

	public class BluetoothDeviceWrapper {
		private final BluetoothDevice device;
		public final String address;
		public final String name;

		public BluetoothDeviceWrapper(BluetoothDevice d) {
			this.device = d;
			this.address = device.getAddress();
			this.name = device.getName();
		}

		@Override
		public String toString() {
			String bonded = device.getBondState() == BluetoothDevice.BOND_BONDED ? " (paired)" : "";
			return String.format("%s\n%s%s", device.getName(), device.getAddress(), bonded);
		}
	}

	public final static int NOT_HANDLED = 0;
	public final static int BLUETOOTH_READY = 1;
	public final static int BLUETOOTH_DISABLED = 2;

	private BroadcastReceiver mReceiver;
	private Activity context;
	private Callback<BaseResponse> doneCallback;
	private Callback<BaseResponse> startCallback;
	private GenericCallback<BluetoothDeviceWrapper> updateCallback;

	Set<String> added = new HashSet<String>();

	public BluetoothScanner(Activity context, Callback<BaseResponse> startCallback,
			Callback<BaseResponse> doneCallback, GenericCallback<BluetoothDeviceWrapper> updateCallback) {
		this.context = context;
		this.doneCallback = doneCallback;
		this.startCallback = startCallback;
		this.updateCallback = updateCallback;
	}

	private static final int REQUEST_ENABLE_BT = 242423423;

	public void register() {
		mReceiver = new BroadcastReceiver() {

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
						updateCallback.onComplete(new BluetoothDeviceWrapper(device));
						added.add(device.getAddress());
					}
				}
			}
		};

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		context.registerReceiver(mReceiver, filter);
	}

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

	public void destroy() {
		context.unregisterReceiver(mReceiver);
	}

	private void addBonded() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		for (BluetoothDevice d : adapter.getBondedDevices()) {
			if (!added.contains(d.getAddress())) {
				updateCallback.onComplete(new BluetoothDeviceWrapper(d));
				added.add(d.getAddress());
			}
		}
	}
}

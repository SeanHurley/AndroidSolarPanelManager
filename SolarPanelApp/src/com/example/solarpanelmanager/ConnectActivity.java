package com.example.solarpanelmanager;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ConnectActivity extends Activity {
	
	private static final UUID uuid = UUID.fromString("87e65bc0-89d0-11e2-9e96-0800200c9a66");
	
	private class BluetoothDeviceWrapper {
		public final BluetoothDevice device;
		
		public BluetoothDeviceWrapper(BluetoothDevice d) {
			this.device = d;
		}
		
		@Override
		public String toString() {
			String bonded = device.getBondState() == BluetoothDevice.BOND_BONDED ? " (paired)" : "";
			return String.format("%s\n%s%s", device.getName(), device.getAddress(), bonded);
		}
	}
	
	private static final int REQUEST_ENABLE_BT = 242423423;
	
	private BroadcastReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		final ArrayAdapter<BluetoothDeviceWrapper> arrayAdapter = new ArrayAdapter<BluetoothDeviceWrapper>(this, android.R.layout.simple_list_item_1);
		final Set<String> added = new HashSet<String>();
		ListView deviceList = (ListView) findViewById(R.id.mainListView);
		deviceList.setAdapter(arrayAdapter);
		deviceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				BluetoothDevice device = ((BluetoothDeviceWrapper) parent.getItemAtPosition(position)).device;
				
				ProgressDialog dialog = new ProgressDialog(ConnectActivity.this);
				dialog.setTitle("Loading");
				dialog.setMessage("Communicating with device");
				dialog.show();
				
				try {
					BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
					BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
					socket.connect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO: actually communicate with device
				boolean itWorked = true;
				
				dialog.dismiss();
				if (itWorked) {
					// TODO: change state to connected
					ConnectActivity.this.finish();					
				} else {
					new AlertDialog.Builder(ConnectActivity.this)
						.setTitle("Communication Failed")
						.setMessage("Could not connect to this device")
						.setNeutralButton("ok", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								finish();
							}
							
						})
						.show();
				}
			}
		});
		
		final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			// TODO: handle case of bluetooth not supported
		}
		
		mReceiver = new BroadcastReceiver() {
			View indicator = findViewById(R.id.bluetooth_loading);
			
		    public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();
		        if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
		        	indicator.setVisibility(View.VISIBLE);
		        } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
		        	indicator.setVisibility(View.INVISIBLE);
		        } else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
		            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		            if (!added.contains(device.getAddress())) {
		            	arrayAdapter.add(new BluetoothDeviceWrapper(device));
		            	added.add(device.getAddress());
		            }
		        }
		    }
		};
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, filter);
		
		((Button) findViewById(R.id.button_bluetooth_scan)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (adapter.isDiscovering()) return;
				arrayAdapter.clear();
				added.clear();
				addBonded(arrayAdapter, added);
				scan();				
			}
		});		
		
		addBonded(arrayAdapter, added);
		scan();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mReceiver != null)
			unregisterReceiver(mReceiver);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK) {
				scan();
			} else {
				Toast.makeText(this, "bluetooth not enabled; cannot continue", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void addBonded(ArrayAdapter<BluetoothDeviceWrapper> arrayAdapter, Set<String> added) {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		for (BluetoothDevice d : adapter.getBondedDevices()) {
			if (!added.contains(d.getAddress())) {
				arrayAdapter.add(new BluetoothDeviceWrapper(d));
				added.add(d.getAddress());
			}
		}
	}
	
	private void scan() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter.isEnabled()) {
			if (!adapter.isDiscovering()) {
				adapter.startDiscovery();
			}
		} else {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
	}
}

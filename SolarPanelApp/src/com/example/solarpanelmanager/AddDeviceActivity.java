package com.example.solarpanelmanager;

import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.bluetooth.GetBluetoothDevices;
import com.example.bluetooth.GetBluetoothDevices.PairedDevicesCallback;

public class AddDeviceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_device);
		
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("Loading");
		dialog.setMessage("Fetching bluetooth devices");
		dialog.show();
		
		final ListView deviceList = (ListView) findViewById(R.id.mainListView);
		deviceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				String[] entry = ((String) parent.getItemAtPosition(position)).split("\n");
				String name = entry[0], address = entry[1];
				
				ProgressDialog dialog = new ProgressDialog(AddDeviceActivity.this);
				dialog.setTitle("Loading");
				dialog.setMessage("Communicating with device");
				dialog.show();
				
				// TODO: actually do it
				boolean itWorked = true;
				
				dialog.dismiss();
				if (itWorked) {
					// store known device
					AddDeviceActivity.this.finish();					
				} else {
					new AlertDialog.Builder(AddDeviceActivity.this)
						.setTitle("Communication Failed")
						.setMessage("Could not add this device")
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
		
		GetBluetoothDevices task = new GetBluetoothDevices(new PairedDevicesCallback() {
			
			@Override
			public void foundPairedDevices(Set<BluetoothDevice> devices) {
				String[] values = new String[devices.size()];
				int i = 0;
			    for (BluetoothDevice d : devices) {
			    	values[i++] = String.format("%s\n%s", d.getName(), d.getAddress());
			    }
			    
			    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddDeviceActivity.this,
			            android.R.layout.simple_list_item_1, values);
			    deviceList.setAdapter(adapter);
			    
			    dialog.dismiss();
			}
			
		});
		task.performAction();

		((Button) findViewById(R.id.button_bluetooth_settings)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));				
			}
		});
	}
	
}

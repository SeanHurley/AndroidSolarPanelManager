package com.example.solarpanelmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bluetooth.BluetoothScanner;
import com.example.bluetooth.BluetoothScanner.BluetoothDeviceWrapper;
import com.example.bluetooth.Callback;
import com.example.bluetooth.CommunicationHandler;
import com.example.bluetooth.GenericCallback;
import com.example.bluetooth.HandshakeHandler;
import com.example.solarpanelmanager.api.responses.BaseResponse;

public class ConnectActivity extends Activity {
	
//	private static final UUID uuid = UUID.fromString("87e65bc0-89d0-11e2-9e96-0800200c9a66");
	
	private BluetoothScanner scanner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		final ArrayAdapter<BluetoothDeviceWrapper> arrayAdapter = new ArrayAdapter<BluetoothDeviceWrapper>(this, android.R.layout.simple_list_item_1);
		ListView deviceList = (ListView) findViewById(R.id.mainListView);
		deviceList.setAdapter(arrayAdapter);
		deviceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				String device = ((BluetoothDeviceWrapper) parent.getItemAtPosition(position)).address;
				
				final ProgressDialog dialog = new ProgressDialog(ConnectActivity.this);
				dialog.setTitle("Loading");
				dialog.setMessage("Communicating with device");
				dialog.show();
				
				CommunicationHandler handler = new HandshakeHandler(new Callback() {

					@Override
					public void onComplete(BaseResponse json) {
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
					
				}, device);
				handler.performAction();
				
			}
		});
		
		final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			// TODO: handle case of bluetooth not supported
		}

		final View indicator = findViewById(R.id.bluetooth_loading);
		
		scanner = new BluetoothScanner(this, new Callback() {

			@Override
			public void onComplete(BaseResponse json) {
	        	indicator.setVisibility(View.VISIBLE);
			}
		}, new Callback() {
			
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
				if (scanner.scan())
					arrayAdapter.clear();
			}
		});		
		
		scanner.scan();
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
		} else if (code == BluetoothScanner.BLUETOOTH_DISABLED)  {
			Toast.makeText(this, "bluetooth not enabled; cannot continue", Toast.LENGTH_SHORT).show();
		}
	}

}

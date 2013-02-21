package com.example.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

public abstract class BaseBluetoothCaller {

	private Callback responseCallback;

	public BaseBluetoothCaller(Callback callback) {
		this.responseCallback = callback;
	}

	public void performAction() {
		ServiceASyncTask task = new ServiceASyncTask();
		task.execute();
	}

	abstract protected String getRequest();

	public class ServiceASyncTask extends AsyncTask<Void, Void, JSONObject> {

		private BluetoothAdapter mBluetoothAdapter;

		public ServiceASyncTask() {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		}

		@Override
		protected JSONObject doInBackground(Void... args) {

			// Fix the newline ending?
			String request = getRequest() + "\n";
			System.out.println("Bluetooth call");
			BluetoothSocket clientSocket = null;
			// Client knows the MAC address of server
			BluetoothDevice mmDevice = mBluetoothAdapter.getRemoteDevice("14:10:9F:E7:CA:93");
			String data = "";

			try {
				// The app's UUID string, also used by the server code
				clientSocket = mmDevice.createRfcommSocketToServiceRecord(UUID
						.fromString("00001101-0000-1000-8000-00805F9B34FB"));

				mBluetoothAdapter.cancelDiscovery(); // Cancel, discovery slows
														// connection
				clientSocket.connect();
				DataInputStream in = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

				out.write(request.getBytes());
				out.flush();

				byte b;
				while (((b = in.readByte()) > 0) && (b != 0x0a)) {
					data += (char) b;
				}
				System.out.println(data);
				System.out.println("Bluetooth end");
			} catch (Exception e) {
			}

			return (JSONObject) JSONValue.parse(data);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			responseCallback.onComplete(result);
		}

	}

}

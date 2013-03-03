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

/**
 * @author seanhurley
 * 
 *         This is the base communication class which will manage the
 *         communication with the actual bluetooth device. This way, children
 *         only need to implement a little bit of the functionality to tell the
 *         bluetooth device which request it's sending in order to get all of
 *         the functionality.
 */
public abstract class CommunicationHandler {
	private Callback responseCallback;

	/**
	 * @return A JSON formatted string which will tell the bluetooth device
	 *         which type of request this is.
	 */
	abstract protected String getRequest();

	public CommunicationHandler(Callback callback) {
		this.responseCallback = callback;
	}

	/**
	 * Will actually start the asynchronous communication and will call the
	 * callback when it is finished.
	 */
	public void performAction() {
		ServiceASyncTask task = new ServiceASyncTask();
		task.execute();
	}

	/**
	 * @author seanhurley This will actually serve as the asynchronous method by
	 *         which we communicate with the device.
	 */
	private class ServiceASyncTask extends AsyncTask<Void, Void, JSONObject> {

		private BluetoothAdapter mBluetoothAdapter;

		public ServiceASyncTask() {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		}

		@Override
		protected JSONObject doInBackground(Void... args) {

			// TODO Fix the newline ending?
			String request = getRequest() + "\n";
			BluetoothSocket clientSocket = null;

			if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
				// Bluetooth is not enabled
				return null;
			}

			// TODO Client knows the MAC address of server
			// This should be passed along from the app to give them the which
			// device address to talk to
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

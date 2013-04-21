package com.teamramrod.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.minidev.json.JSONObject;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import com.teamramrod.solarpanelmanager.api.parsers.MessageKeys;
import com.teamramrod.solarpanelmanager.api.responses.BaseResponse;

/**
 * @author seanhurley
 * 
 *         This is the base communication class which will manage the
 *         communication with the actual bluetooth device. This way, children
 *         only need to implement a little bit of the functionality to tell the
 *         bluetooth device which request it's sending in order to get all of
 *         the functionality.
 */
public abstract class CommunicationHandler<T extends BaseResponse> {
	private Callback<T> responseCallback;
	private String address;
	private String pass;
	private ServiceASyncTask task;

	/**
	 * @return A JSONObject which will tell the bluetooth device which type of
	 *         request this is.
	 */
	abstract protected JSONObject getRequest();

	/**
	 * @param data
	 *            - The raw string data which is received from the solar panel
	 * @return The T which is the appropriate type of response for this handler
	 */
	abstract protected T parseResponse(String data);

	/**
	 * @param callback
	 *            The Callback which will be called back in the main thread
	 *            after the communication is complete
	 * @param target
	 *            This is the MAC address of the BT device you would like to
	 *            commnuicate with
	 * @param pass
	 *            The passphrase that has been set for the device
	 */
	public CommunicationHandler(Callback<T> callback, String target, String pass) {
		this.address = target;
		this.pass = pass;
		this.responseCallback = callback;
	}

	/**
	 * Will actually start the asynchronous communication and will call the
	 * callback when it is finished.
	 */
	public void performAction() {
		task = new ServiceASyncTask();
		task.execute();
	}

	/**
	 * @param millis
	 *            - How long to wait (in milliseconds) before continuing
	 */
	public void waitOnTask(long millis) {
		try {
			task.get(millis, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author seanhurley This will actually serve as the asynchronous method by
	 *         which we communicate with the device.
	 */
	private class ServiceASyncTask extends AsyncTask<Void, Void, T> {

		private BluetoothAdapter mBluetoothAdapter;

		public ServiceASyncTask() {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		}

		@Override
		protected T doInBackground(Void... args) {

			// TODO Fix the newline ending?
			JSONObject json = getRequest();
			if (pass == null) {
				json.put(MessageKeys.PIN_PASSWORD, "");
			} else {
				json.put(MessageKeys.PIN_PASSWORD, pass);
			}
			String request = json + "\n";
			BluetoothSocket clientSocket = null;

			if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
				// Bluetooth is not enabled
				return null;
			}

			BluetoothDevice mmDevice = mBluetoothAdapter.getRemoteDevice(address);
			String data = "";
			try {

				// The app's UUID string, also used by the server code
				clientSocket = mmDevice.createRfcommSocketToServiceRecord(UUID
						.fromString("00001101-0000-1000-8000-00805F9B34FB"));

				mBluetoothAdapter.cancelDiscovery(); // Cancel, discovery slows
														// connection

				try {
					clientSocket.connect();
				} catch (IOException e) {
					// sad little hack from here:
					// http://stackoverflow.com/questions/3397071/service-discovery-failed-exception-using-bluetooth-on-android
					Method m = mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
			        clientSocket = (BluetoothSocket) m.invoke(mmDevice, 1);
			        clientSocket.connect();
				}

				DataInputStream in = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

				out.write(request.getBytes());
				out.flush();

				byte b;
				while (((b = in.readByte()) > 0) && (b != 0x0a)) {
					data += (char) b;
				}

				in.close();
				out.close();
				clientSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return parseResponse(data);
		}

		@Override
		protected void onPostExecute(T result) {
			responseCallback.onComplete(result);
		}

	}

}

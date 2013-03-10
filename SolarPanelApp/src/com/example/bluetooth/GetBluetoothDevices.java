package com.example.bluetooth;

import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

/**
 * @author seanhurley
 * 
 *         This is the class used to see what devices are paired with this one.
 */
public class GetBluetoothDevices {

	private PairedDevicesCallback responseCallback;

	public GetBluetoothDevices(PairedDevicesCallback callback) {
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

	public interface PairedDevicesCallback {
		public void foundPairedDevices(Set<BluetoothDevice> devices);
	}

	/**
	 * @author seanhurley This will actually serve as the asynchronous method by
	 *         which we communicate to find devices.
	 */
	private class ServiceASyncTask extends AsyncTask<Void, Void, Set<BluetoothDevice>> {

		@Override
		protected Set<BluetoothDevice> doInBackground(Void... args) {
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

			return pairedDevices;
		}

		@Override
		protected void onPostExecute(Set<BluetoothDevice> devices) {
			responseCallback.foundPairedDevices(devices);
		}

	}

}

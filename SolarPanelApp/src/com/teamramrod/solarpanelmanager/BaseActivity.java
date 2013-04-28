package com.teamramrod.solarpanelmanager;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.solarpanelmanager.R;
import com.teamramrod.bluetooth.BluetoothScanner;

/**
 * @author tim
 * 
 */
public class BaseActivity extends SherlockActivity {
	private boolean forbiddenDialogShowing = false;
	private boolean noDeviceDialogShowing = false;
	protected ActionBar actionBar;
	private static final int REQUEST_ENABLE_BT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupActionBar();
	}

	@Override
	protected void onResume() {
		super.onResume();
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			showNoBluetoothAvailable();
		} else {
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				forbiddenDialogShowing = true;
				noDeviceDialogShowing = true;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == BluetoothScanner.BLUETOOTH_READY) {
			forbiddenDialogShowing = false;
			noDeviceDialogShowing = false;
		} else if (resultCode == BluetoothScanner.BLUETOOTH_DISABLED) {
			forbiddenDialogShowing = false;
			noDeviceDialogShowing = false;
		}
	}

	/**
	 * Setup the action bar for the activity.
	 * Base functionality enables the up functionality.
	 */
	protected void setupActionBar() {
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * This can be called in order to show a dialog that tells the user that
	 * there was a security permission issue with the device
	 */
	protected void showForbiddenErrorDialog() {
		if (!forbiddenDialogShowing) {
			forbiddenDialogShowing = true;

			DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					forbiddenDialogShowing = false;
				}
			};
			showDialog(R.string.forbidden_error_title,
					R.string.forbidden_error, okListener, okListener);
		}
	}

	/**
	 * This can be called in order to show a dialog that tells the user that
	 * there was an error reaching the device and suggests possible reasons
	 */
	protected void showDeviceNotAvailable() {
		if (!noDeviceDialogShowing) {
			noDeviceDialogShowing = true;

			DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					noDeviceDialogShowing = false;
				}
			};
			showDialogOkOnly(R.string.device_connection_error_title,
					R.string.device_connection_error, okListener, null);
		}
	}

	/**
	 * Displays dialog box saying bluetooth is not available.
	 */
	protected void showNoBluetoothAvailable() {
		DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				noDeviceDialogShowing = false;
			}
		};
		// TODO Get rid of cancel and ok
		showDialogOkOnly(R.string.bluetooth_not_available,
				R.string.bluetooth_not_available_message, okListener, null);
	}

	/**
	 * Wrapper to show a dialog which has a custom view, with no special
	 * negative listener
	 * 
	 * @param titleId
	 *            The resource id which will be the title of the dialog to show
	 * @param messageId
	 *            The resource id which will be the central message of the
	 *            dialog to show
	 * @param positiveListener
	 *            This will be called when the user selects that "ok" option
	 *            from the dialog
	 * @param customView
	 *            This can be used if you want to show a custom view (For
	 *            example a text input). Can be left null and instead it will
	 *            just be a default dialog
	 */
	protected void showDialogNoNegative(int titleId, int messageId,
			DialogInterface.OnClickListener positiveListener, View customView) {
		showDialog(titleId, messageId, positiveListener, defaultCancel,
				customView);
	}

	/**
	 * Wrapper to show a dialog that has a default view and cancel listener, but
	 * does have a special positive listener
	 * 
	 * @param titleId
	 *            The resource id which will be the title of the dialog to show
	 * @param messageId
	 *            The resource id which will be the central message of the
	 *            dialog to show
	 * @param positiveListener
	 *            This will be called when the user selects that "ok" option
	 *            from the dialog
	 */
	protected void showDialog(int titleId, int messageId,
			DialogInterface.OnClickListener positiveListener) {
		showDialog(titleId, messageId, positiveListener, defaultCancel, null);
	}

	/**
	 * Wrapper to show a dialog with a default view but has special listeners
	 * for both positive and negative
	 * 
	 * @param titleId
	 *            The resource id which will be the title of the dialog to show
	 * @param messageId
	 *            The resource id which will be the central message of the
	 *            dialog to show
	 * @param positiveListener
	 *            This will be called when the user selects that "ok" option
	 *            from the dialog
	 * @param cancelListener
	 *            This will be called when the user selects that "cancel" option
	 *            from the dialog
	 */
	protected void showDialog(int titleId, int messageId,
			DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener cancelListener) {
		showDialog(titleId, messageId, positiveListener, cancelListener, null);
	}

	/**
	 * Can be called to show a customized dialog with features required for the
	 * specific location
	 * 
	 * @param titleId
	 *            The resource id which will be the title of the dialog to show
	 * @param messageId
	 *            The resource id which will be the central message of the
	 *            dialog to show
	 * @param positiveListener
	 *            This will be called when the user selects that "ok" option
	 *            from the dialog
	 * @param cancelListener
	 *            This will be called when the user selects that "cancel" option
	 *            from the dialog
	 * @param customView
	 *            This can be used if you want to show a custom view (For
	 *            example a text input). Can be left null and instead it will
	 *            just be a default dialog
	 */
	protected void showDialog(int titleId, int messageId,
			DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener cancelListener, View customView) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(titleId);
		alert.setMessage(messageId);
		if (customView != null) {
			alert.setView(customView);
		}
		alert.setPositiveButton(R.string.ok, positiveListener);
		alert.setNegativeButton(R.string.cancel, cancelListener);
		alert.show();
	}

	/**
	 * Generates a dialog box with only an OK button
	 * 
	 * @param titleId
	 *            The resource id which will be the title of the dialog to show
	 * @param messageId
	 *            The resource id which will be the central message of the
	 *            dialog to show
	 * @param positiveListener
	 *            This will be called when the user selects that "ok" option
	 *            from the dialog
	 * @param customView
	 *            This can be used if you want to show a custom view (For
	 *            example a text input). Can be left null and instead it will
	 *            just be a default dialog
	 */
	protected void showDialogOkOnly(int titleId, int messageId,
			DialogInterface.OnClickListener positiveListener, View customView) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(titleId);
		alert.setMessage(messageId);
		if (customView != null) {
			alert.setView(customView);
		}
		alert.setPositiveButton(R.string.ok, positiveListener);
		alert.show();
	}

	/*
	 * This is a listener that has the default (no behavior) listener for the
	 * dialogs
	 */
	private DialogInterface.OnClickListener defaultCancel = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// Do Nothing
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			refresh();
			return true;
		case R.id.menu_home:
			return changeActivity(MainDeviceActivity.class);
		case R.id.menu_history:
			return changeActivity(HistoryGraphActivity.class);
		case R.id.menu_schedule:
			return changeActivity(CalendarActivity.class);
		case R.id.menu_device_settings:
			return changeActivity(DevicePreferencesActivity.class);
		case R.id.menu_change_device:
			return changeActivity(ChooseDeviceActivity.class);
		case R.id.menu_settings:
			return changeActivity(ApplicationPreferencesActivity.class);
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Changes the activity to the one specified
	 * 
	 * @param c
	 *            The class of the activity to start
	 * @return Returns true to inform the system to not automatically handle the
	 *         change
	 */
	private boolean changeActivity(Class c) {
		Intent intent = new Intent(this, c);
		startActivity(intent);
		return true;
	}

	/**
	 * Empty function to allow refresh behavior. Override for activities that
	 * require refresh functionality.
	 */
	protected void refresh() {
	}
}

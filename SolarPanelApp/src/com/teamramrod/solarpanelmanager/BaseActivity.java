package com.teamramrod.solarpanelmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.solarpanelmanager.R;

public class BaseActivity extends SherlockActivity {
	private boolean forbiddenDialogShowing = false;
	private boolean noDeviceDialogShowing = false;

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
			showDialog(R.string.forbidden_error_title, R.string.forbidden_error, okListener, okListener);
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
			showDialog(R.string.device_connection_error_title, R.string.device_connection_error, okListener, okListener);
		}

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
	protected void showDialogNoNegative(int titleId, int messageId, DialogInterface.OnClickListener positiveListener,
			View customView) {
		showDialog(titleId, messageId, positiveListener, defaultCancel, customView);
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
	protected void showDialog(int titleId, int messageId, DialogInterface.OnClickListener positiveListener) {
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
	protected void showDialog(int titleId, int messageId, DialogInterface.OnClickListener positiveListener,
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
	protected void showDialog(int titleId, int messageId, DialogInterface.OnClickListener positiveListener,
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
}

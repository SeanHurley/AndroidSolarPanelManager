package com.teamramrod.solarpanelmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.solarpanelmanager.R;

public class BaseActivity extends SherlockActivity {
	private boolean forbiddenDialogShowing = false;
	private boolean noDeviceDialogShowing = false;

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

	protected void showDialogNoNegative(int titleId, int messageId, DialogInterface.OnClickListener positiveListener,
			View customView) {
		showDialog(titleId, messageId, positiveListener, defaultCancel, customView);
	}

	protected void showDialog(int titleId, int messageId, DialogInterface.OnClickListener positiveListener) {
		showDialog(titleId, messageId, positiveListener, defaultCancel, null);
	}

	protected void showDialog(int titleId, int messageId, DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener cancelListener) {
		showDialog(titleId, messageId, positiveListener, cancelListener, null);
	}

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

	private DialogInterface.OnClickListener defaultCancel = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// Do Nothing
		}
	};
}

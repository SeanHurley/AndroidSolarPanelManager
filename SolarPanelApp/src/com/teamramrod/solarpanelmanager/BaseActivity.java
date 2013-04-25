package com.teamramrod.solarpanelmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.solarpanelmanager.R;

public class BaseActivity extends SherlockActivity {
	private boolean forbiddenDialogShowing = false;
	private boolean noDeviceDialogShowing = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupActionBar();
	}
	
	protected void setupActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

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
	
	private boolean changeActivity(Class c) {
		Intent intent = new Intent(this, c);
		startActivity(intent);
		return true;
	}
	
	protected void refresh() {}
}

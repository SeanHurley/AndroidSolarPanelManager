package com.teamramrod.solarpanelmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.solarpanelmanager.R;
import com.teamramrod.Constants;

public class ApplicationPreferencesActivity extends SherlockPreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupActionBar();

		// TODO replace these old deprecated api calls with ABS calls
		addPreferencesFromResource(R.layout.activity_preferences);

		PreferenceManager.setDefaultValues(this, R.layout.activity_preferences, true);

		final CheckBoxPreference powerUserPref = (CheckBoxPreference) this
				.findPreference(Constants.POWER_USER_PREFERENCE);

		powerUserPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference arg0) {
				if (!powerUserPref.isChecked()) {
					powerUserPref.setChecked(false);
				} else {
					new AlertDialog.Builder(ApplicationPreferencesActivity.this)
							.setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.power_user_message_title)
							.setMessage(R.string.power_user_warning)
							.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									powerUserPref.setChecked(true);
								}

							}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									powerUserPref.setChecked(false);
								}
							}).setOnCancelListener(new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {
									powerUserPref.setChecked(false);
								}
							}).show();
				}
				return false;
			}
		});
	}

	private void setupActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}
}
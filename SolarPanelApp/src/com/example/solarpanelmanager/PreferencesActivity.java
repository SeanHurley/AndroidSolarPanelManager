package com.example.solarpanelmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.example.Constants;

public class PreferencesActivity extends SherlockPreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO replace these old deprecated api calls with ABS calls
		addPreferencesFromResource(R.layout.activity_preferences);

		PreferenceManager.setDefaultValues(this, R.layout.activity_preferences, true);

		final CheckBoxPreference pref = (CheckBoxPreference) this.findPreference(Constants.POWER_USER_PREFERENCE);
		pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference arg0) {
				if (!pref.isChecked()) {
					pref.setChecked(false);
				} else {
					new AlertDialog.Builder(PreferencesActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle(R.string.power_user_message_title).setMessage(R.string.power_user_warning)
							.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									pref.setChecked(true);
								}

							}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									pref.setChecked(false);
								}
							}).show();
				}
				return false;
			}
		});
	}
}
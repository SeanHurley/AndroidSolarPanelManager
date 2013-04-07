package com.example.solarpanelmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.example.Constants;
import com.example.bluetooth.Callback;
import com.example.bluetooth.PINUpdateHandler;
import com.example.solarpanelmanager.api.responses.BaseResponse;

public class PreferencesActivity extends SherlockPreferenceActivity {

	private String oldPass;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO replace these old deprecated api calls with ABS calls
		addPreferencesFromResource(R.layout.activity_preferences);

		PreferenceManager.setDefaultValues(this, R.layout.activity_preferences, true);

		final CheckBoxPreference powerUserPref = (CheckBoxPreference) this
				.findPreference(Constants.POWER_USER_PREFERENCE);
		final EditTextPreference passPreference = (EditTextPreference) this
				.findPreference(Constants.PASS_PHRASE_PREFERENCE);

		powerUserPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference arg0) {
				if (!powerUserPref.isChecked()) {
					powerUserPref.setChecked(false);
				} else {
					new AlertDialog.Builder(PreferencesActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle(R.string.power_user_message_title).setMessage(R.string.power_user_warning)
							.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									powerUserPref.setChecked(true);
									passPreference.setEnabled(true);
								}

							}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									powerUserPref.setChecked(false);
									passPreference.setEnabled(false);
								}
							}).setOnCancelListener(new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {
									powerUserPref.setChecked(false);
									passPreference.setEnabled(false);
								}
							}).show();
				}
				return false;
			}
		});

		oldPass = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PASS_PHRASE_PREFERENCE, null);
		final String deviceId = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.CURRENT_DEVICE,
				null);
		if (deviceId == null) {
			// TODO - Tell the user that something is wrong
		}
		passPreference.setEnabled(powerUserPref.isChecked());
		passPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, final Object newValue) {
				PINUpdateHandler handler = new PINUpdateHandler(new Callback<BaseResponse>() {

					@Override
					public void onComplete(BaseResponse response) {
						if (response.getResult() == 200) {
							oldPass = (String) newValue;
						} else {
							// TODO Something went wrong, tell the user
							System.out.println("FAILED SETTING NEW PASS");
							passPreference.setText(oldPass);
						}
					}
				}, deviceId, oldPass, (String) newValue);
				handler.performAction();
				return true;
			}
		});
	}
}
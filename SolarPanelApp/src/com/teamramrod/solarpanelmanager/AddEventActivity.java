package com.teamramrod.solarpanelmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.example.solarpanelmanager.R;
import com.teamramrod.Constants;
import com.teamramrod.solarpanelmanager.api.responses.Event;

public class AddEventActivity extends BaseActivity {
	
	private static final long DAY_INTERVAL = 24 * 60 * 60 * 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		
		final EditText startTime = (EditText) findViewById(R.id.StartTime);
		final EditText durationText = (EditText) findViewById(R.id.Duration);
		final EditText nameText = (EditText) findViewById(R.id.Name);
		Button addButton = (Button) findViewById(R.id.AddButton);
		
		final String deviceId = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.CURRENT_DEVICE, null);
		if (deviceId == null){
			// TODO - Tell the user that something is wrong
		}
		
		addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");
				String start = startTime.getText().toString();
				String duration = durationText.getText().toString();
				String name = nameText.getText().toString();
				
				Date startTime = null;
				try {
					startTime = df.parse(start);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Date durationDate = null;
				try {
					durationDate = df.parse(duration);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Date stopTime = new Date(startTime.getTime());
				stopTime.setHours(stopTime.getHours() + durationDate.getHours());
				stopTime.setMinutes(startTime.getMinutes() + durationDate.getMinutes());
				long durationLength = stopTime.getTime() - startTime.getTime();
				
				Event e = new Event("temporary", name, startTime.getTime(), durationLength, DAY_INTERVAL);
				Intent i = new Intent();
				i.putExtra(Constants.EVENT_RESULT_CODE, e);
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}
	
	@Override
	protected void setupActionBar() {
		super.setupActionBar();
		actionBar.setTitle(R.string.menu_activity_add_event_title);
	}
}

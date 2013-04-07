package com.example.solarpanelmanager;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.Constants;
import com.example.bluetooth.Callback;
import com.example.bluetooth.EventHandler;
import com.example.calendar.Calendar;
import com.example.solarpanelmanager.api.responses.Event;
import com.example.solarpanelmanager.api.responses.EventsResponse;

public class CalendarActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		
		String deviceId = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.CURRENT_DEVICE, null);
		if (deviceId == null) {
			// TODO - Tell the user that something is wrong
		}
		
		final ArrayAdapter<Event> arrayAdapter = new ArrayAdapter<Event>(this,
				android.R.layout.simple_list_item_1);
		ListView eventListView = (ListView) findViewById(R.id.calendar_event_list);
		eventListView.setAdapter(arrayAdapter);
		
		(new EventHandler(new Callback<EventsResponse>() {

			@Override
			public void onComplete(EventsResponse response) {
				Calendar calendar = new Calendar(response.getEvents());
				for (Entry<Date, List<Event>> entry : calendar.getCalendar().entrySet()) {
					for (Event ev : entry.getValue()) {
						arrayAdapter.add(ev);
					}
				}
			}
			
		}, deviceId)).performAction();
	}

}

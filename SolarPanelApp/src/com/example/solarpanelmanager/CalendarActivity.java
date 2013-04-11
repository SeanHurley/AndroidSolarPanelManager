package com.example.solarpanelmanager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.Constants;
import com.example.bluetooth.Callback;
import com.example.bluetooth.EventHandler;
import com.example.bluetooth.UnscheduleEventHandler;
import com.example.calendar.BasicCalendar;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.Event;
import com.example.solarpanelmanager.api.responses.EventsResponse;

public class CalendarActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		
		final String deviceId = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.CURRENT_DEVICE, null);
		if (deviceId == null) {
			// TODO - Tell the user that something is wrong
		}
		
		final ArrayAdapter<EventDisplay> arrayAdapter = new ArrayAdapter<EventDisplay>(this,
				android.R.layout.simple_list_item_1);
		ListView eventListView = (ListView) findViewById(R.id.calendar_event_list);
		eventListView.setAdapter(arrayAdapter);
		
		(new EventHandler(new Callback<EventsResponse>() {

			@Override
			public void onComplete(EventsResponse response) {
				BasicCalendar calendar = new BasicCalendar(response.getEvents());
				
				Date start = null, end = null;
				try {
					DateFormat df = new SimpleDateFormat();
					start = df.parse("04/10/2013 4:5 PM, CST");
					end = df.parse("04/10/2013 4:6 PM, CST");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Event e = new Event("some event", "event name", start.getTime(), start.getTime() - end.getTime(), 86400 * 1000);
				calendar.addEvent(e);
				
				for (Entry<String, Event> entry : calendar.getCalendar().entrySet())
					arrayAdapter.add(new EventDisplay(entry.getValue(), entry.getKey()));
			}
			
		}, deviceId)).performAction();
		
		eventListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> lis, View arg1, int position,
					long arg3) {
				EventDisplay disp = (EventDisplay) lis.getItemAtPosition(position);
				(new UnscheduleEventHandler(new Callback<BaseResponse>() {

					@Override
					public void onComplete(BaseResponse response) {
						// TODO Auto-generated method stub
						
					}
					
				}, deviceId, disp.event.getId())).performAction();
			}
			
		});
	}

	private static class EventDisplay {
		public Event event;
		public String time;
		
		public EventDisplay(Event e, String t) {
			event = e;
			time = t;
		}
		
		public String toString() {
			return String.format("%s at %s", event.getName(), time);
		}
	}
	
}

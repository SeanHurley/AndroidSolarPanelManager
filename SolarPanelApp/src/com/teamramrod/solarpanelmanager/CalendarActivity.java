package com.teamramrod.solarpanelmanager;

import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.solarpanelmanager.R;
import com.teamramrod.Constants;
import com.teamramrod.bluetooth.Callback;
import com.teamramrod.bluetooth.EventHandler;
import com.teamramrod.bluetooth.ScheduleEventHandler;
import com.teamramrod.bluetooth.UnscheduleEventHandler;
import com.teamramrod.calendar.BasicCalendar;
import com.teamramrod.solarpanelmanager.api.responses.BaseResponse;
import com.teamramrod.solarpanelmanager.api.responses.Event;
import com.teamramrod.solarpanelmanager.api.responses.EventsResponse;

public class CalendarActivity extends BaseActivity {

	private static final int ADD_EVENT_CODE = 2048;
	
	BasicCalendar calendar;
	String deviceId;
	String pass;
	ArrayAdapter<EventDisplay> arrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		
		deviceId = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.CURRENT_DEVICE, null);
		pass = PreferenceManager.getDefaultSharedPreferences(this).getString(
				Constants.PASS_PHRASE_PREFERENCE + deviceId, null);
		if (deviceId == null) {
			// TODO - Tell the user that something is wrong
		}
		
		arrayAdapter = new ArrayAdapter<EventDisplay>(this,
				android.R.layout.simple_list_item_1);
		ListView eventListView = (ListView) findViewById(R.id.calendar_event_list);
		eventListView.setAdapter(arrayAdapter);
		
		final ProgressDialog loadDialog = new ProgressDialog(CalendarActivity.this);
		loadDialog.setTitle("Loading");
		loadDialog.setMessage("Loading events");
		loadDialog.show();
		
		(new EventHandler(new Callback<EventsResponse>() {

			@Override
			public void onComplete(EventsResponse response) {
				loadDialog.dismiss();
				
				calendar = new BasicCalendar(response.getEvents());
				for (Entry<String, Event> entry : calendar.getCalendar().entrySet())
					arrayAdapter.add(new EventDisplay(entry.getValue(), entry.getKey()));
			}
			
		}, deviceId, pass)).performAction();
		
		eventListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> lis, View arg1, final int position,
					long arg3) {
				new AlertDialog.Builder(CalendarActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.events_delete_confirm_title).setMessage(R.string.events_delete_confirm_message)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface d, int which) {
						final EventDisplay disp = (EventDisplay) lis.getItemAtPosition(position);
						final String id = disp.event.getId();
						
						final ProgressDialog dialog = new ProgressDialog(CalendarActivity.this);
						dialog.setTitle("Loading");
						dialog.setMessage("Communicating with device");
						dialog.show();
						
						(new UnscheduleEventHandler(new Callback<BaseResponse>() {

							@Override
							public void onComplete(BaseResponse response) {
								dialog.dismiss();
								if (response.getResult() == 200) {
									calendar.removeEvent(id);
									arrayAdapter.remove(disp);
								} else {
									Toast.makeText(CalendarActivity.this, "Could not remove event", Toast.LENGTH_SHORT).show();
								}
							}
							
						}, deviceId, pass, id)).performAction();		
					}

				}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				}).setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						// do nothing
					}
				}).show();

			}
			
		});
		
		((Button) findViewById(R.id.calendar_add_event_button)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(CalendarActivity.this, AddEventActivity.class);
				startActivityForResult(i, ADD_EVENT_CODE);
			}
			
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ADD_EVENT_CODE && resultCode == RESULT_OK){
			Bundle bundle = data.getExtras();
			final Event e = (Event) bundle.get(Constants.EVENT_RESULT_CODE);

			final ProgressDialog dialog = new ProgressDialog(CalendarActivity.this);
			dialog.setTitle("Loading");
			dialog.setMessage("Communicating with device");
			dialog.show();
			
			(new ScheduleEventHandler(new Callback<BaseResponse>() {

				@Override
				public void onComplete(BaseResponse response) {
					dialog.dismiss();
					if (response.getResult() == 200) {
						String id = response.getMessage();
						// TODO: replace event data with data from the message instead of
						// assuming it's the same.
						Event toAdd = new Event(id, e.getName(), e.getFirstTime(), e.getDuration(), e.getInterval());
						if (!calendar.addEvent(toAdd)) {
							Toast.makeText(CalendarActivity.this, "Event conflicts with another event", Toast.LENGTH_SHORT).show();
						} else {
							arrayAdapter.add(new EventDisplay(e, BasicCalendar.eventToKey(e)));
						}
					} else {
						Toast.makeText(CalendarActivity.this, "Could not add event", Toast.LENGTH_SHORT).show();
					}
				}
				
			}, deviceId, pass, e.getName(), e.getFirstTime(), e.getDuration(), e.getInterval())).performAction();
		}
	}
	
	private static class EventDisplay {
		public Event event;
		public String time;
		
		public EventDisplay(Event e, String t) {
			event = e;
			time = t;
		}
		
		public String toString() {
			long duration = event.getDuration() / (1000 * 60);
			return String.format("%s at %s for %d minutes", event.getName(), time, duration);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_calendar_menu, menu);
		return true;
	}	
}

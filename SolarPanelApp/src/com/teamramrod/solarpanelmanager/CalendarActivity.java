package com.teamramrod.solarpanelmanager;

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
import com.teamramrod.calendar.Calendar;
import com.teamramrod.solarpanelmanager.api.responses.BaseResponse;
import com.teamramrod.solarpanelmanager.api.responses.Event;
import com.teamramrod.solarpanelmanager.api.responses.EventsResponse;

/**
 * Activity class for the scheduler screen. Displays a list of currently
 * scheduled events and provides a functionality to delete existing events
 * and add new ones.
 * 
 * @author michael
 */
public class CalendarActivity extends BaseActivity {

	private static final int ADD_EVENT_CODE = 2048;
	
	private Calendar calendar;
	private String deviceId;
	private String pass;
	private ArrayAdapter<Event> arrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		
		// this legwork should probably be handled in a shared SolarActivity superclass
		deviceId = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.CURRENT_DEVICE, null);
		pass = PreferenceManager.getDefaultSharedPreferences(this).getString(
				Constants.PASS_PHRASE_PREFERENCE + deviceId, null);
		if (deviceId == null) {
			// TODO - Tell the user that something is wrong
		}
		
		arrayAdapter = new ArrayAdapter<Event>(this,
				android.R.layout.simple_list_item_1);
		ListView eventListView = (ListView) findViewById(R.id.calendar_event_list);
		eventListView.setAdapter(arrayAdapter);
		
		pullEvents();
		
		eventListView.setOnItemClickListener(new EventClickListener());
		
		((Button) findViewById(R.id.calendar_add_event_button)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(CalendarActivity.this, AddEventActivity.class);
				startActivityForResult(i, ADD_EVENT_CODE);
			}
			
		});
	}
	
	/**
	 * Pull the initial list of events from the controller and add them to the
	 * interface.
	 */
	private void pullEvents() {
		final ProgressDialog loadDialog = new ProgressDialog(CalendarActivity.this);
		loadDialog.setTitle("Loading");
		loadDialog.setMessage("Loading events");
		loadDialog.show();
		
		(new EventHandler(new Callback<EventsResponse>() {

			@Override
			public void onComplete(EventsResponse response) {
				loadDialog.dismiss();
				
				calendar = new Calendar(response.getEvents());
				for(Event e : calendar.getEventList())
					arrayAdapter.add(e);
			}
			
		}, deviceId, pass)).performAction();
	}
	
	/**
	 * Make a request to the controller to remove an event. If successful,
	 * also remove the event from the interface.
	 * 
	 * @param event    the event to delete
	 */
	private void removeEvent(final Event event) {
		final ProgressDialog dialog = new ProgressDialog(CalendarActivity.this);
		dialog.setTitle("Loading");
		dialog.setMessage("Communicating with device");
		dialog.show();
		
		(new UnscheduleEventHandler(new Callback<BaseResponse>() {

			@Override
			public void onComplete(BaseResponse response) {
				dialog.dismiss();
				if (response.getResult() == 200) {
					calendar.removeEvent(event.getId());
					arrayAdapter.remove(event);
				} else {
					Toast.makeText(CalendarActivity.this, "Could not remove event", Toast.LENGTH_SHORT).show();
				}
			}
			
		}, deviceId, pass, event.getId())).performAction();
	}
	
	/**
	 * Make a request to the controller to add an event. If successful, add
	 * the event to the interface. The id of the event to be added doesn't
	 * matter since the controller will assign an id.
	 * 
	 * @param event    the event to add
	 */
	private void addEvent(final Event event) {
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
					// assuming it's the same as the event requested to be added.
					Event toAdd = new Event(id, event.getName(), event.getFirstTime(), event.getDuration(), event.getInterval());
					if (!calendar.addEvent(toAdd)) {
						Toast.makeText(CalendarActivity.this, "Event conflicts with another event", Toast.LENGTH_SHORT).show();
					} else {
						arrayAdapter.add(toAdd);
					}
				} else {
					Toast.makeText(CalendarActivity.this, "Could not add event", Toast.LENGTH_SHORT).show();
				}
			}
			
		}, deviceId, pass, event.getName(), event.getFirstTime(), event.getDuration(), event.getInterval())).performAction();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ADD_EVENT_CODE && resultCode == RESULT_OK) {
			addEvent((Event) data.getExtras().get(Constants.EVENT_RESULT_CODE));
		}
	}
	
	/**
	 * Listens for clicks on event rows and deletes the clicked events. 
	 */
	private class EventClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(final AdapterView<?> lis, View arg1, final int position,
				long arg3) {
			new AlertDialog.Builder(CalendarActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(R.string.events_delete_confirm_title).setMessage(R.string.events_delete_confirm_message)
			.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface d, int which) {
					removeEvent((Event) lis.getItemAtPosition(position));	
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
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_calendar_menu, menu);
		return true;
	}
	
	@Override
	protected void setupActionBar() {
		super.setupActionBar();
		actionBar.setTitle(R.string.menu_activity_calendar_title);
	}
}

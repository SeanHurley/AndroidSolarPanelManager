package com.teamramrod.solarpanelmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.solarpanelmanager.R;
import com.teamramrod.Constants;
import com.teamramrod.solarpanelmanager.api.responses.Event;

/**
 * Activity class for the add event screen. The activity displays three fields
 * that can be edited by the user to add a desired event. This activity is
 * reachable from the calendar activity
 * 
 * @author Jim Bright
 */
public class AddEventActivity extends BaseActivity {

	// long to represent the total number of milliseconds in a day.
	private static final long DAY_INTERVAL = 24 * 60 * 60 * 1000;

	/**
	 * The onCreate method sets the layout for the AddActivityEvent for the
	 * application. This method also creates references for all the necessary
	 * fields to create a new event. (i.e. Start time, duration, and the event's
	 * name). Finally, an onClickListener is added to the "Add" button to create
	 * the new event within the application once the user clicks the button.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);

		final EditText startTime = (EditText) findViewById(R.id.StartTime);
		final EditText durationText = (EditText) findViewById(R.id.Duration);
		final EditText nameText = (EditText) findViewById(R.id.Name);
		Button addButton = (Button) findViewById(R.id.AddButton);

		final String deviceId = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.CURRENT_DEVICE,
				null);
		if (deviceId == null) {
			// TODO - Tell the user that something is wrong
		}

		addButton.setOnClickListener(new View.OnClickListener() {

			/**
			 * onClick method for the "Add" button. Once the button is clicked
			 * the function retrieves all the needed information to create a new
			 * event. Then it creates a new event and returns it to the
			 * CalendarActivity in a new Intent once the method finishes.
			 */
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
					// TODO Handle errors if the user inputs a time in an
					// improper format
					e1.printStackTrace();
				}

				Date durationDate = null;
				try {
					durationDate = df.parse(duration);
				} catch (ParseException e1) {
					// TODO Handle errors if the user inputs a time in an
					// improper format
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

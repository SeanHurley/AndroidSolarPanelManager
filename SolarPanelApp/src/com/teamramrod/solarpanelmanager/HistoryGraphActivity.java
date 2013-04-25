package com.teamramrod.solarpanelmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.solarpanelmanager.R;
import com.teamramrod.Constants;
import com.teamramrod.bluetooth.Callback;
import com.teamramrod.bluetooth.HistoryHandler;
import com.teamramrod.solarpanelmanager.api.responses.HistoryResponse;

public class HistoryGraphActivity extends BaseActivity {

	private Button dateButton;
	private Button monthButton;
	private Button timeButton;
	private LinearLayout graphView;
	private View activityIndicator;

	
	ProgressDialog dialog;

	public enum GraphLabelEnum {
		DATE, MONTH, TIME
	}
	
	private void hideUI() {
		activityIndicator.setVisibility(View.VISIBLE);
		dateButton.setVisibility(View.INVISIBLE);
		monthButton.setVisibility(View.INVISIBLE);
		timeButton.setVisibility(View.INVISIBLE);
		graphView.setVisibility(View.INVISIBLE);
	}
	
	private void showUI() {
		activityIndicator.setVisibility(View.GONE);
		dateButton.setVisibility(View.VISIBLE);
		monthButton.setVisibility(View.VISIBLE);
		timeButton.setVisibility(View.VISIBLE);
		graphView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_graph);
		activityIndicator = findViewById(R.id.activityIndicator);
		graphView = (LinearLayout) findViewById(R.id.graph_view);
		
		setupActionBar();
		
		dialog = new ProgressDialog(HistoryGraphActivity.this);
		dialog.setTitle(R.string.Loading);
		dialog.setMessage(getString(R.string.Communicating));

		// BUTTON: Month
		monthButton = (Button) findViewById(R.id.button_month);
		monthButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showGraph(GraphLabelEnum.MONTH);
			}
		});

		// BUTTON: Month
		dateButton = (Button) findViewById(R.id.button_date);
		dateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showGraph(GraphLabelEnum.DATE);
			}
		});

		// BUTTON: Time
		timeButton = (Button) findViewById(R.id.button_time);
		timeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showGraph(GraphLabelEnum.TIME);
			}
		});

	}

	private void showGraph(final GraphLabelEnum graphLabel) {
		hideUI();
		String deviceId = PreferenceManager.getDefaultSharedPreferences(
				HistoryGraphActivity.this).getString(Constants.CURRENT_DEVICE,
				null);
		if (deviceId == null) {
			// TODO - Tell the user that something is wrong
		}

		String pass = PreferenceManager.getDefaultSharedPreferences(
				HistoryGraphActivity.this).getString(
				Constants.PASS_PHRASE_PREFERENCE, null);
		// TODO use the deviceid when calling the handler
		HistoryHandler call = new HistoryHandler(
				new Callback<HistoryResponse>() {
					// Create loading spinner
					@Override
					public void onComplete(HistoryResponse response) {
						if (response == null) {
							// TODO Something went wrong Alert the user
							return;
						}

						LineGraph lineGraph = new LineGraph();
						/**
						 * TODO: Pass a collection of SnapshotResponses to
						 * display in our graph
						 */
						View lineGraphView = lineGraph.getView(
								HistoryGraphActivity.this,
								response.getHistoryData(), graphLabel);
						// Whenever they hit a button, call removeAllViews
						graphView.removeAllViews();
						graphView.addView(lineGraphView);

						// When you actually get all the data and create the
						// dialog, remove the dialog
						showUI();
					}

				}, deviceId, pass);
		call.performAction();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_history_graph_menu, menu);
		return true;
	}
}

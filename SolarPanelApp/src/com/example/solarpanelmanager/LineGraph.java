package com.example.solarpanelmanager;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.example.solarpanelmanager.api.responses.SnapshotResponse;

public class LineGraph {

	private static final String nameOfGraph = "Power Usage";
	private static final String nameXAxis = "Time";
	private static final String nameYAxis = "Power (watts)";
	private static final float sizeOfAxisTitleText = 20;
	private static final float sizeOfLabelsText = 20;

	public Intent getIntent(Context context, Collection<SnapshotResponse> history) {
		// Convert our data into series object via TimeSeries
		// Creates a line called "Line 1"
		TimeSeries series = new TimeSeries(nameOfGraph);

		// TODO fix the labels
		int x = 1;
		for (SnapshotResponse sr : history) {
			series.add(x, sr.getBatteryPercent());
			x++;
		}

		// A graph may have multiple "lines" in them
		// The following snip of code acts as a "collection" which allows us to
		// plot multiple lines
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		mDataset.addSeries(series);

		// A graph may have multiple lines, so again same logic as previous
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

		// Properties for series
		// Following gives a line its properties, thus you can customize this
		// line
		// Each renderer corresponds to a single series
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		renderer.setColor(Color.RED);
		mRenderer.addSeriesRenderer(renderer);
		// Change axis title text size
		mRenderer.setAxisTitleTextSize(sizeOfAxisTitleText);
		// Change axis label text size
		mRenderer.setLabelsTextSize(sizeOfLabelsText);
		// Turn on/off grid
		mRenderer.setShowGrid(true);
		// Turn off Default XAxis label
		mRenderer.setXLabels(0);
		// XAxis Title
		mRenderer.setXTitle(nameXAxis);
		// YAxis Title
		mRenderer.setYTitle(nameYAxis);
		
		// Covert timestamp into date and use this as new XAxis label
		x = 1;
		Date date;
		for (SnapshotResponse sr : history) {
			date = new Date(sr.getTimestamp());
			DateFormat shortDf = DateFormat.getTimeInstance(DateFormat.SHORT);
			// Only display 1 out of every 3 timestamps
			if (x%3==0) {
				mRenderer.addXTextLabel(x, shortDf.format(date));
			}
			x++;
		}

		// Now we create out intent
		Intent intent = ChartFactory.getLineChartIntent(context, mDataset, mRenderer);

		return intent;
	}
}
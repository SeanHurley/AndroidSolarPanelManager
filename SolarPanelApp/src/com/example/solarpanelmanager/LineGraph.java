package com.example.solarpanelmanager;

import java.util.Collection;

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
	private static final String nameXAxis = "Time (seconds)";
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
		mRenderer.setXTitle(nameXAxis);
		mRenderer.setYTitle(nameYAxis);

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

		// Now we create out intent
		Intent intent = ChartFactory.getLineChartIntent(context, mDataset, mRenderer);

		return intent;
	}
}
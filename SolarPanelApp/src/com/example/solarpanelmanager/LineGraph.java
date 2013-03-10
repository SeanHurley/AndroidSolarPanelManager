package com.example.solarpanelmanager;

import java.util.Collection;

import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.solarpanelmanager.api.responses.SnapshotResponse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class LineGraph {
	
	private static final String nameOfGraph = "Power Usage";
	private static final String nameXAxis = "Time (seconds)";
	private static final String nameYAxis = "Power (watts)";
	
	public Intent getIntent(Context context, Collection<SnapshotResponse> history) {
		
		int[] x = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }; // x values!
		int[] y =  { 30, 34, 45, 57, 77, 89, 100, 111 ,123 ,145 }; // y values!
		
		// Convert our data into series object via TimeSeries
		// Creates a line called "Line 1"
		TimeSeries series = new TimeSeries(nameOfGraph);
		
		// loop through values and add them to series
		for (int i = 0; i < x.length; i++) {
			series.add(x[i], y[i]);
		}
		
//		for (SnapshotResponse sr : history) {
//			series.add(sr.getTimestamp(), sr.getPVCurrent()*sr.getPVVoltage());
//		}
		
		// A graph may have multiple "lines" in them
		// The following snip of code acts as a "collection" which allows us to plot multiple lines
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		mDataset.addSeries(series);
		
		
		// A graph may have multiple lines, so again same logic as previous
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.setXTitle(nameXAxis);
		mRenderer.setYTitle(nameYAxis);
		
		// Properties for series
		// Following gives a line its properties, thus you can customize this line
		// Each renderer corresponds to a single series
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		renderer.setColor(Color.CYAN);
		mRenderer.addSeriesRenderer(renderer);
		
		// Now we create out intent
		Intent intent = ChartFactory.getLineChartIntent(context, mDataset, mRenderer);
		
		return intent;
	}
}
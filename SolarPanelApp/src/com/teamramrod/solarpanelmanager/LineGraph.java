package com.teamramrod.solarpanelmanager;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import com.teamramrod.solarpanelmanager.HistoryGraphActivity.GraphLabelEnum;
import com.teamramrod.solarpanelmanager.api.responses.SnapshotResponse;

/**
 * Class for generating the line graph view.
 * 
 * @author Soo Woo
 */
public class LineGraph {

	private static final String nameOfGraph = "Power Usage";
	private static final String nameXAxis = "Time";
	private static final String nameYAxis = "Power (watts)";
	private static final float sizeOfAxisTitleText = 20;
	private static final float sizeOfLabelsText = 20;

	/**
	 * Utilizes AChartEngine (library) to create an instance of the line graph
	 * which is displayed in the history graph activity
	 * 
	 * @param context
	 * @param history the collection of snapshots, snapshot contains the information to plot
	 * @param graphLabel the Enum which specifies different options for x-axis label
	 * @return view of the history graph
	 */
	public View getView(Context context, Collection<SnapshotResponse> history,
			GraphLabelEnum graphLabel) {

		// Convert our data into series object via TimeSeries
		TimeSeries series = new TimeSeries(nameOfGraph);

		int x = 1;
		for (SnapshotResponse sr : history) {
			series.add(x, sr.getBatteryPercent());
			x++;
		}

		// A graph may have multiple "lines" in them
		// The following snip of code acts as a "collection" which allows us to plot multiple lines
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		mDataset.addSeries(series);

		// A graph may have multiple lines, so again same logic as previous
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

		// Each renderer corresponds to a single series
		// Properties for series
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		renderer.setColor(Color.RED);
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.setAxisTitleTextSize(sizeOfAxisTitleText);
		mRenderer.setLabelsTextSize(sizeOfLabelsText);
		mRenderer.setShowGrid(true);
		mRenderer.setXLabels(0);
		mRenderer.setXTitle(nameXAxis);
		mRenderer.setYTitle(nameYAxis);
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setZoomEnabled(true);

		// Covert timestamp into date and use this as new x-axis label
		x = 1;
		Date date;
		for (SnapshotResponse sr : history) {
			date = new Date(sr.getTimestamp());
			// Only display 1 out of every 3 timestamps
			if (x % 3 == 0) {
				switch (graphLabel) {
				case MONTH:
					DateFormat shortDf = DateFormat.getDateInstance(DateFormat.SHORT);
					mRenderer.addXTextLabel(x, shortDf.format(date));
					break;
				case DATE:
					DateFormat fullDf = DateFormat.getDateInstance(DateFormat.FULL);
					mRenderer.addXTextLabel(x, fullDf.format(date));
					break;
				case TIME:
					DateFormat longDf = DateFormat.getTimeInstance(DateFormat.LONG);
					mRenderer.addXTextLabel(x, longDf.format(date));
					break;
				default:
					System.out.println("Did not specify X-Axis Label");
					break;
				}
			}
			x++;
		}

		// Embeds graph in a view to be displayed in history graph activity
		View mChartView = ChartFactory.getLineChartView(context, mDataset, mRenderer);

		return mChartView;
	}
}
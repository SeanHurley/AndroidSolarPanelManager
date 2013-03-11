package com.example.solarpanelmanager;

import com.example.bluetooth.Callback;
import com.example.bluetooth.HistoryHandler;
import com.example.bluetooth.SetChargeConstraintsHandler;
import com.example.bluetooth.ViewChargeConstraintsHandler;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.HistoryResponse;
import com.example.solarpanelmanager.api.responses.ViewChargeConstraintsResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class BatteryActivity extends Activity {
	int minVal;
	int maxVal;
	
	SeekBar min;
	SeekBar max;
	TextView minvalue;
	TextView maxvalue;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battery);
		
		minVal = 0;
		maxVal = 100;

		minvalue = (TextView) findViewById(R.id.minView);
		maxvalue = (TextView) findViewById(R.id.maxView);
		min = (SeekBar) findViewById(R.id.minbar);
		max = (SeekBar) findViewById(R.id.maxbar);
		
		ViewChargeConstraintsHandler i = new ViewChargeConstraintsHandler(new Callback() {

			@Override
			public void onComplete(BaseResponse json) {
				if (json.getResult() == 200) {
					minVal = ((ViewChargeConstraintsResponse) json).getMin();
					maxVal = ((ViewChargeConstraintsResponse) json).getMax();
					System.out.println("start: " + minVal + ", " + maxVal);
					min.setProgress(minVal);
					min.refreshDrawableState();
					max.setProgress(maxVal);
					max.refreshDrawableState();
					View v = findViewById(R.id.activityIndicator);
					v.setVisibility(View.GONE);
					
					v = findViewById(R.id.maxbar);
					v.setVisibility(View.VISIBLE);
					
					v = findViewById(R.id.minbar);
					v.setVisibility(View.VISIBLE);
					
				} else {
					System.out.println("failure in communication");
				}
			}
			
		});
		
		i.performAction();
		
		minvalue.setText("Minimum:" + minVal);
		maxvalue.setText("Maximum:" + maxVal);

		min.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				minvalue.setText("Minimum: " + progress);
				minVal = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				System.out.println("min: " + minVal +", max: " + maxVal);
				updateLevels();
			}
		});

		max.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				maxvalue.setText("Maximum: " + progress);
				maxVal = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				System.out.println("min: " + minVal +", max: " + maxVal);
				updateLevels();
			}
		});
	}
	
	private void updateLevels() {
		
		 SetChargeConstraintsHandler call = new SetChargeConstraintsHandler(new Callback() {
			@Override
			public void onComplete(BaseResponse json) {
				System.out.println(json.getResult());
			}
		}, maxVal, minVal);
		 
		 call.performAction();
		
	}
}

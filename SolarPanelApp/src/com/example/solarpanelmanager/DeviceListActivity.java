package com.example.solarpanelmanager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DeviceListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_list);
		
		ListView listView = (ListView) findViewById(R.id.mainListView);

	    String[] values = new String[] {
	    	"F7:20:4E:27:D3:99",
	    	"4A:0B:EC:2B:F3:E4",
	    	"93:97:30:DD:B3:07",
	    	"24:23:A2:7A:53:B6",
	    	"10:24:24:66:4C:8A",
	    	"EB:71:43:CF:7F:EE",
	    	"6B:07:5B:F2:87:C3"
	    };
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, values);
	    listView.setAdapter(adapter);
	}
	
}

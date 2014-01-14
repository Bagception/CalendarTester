package de.uniulm.bagception.calendar;

import java.util.ArrayList;

import de.uniulm.bagception.bundlemessageprotocol.entities.CalendarEvent;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver.Receiver;
import de.uniulm.bagception.services.ServiceNames;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony.Mms.Part;
import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.content.ClipData.Item;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements Receiver{

	private MyResultReceiver mResultreceiver;
	private ListView mCalendarListView;
	ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mCalendarListView = (ListView) findViewById(R.id.CalendarListView);
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
		mCalendarListView.setAdapter(adapter);
		mCalendarListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Log.d("itemnr:", ""+arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mResultreceiver = new MyResultReceiver(new Handler());
		mResultreceiver.setReceiver(this);
		
		
		// building intent
		String serviceString = ServiceNames.CALENDAR_SERVICE;
		Intent i = new Intent(serviceString);
		i.putExtra("receiverTag", mResultreceiver);
		int[] calendarIDs = {1};
		String[] calendarNames = {};
		i.putExtra("numberOfEvents", 3);
		// adding optional calendar ids or names
//		i.putExtra("calendarIDs", calendarIDs);
//		i.putExtra("calendarNames", calendarNames);
		i.putExtra("requestType", "calendarNames");
		startService(i);
		log("sending calendar event request...");
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		ArrayList<String> calendarEvents = new ArrayList<String>();
		ArrayList<String> calendarNames = new ArrayList<String>();
		String s = resultData.getString("payload");
		if(resultData.containsKey("calendarEvents")){
			calendarEvents = resultData.getStringArrayList("calendarEvents");
			for(String st : calendarEvents){
				log(st);
			}
		}
		if(resultData.containsKey("calendarNames")){
			calendarNames = resultData.getStringArrayList("calendarNames");
			for(String st : calendarNames){
				log(st);
				listItems.add(st);
		        adapter.notifyDataSetChanged();
			}
		}
		
		log("answer received!");
		
	}
	
	private void log(String s){
		Log.d("Tester", s);
	}
	
}

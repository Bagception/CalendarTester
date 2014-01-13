package de.uniulm.bagception.calendar;

import de.uniulm.bagception.intentservicecommunication.MyResultReceiver;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver.Receiver;
import de.uniulm.bagception.services.ServiceNames;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity implements Receiver{

	private MyResultReceiver mResultreceiver;
 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mResultreceiver = new MyResultReceiver(new Handler());
		mResultreceiver.setReceiver(this);
		
		String serviceString = ServiceNames.CALENDAR_SERVICE;
		
		Intent i = new Intent(serviceString);
		i.putExtra("receiverTag", mResultreceiver);
		startService(i);
		log("sending request...");
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		String s = resultData.getString("payload");
		log("answer received! " + s);
		
	}
	
	private void log(String s){
		Log.d("Tester", s);
	}
	
}

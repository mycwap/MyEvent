package ie.ucc.mid;




import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.Menu;
import android.view.MenuItem;

public class Test_Activity extends Activity {
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_test_);
		//alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//		Intent intent = new Intent(Test_Activity.this, OnAlarmReceive.class);
////
////    	startService(intent);
//		
////		
//		AlarmManager alarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
//		 
//		 PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,  intent, PendingIntent.FLAG_UPDATE_CURRENT);
//		 alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),20*1000, pendingIntent); 
		
		Intent i = new Intent(AlarmClock.ACTION_SET_ALARM); 
		i.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm"); 
		i.putExtra(AlarmClock.EXTRA_HOUR, 10); 
		i.putExtra(AlarmClock.EXTRA_MINUTES, 30); 
		startActivity(i); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}
}

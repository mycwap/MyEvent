package ie.ucc.mid;




import java.util.Calendar;

import org.json.JSONException;



import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
//public class Test_Activity extends Activity {
//
//	
//	private TextView cityText;
//	private TextView condDescr;
//	private TextView temp;
//	private TextView press;
//	private TextView windSpeed;
//	private TextView windDeg;
//	
//	private TextView hum;
//	private ImageView imgView;
//	
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_weather);
//		String city = "Cork,IE";
//		
//		cityText = (TextView) findViewById(R.id.cityText);
//		condDescr = (TextView) findViewById(R.id.condDescr);
//		temp = (TextView) findViewById(R.id.temp);
//		hum = (TextView) findViewById(R.id.hum);
//		press = (TextView) findViewById(R.id.press);
//		windSpeed = (TextView) findViewById(R.id.windSpeed);
//		windDeg = (TextView) findViewById(R.id.windDeg);
//		imgView = (ImageView) findViewById(R.id.imageView1);
//		
//		JSONWeatherTask task = new JSONWeatherTask();
//		task.execute(new String[]{city});
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		//getMenuInflater().inflate(R.me, menu);
//		return true;
//	}
//
//	
//	private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
//		
//		@Override
//		protected Weather doInBackground(String... params) {
//			Weather weather = new Weather();
//			String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));
//
//			try {
//				weather = JSONWeatherParser.getWeather(data);
//				
//				// Let's retrieve the icon
//				weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
//				
//			} catch (JSONException e) {				
//				e.printStackTrace();
//			}
//			return weather;
//		
//	}
//		
//		
//		
//		
//			@Override
//		protected void onPostExecute(Weather weather) {			
//			super.onPostExecute(weather);
////			
////			if (weather.iconData != null && weather.iconData.length > 0) {
////				Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length); 
////				imgView.setImageBitmap(img);
////			}
////			
//			cityText.setText("Cork,IE");
//			condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
//			temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "C");
//			hum.setText("" + weather.currentCondition.getHumidity() + "%");
//			press.setText("" + weather.currentCondition.getPressure() + " hPa");
//			windSpeed.setText("" + weather.wind.getSpeed() + " mps");
//			windDeg.setText("" + weather.wind.getDeg() + "�");
//				
//		}
//
//
//
//
//
//
//	
// }
//}
public class Test_Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_test_);
		//alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(Test_Activity.this, OnAlarmReceive.class);
		
		AlarmManager alarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
		 
		 PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,  intent, PendingIntent.FLAG_UPDATE_CURRENT);
		 alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),20*1000, pendingIntent); 
		
//		Intent i = new Intent(AlarmClock.ACTION_SET_ALARM); 
//		i.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm"); 
//		i.putExtra(AlarmClock.EXTRA_HOUR, 10); 
//		i.putExtra(AlarmClock.EXTRA_MINUTES, 30); 
//		startActivity(i); 
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

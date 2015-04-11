package ie.ucc.mid;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;











import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class OnAlarmReceive extends BroadcastReceiver {
	NotificationManager mNotificationManager;
	Context context;

	    private static String IMG_URL = "http://openweathermap.org/img/w/";
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
//    	Weather wt=getWeather();
//   	 showNotification(wt);
		LocationService locationService = new LocationService(context);
		double lat = locationService.getLocation().getLatitude();
		double lon = locationService.getLocation().getLongitude();
		String city = "lat="+lat+"&lon="+lon;

		JSONWeatherTask task = new JSONWeatherTask();
		task.execute(new String[]{city});
		// Log.d(Globals.TAG, "BroadcastReceiver, in onReceive:");

	}

//	public Weather getWeather() {
//		Weather weather = new Weather();
//		String city = "Cork,IE";
//		String data = getWeatherData(city);
//
//		try {
//			weather = JSONWeatherParser.getWeather(data);
//
//			// Let's retrieve the icon
//			weather.iconData = ((new WeatherHttpClient())
//					.getImage(weather.currentCondition.getIcon()));
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return weather;
//
//	}
	
	private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
		
		@Override
		protected Weather doInBackground(String... params) {
			Weather weather = new Weather();
			String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

			try {
				weather = JSONWeatherParser.getWeather(data);
		
		       
				// Let's retrieve the icon
				//weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
				
			} catch (JSONException e) {				
				e.printStackTrace();
			}
			return weather;
		
	}
		
		
		
		
			@Override
		protected void onPostExecute(Weather weather) {			
			super.onPostExecute(weather);

			 showNotification(weather);
		}






	
  }
	private void showNotification(Weather weather) {
		
		String t=weather.currentCondition.getIcon();
		String name=GetImageName.getName(t);
		int resID = context.getResources().getIdentifier( name, "drawable", context.getPackageName());
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(resID)
				.setContentTitle(weather.currentCondition.getDescr())
				.setContentText(
						Math.round((weather.temperature.getTemp() - 273.15))
								+ "¡æ");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, MainActivity.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.build());
	}
}

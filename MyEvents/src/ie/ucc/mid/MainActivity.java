

package ie.ucc.mid;



import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera.Size;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import bolts.AppLinks;

import com.facebook.*;





public class MainActivity extends FragmentActivity {
    static final int RPS = 0;
    static final int SETTINGS = 1;
//    static final int CONTENT = 2;
    static final int FRAGMENT_COUNT = 2;
    Location location = null;
    Boolean isWifiEnable ;
    Boolean isGPSEnable ;
    LocationService locationService;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private MenuItem settings;
    private MenuItem friends;
    private MenuItem share;
    private MenuItem message;
    private boolean isResumed = false;
    private UiLifecycleHelper uiHelper;
    private EditText addressText;
    private EditText whatTextView;
    private EditText dateTextView;
    private EditText timeTextView;
    public List<Event> eventList = new ArrayList<Event>();
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    private boolean hasNativeLink = false;
    
    public void test(View v){
//		Intent Intent1 = new Intent();
//		Intent1.setClass(MainActivity.this, Test_Activity.class);
//    	
//		
//		startActivity(Intent1);
    	//getDataFromUser();
    	
    	PullAsync task = new PullAsync();
		task.execute();
    	
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
         locationService = new LocationService(MainActivity.this);
        setContentView(R.layout.main);
         addressText=(EditText) findViewById(R.id.addresstext);
         whatTextView=(EditText) findViewById(R.id.whattext);
         dateTextView=(EditText) findViewById(R.id.datetext);
         timeTextView=(EditText) findViewById(R.id.timetext);
        FragmentManager fm = getSupportFragmentManager();
        fragments[RPS] = fm.findFragmentById(R.id.rps_fragment);
        fragments[SETTINGS] = fm.findFragmentById(R.id.settings_fragment);
//        fragments[CONTENT] = fm.findFragmentById(R.id.content_fragment);

        FragmentTransaction transaction = fm.beginTransaction();
        for(int i = 0; i < fragments.length; i++) {
            transaction.hide(fragments[i]);
        }
        transaction.commit();

        hasNativeLink = handleNativeLink();
        setAddress();
    }
    public void Reget(View v){
    	setAddress();
    }
    public void setAddress(){
    	location=locationService.getLocation();
        String[] test = {};

        try {
            test = new getLocationAsyncTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (test[0]!=null) {
        	addressText.setText(test[0]);
        }else {
            showToastInAsync("no address");
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;

        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app may be launched into.
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be launched into.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        uiHelper.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        if (hasNativeLink) {
           // showFragment(CONTENT, false);
            hasNativeLink = false;
        } else {
            showFragment(RPS, false);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // only add the menu when the selection fragment is showing
        if (fragments[RPS].isVisible()) {
            if (menu.size() == 0) {
                share = menu.add(R.string.share_on_facebook);
                message = menu.add(R.string.send_with_messenger);
                friends = menu.add(R.string.see_friends);
                settings = menu.add(R.string.check_settings);
            }
            return true;
        } else {
            menu.clear();
            settings = null;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(settings)) {
            showFragment(SETTINGS, true);
            return true;
        } else if (item.equals(friends)) {
            Intent intent = new Intent();
            intent.setClass(this, FriendActivity.class);
            startActivity(intent);
            return true;
        } else if (item.equals(share)) {
        	PushAsync task = new PushAsync();
    		task.execute();
            EventFragment fragment = (EventFragment) fragments[RPS];
            fragment.shareUsingNativeDialog();
            return true;
        } else if (item.equals(message)) {
            EventFragment fragment = (EventFragment) fragments[RPS];
            fragment.shareUsingMessengerDialog();
            return true;
        }
        return false;
    }

    private boolean handleNativeLink() {
        Session existingSession = Session.getActiveSession();
        // If we have a valid existing session, we'll use it; if not, open one using the provided Intent
        // but do not cache the token (we don't want to use the same user identity the next time the
        // app is run).
        if (existingSession == null || !existingSession.isOpened()) {
            AccessToken accessToken = AccessToken.createFromNativeLinkingIntent(getIntent());
            if (accessToken != null) {
                Session newSession = new Session.Builder(this).setTokenCachingStrategy(new NonCachingTokenCachingStrategy())
                        .build();
                newSession.open(accessToken, null);

                Session.setActiveSession(newSession);
            }
        }

        return false;
    }

    class getLocationAsyncTask extends AsyncTask<Void, Void, String[]> {

        @SuppressLint("ShowToast")

        @Override
        protected String[] doInBackground(Void... params) {


            String[] locationString = new String[5];

            if (location != null) {
                try {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                    try {

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);

                        if (addresses != null) {

                            for (int i = 0; i < addresses.size(); i++) {
                                Address address = addresses.get(i);
                                StringBuilder strReturnedAddress = new StringBuilder();

                                int n = address.getMaxAddressLineIndex();
                                for (int a = 0; a <= n; a++) {
                                    strReturnedAddress.append(
                                            address.getAddressLine(a)).append(
                                            ",");
                                }
                                ;
                                locationString[i] = strReturnedAddress.toString();
                            }
                            ;
                        } else {

                            showToastInAsync("Can't get address");


                        }
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Log.v("Add", "ERROR : " + e);
                    e.printStackTrace();
                }
            } else {
                showToastInAsync("Can't get coordinates");
            }

            return locationString;
        }

    }
    public void showToastInAsync(final String toast) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (isResumed) {
            if (exception != null && !(exception instanceof FacebookOperationCanceledException)) {
                Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show();
                return;
            }

            if (session.isClosed()) {
                showFragment(RPS, false);
            }
        }
    }

    void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        } else {
            int backStackSize = fm.getBackStackEntryCount();
            for (int i = 0; i < backStackSize; i++) {
                fm.popBackStack();
            }
        }
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        transaction.commit();
    }
    protected void getDataFromUser(){
    	String what=whatTextView.getText().toString();
    	String when=dateTextView.getText().toString()+" "+timeTextView.getText().toString();
    	String where=addressText.getText().toString();
    	SharedPreferences.Editor editor = getSharedPreferences("MYEVENT", MODE_PRIVATE).edit();
		 editor.putString("WHERE", what);
		 editor.putString("WHEN", when);
		 editor.putString("WHAT", what);
		 editor.commit();
    	updateToWebService(what, when, where);
    }
	protected void updateToWebService(String what,String when, String where) {
		// TODO Auto-generated method stub
		try {
			JSONObject obj = new JSONObject();
			obj.put("what", what);
			obj.put("when", when);
			obj.put("where", where);
		
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			URI website = new URI(
					"http://cs1.ucc.ie/~my3/api/index.php?a=new_event&event="
							+ URLEncoder.encode(obj.toString(), "UTF-8"));
			request.setURI(website);
			HttpResponse response = httpclient.execute(request);

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
	}
	
	protected String getEvents() {
		// TODO Auto-generated method stub
		String events = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			URI website = new URI(
					"http://cs1.ucc.ie/~my3/api/index.php?a=get_events");
			request.setURI(website);
			HttpResponse response = httpclient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				events = EntityUtils.toString(response.getEntity());

			}
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		return events;
	}
	protected Event parseEvent() {
		String source = getEvents();
		source=source.replaceAll("\"\\{", "{");
		source=source.replaceAll("\\}\"", "}");
		source=source.replaceAll("\\\\", "");
		try {
			JSONTokener jsonTokener = new JSONTokener(source);
			JSONObject jsonObject = (JSONObject) jsonTokener
					.nextValue();
			JSONArray array = jsonObject.getJSONArray("events");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				Event event = new Event(object.getString("what"),object.getString("when"), object.getString("where"));
				eventList.add(event);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Event test= eventList.get(eventList.size()-1);
		
//		Toast.makeText(MainActivity.this, test.what+"\n", Toast.LENGTH_LONG)
//				.show();
		return test;
	} 

	private class PullAsync extends AsyncTask<Void, Void, Event> {

		@Override
		protected Event doInBackground(Void... params) {
			Event e=parseEvent();
			return e;
		}

		@Override
		protected void onPostExecute(Event result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(MainActivity.this, result.what+"\n", Toast.LENGTH_LONG)
			.show();
		}

	
		
		
		
	  }
private class PushAsync extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		getDataFromUser();
		return null;
	}

  }
}

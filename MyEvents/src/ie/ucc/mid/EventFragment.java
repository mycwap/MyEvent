package ie.ucc.mid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.facebook.*;
import com.facebook.model.GraphObject;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import com.facebook.widget.FacebookDialog;

import java.util.Arrays;
import java.util.Random;

import static ie.ucc.mid.OpenGraphUtils.*;


public class EventFragment extends Fragment {

	private   String SHARE_MY_LINK = "http://www.ucc.ie";
	private   String SHARE_EVENT_NAME = "Let's have a Party";

	private static final String PENDING_PUBLISH_KEY = "pending_publish";
	private static final String IMPLICIT_PUBLISH_KEY = "implicitly_publish";


	private ImageButton fbButton;


	// private InitHandler handler = new InitHandler();
	private Random random = new Random(System.currentTimeMillis());
	private boolean pendingPublish;
	private boolean shouldImplicitlyPublish = true;


	public void shareUsingNativeDialog() {
		SharedPreferences prefs = getActivity().getSharedPreferences("MYEVENT", Context.MODE_PRIVATE); 
		String WhenAndWhere="We will have party at "+" "+prefs.getString("WHEN", "today")+" in "+prefs.getString("WHERE", "UCC");
		String What=prefs.getString("WHERE", "today");
		
		FacebookDialog.ShareDialogBuilder builder = new FacebookDialog.ShareDialogBuilder(
				getActivity()).setLink(SHARE_MY_LINK)
				.setName(What).setDescription(WhenAndWhere)

				.setFragment(this);
		//builder.build().present();
		// share the app
		if (builder.canPresent()) {
			builder.build().present();
		}

	}

	public void shareUsingMessengerDialog() {
		SharedPreferences prefs = getActivity().getSharedPreferences("MYEVENT", Context.MODE_PRIVATE); 
		String WhenAndWhere="We will have party at "+" "+prefs.getString("WHEN", "today")+" in "+prefs.getString("WHERE", "UCC");
		String What=prefs.getString("WHERE", "today");
		FacebookDialog.MessageDialogBuilder builder = new FacebookDialog.MessageDialogBuilder(
				getActivity()).setLink(SHARE_MY_LINK).setDescription(WhenAndWhere)
				.setName(What).setFragment(this);
		// share the app
		builder.build().present();
//		if (builder.canPresent()) {
//			builder.build().present();
//		}
//		if (true) {
//            FacebookDialog.ShareDialogBuilder builder = new FacebookDialog.ShareDialogBuilder(getActivity())
//                    .setLink(SHARE_GAME_LINK)
//                    .setName(SHARE_GAME_NAME)
//                    .setFragment(this);
//            // share the app
//            if (builder.canPresent()) {
//                builder.build().present();
//            }}
//        } else {
//            ThrowAction throwAction = OpenGraphAction.Factory.createForPost(ThrowAction.class, ThrowAction.TYPE);
////            throwAction.setGesture(getBuiltInGesture(playerChoice));
////            throwAction.setOpposingGesture(getBuiltInGesture(computerChoice));
//
//            // The OG objects have their own bitmaps we could rely on, but in order to demonstrate attaching
//            // an in-memory bitmap (e.g., a game screencap) we'll send the bitmap explicitly ourselves.
////            ImageButton view = gestureImages[playerChoice];
////            BitmapDrawable drawable = (BitmapDrawable) view.getBackground();
////            Bitmap bitmap = drawable.getBitmap();
//
//            FacebookDialog.OpenGraphActionDialogBuilder builder = new FacebookDialog.OpenGraphActionDialogBuilder(
//                    getActivity(),
//                    throwAction,
//                    ThrowAction.PREVIEW_PROPERTY_NAME)
//                    .setFragment(this)
//                    ;
//
//            // share the game play
//            if (builder.canPresent()) {
//                builder.build().present();
//            }
//        }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.event_fragment, container, false);

		fbButton = (ImageButton) view.findViewById(R.id.facebook_button);

		fbButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getActivity().openOptionsMenu();
			}
		});

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			// wins = savedInstanceState.getInt(WIN_KEY);
			// losses = savedInstanceState.getInt(LOSS_KEY);

			pendingPublish = savedInstanceState.getBoolean(PENDING_PUBLISH_KEY);
			shouldImplicitlyPublish = savedInstanceState
					.getBoolean(IMPLICIT_PUBLISH_KEY);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Session.getActiveSession() != null) {
			Session.getActiveSession().onActivityResult(getActivity(),
					requestCode, resultCode, data);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// switchState(currentState, true);
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		// bundle.putInt(WIN_KEY, wins);
		// bundle.putInt(LOSS_KEY, losses);

		bundle.putBoolean(PENDING_PUBLISH_KEY, pendingPublish);
		bundle.putBoolean(IMPLICIT_PUBLISH_KEY, shouldImplicitlyPublish);
	}

}

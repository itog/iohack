package org.alljoyn.bus.sample.chat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class StartScreen extends Activity {

	private static final String TAG = "StartScreen";
	
	public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startscreen);
	}
	
	public void importBtn(View v) {
		//this method is a button listener declared in the xml layout
		switch(v.getId()) {
		case R.id.st_email_btn:
			break;
		case R.id.st_facebook_btn:
			break;
		case R.id.st_phone_btn:
			break;
		case R.id.st_plus_btn:
			break;
		case R.id.st_twitter_btn:
			break;
		}
	}
}

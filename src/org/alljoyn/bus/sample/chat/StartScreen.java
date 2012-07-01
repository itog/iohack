package org.alljoyn.bus.sample.chat;

import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.telephony.TelephonyManager;

public class StartScreen extends Activity {

	private static final String TAG = "StartScreen";
	
	public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startscreen);
        
        // Set the devices default phone number
        String number = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
                .getLine1Number();
        TextView Phoneno=(TextView)findViewById(R.id.st_phone);
        Phoneno.setText(number);
        
        // Set the device's default email address
        String possibleEmail = null;
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getBaseContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
            }
        }
        TextView Email=(TextView)findViewById(R.id.st_email);
        Email.setText(possibleEmail);
        
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

package org.alljoyn.bus.sample.chat;

import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.telephony.TelephonyManager;

public class StartScreen extends Activity {

	private static final String TAG = "StartScreen";
	static final String EMAIL_KEY = "EMAIL_KEY";
	static final String NAME_KEY = "NAME_KEY";
	static final String PHONE_KEY = "PHONE_KEY";
	private SharedPreferences prefs;
	private TextView phoneNum;
	private TextView email;
	private TextView name;
	public static final String PREFS_NAME ="PREFS";
	public static final String HAVE_INFO = "HAVE_INFO";
	
	public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        
        prefs = getSharedPreferences(
				PREFS_NAME, 0);
        if(prefs.getBoolean(HAVE_INFO, false)){
        	startActivity(new Intent(this, ViewChannelActivity.class));
        }
        
        setContentView(R.layout.startscreen);
        
        // Set the devices default phone number
        String number = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
                .getLine1Number();
        phoneNum=(TextView)findViewById(R.id.st_phone);
        phoneNum.setText(number);
        
        // Set the device's default email address
        String possibleEmail = null;
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getBaseContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
            }
        }
        email=(TextView)findViewById(R.id.st_email);
        email.setText(possibleEmail);
        
        name=(TextView)findViewById(R.id.st_name);
        
	}
	
	public void submitInfo(View v) {
		 prefs = getSharedPreferences(
					PREFS_NAME, 0);
		 SharedPreferences.Editor editor = prefs.edit();
		 editor.putString(EMAIL_KEY, email.getText().toString());
		 editor.putString(NAME_KEY, name.getText().toString());
		 editor.putString(PHONE_KEY, phoneNum.getText().toString());
		 editor.putBoolean(HAVE_INFO, true);
		 editor.commit();
		 startActivity(new Intent(this, ViewChannelActivity.class));
		
	}
}

/*
 * Copyright 2011, Qualcomm Innovation Center, Inc.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.alljoyn.bus.sample.chat;

import org.alljoyn.bus.sample.chat.ChatApplication;
import org.alljoyn.bus.sample.chat.Observable;
import org.alljoyn.bus.sample.chat.Observer;
import org.alljoyn.bus.sample.chat.DialogBuilder;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UseActivity extends Activity implements Observer {
	private static final String TAG = "chat.UseActivity";

	public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.use);
        
        //String str = getIntent().getExtras().getString("name");
                
        //mHistoryList = new ArrayAdapter<String>(this, android.R.layout.test_list_item);
        mHistoryList = new SampleAdapter(this);
        ListView hlv = (ListView) findViewById(R.id.useHistoryList);
        hlv.setAdapter(mHistoryList);
        hlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int arg2,
					long id) {
				Log.i("my tag", "HERE");
				TextView nameView = (TextView) v.findViewById(R.id.textName);
				String name = nameView.getText().toString();
				String email = mChatApplication.nameMap.get(name).get(0);
				String phone = mChatApplication.nameMap.get(name).get(1);
				Log.i("my tag", email);
				Log.i("my tag", name);
				Log.i("my tag", phone);
			}
		});
                
//        mJoinButton = (Button)findViewById(R.id.useJoin);
//        mJoinButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                showDialog(DIALOG_JOIN_ID);
//        	}
//        });

        mLeaveButton = (Button)findViewById(R.id.useLeave);
        mLeaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_LEAVE_ID);
            }
        });
        
        
        mAppsButton = (Button)findViewById(R.id.callActions);
        mAppsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	/*
            	Toast toast = Toast.makeText(getApplicationContext(), "Not implemented yet", Toast.LENGTH_SHORT);
                toast.show();*/
            	Intent appsIntent = new Intent(UseActivity.this,AppsActivity.class);
            	UseActivity.this.startActivity(appsIntent);
            	
            }
        });
        
        mChannelName = (TextView)findViewById(R.id.useChannelName);
        mChannelStatus = (TextView)findViewById(R.id.useChannelStatus);
        
        /*
         * Keep a pointer to the Android Appliation class around.  We use this
         * as the Model for our MVC-based application.    Whenever we are started
         * we need to "check in" with the application so it can ensure that our
         * required services are running.
         */
        mChatApplication = (ChatApplication)getApplication();
        mChatApplication.checkin();
        
        /*
         * Call down into the model to get its current state.  Since the model
         * outlives its Activities, this may actually be a lot of state and not
         * just empty.
         */
        updateChannelState();
        updateHistory();
        
        /*
         * Now that we're all ready to go, we are ready to accept notifications
         * from other components.
         */
        mChatApplication.addObserver(this);
    }

	public void onDestroy() {
		Log.i(TAG, "onDestroy()");
		mChatApplication = (ChatApplication) getApplication();
		mChatApplication.deleteObserver(this);
		super.onDestroy();
	}

	public static final int DIALOG_JOIN_ID = 0;
	public static final int DIALOG_LEAVE_ID = 1;
	public static final int DIALOG_ALLJOYN_ERROR_ID = 2;

	protected Dialog onCreateDialog(int id) {
		Log.i(TAG, "onCreateDialog()");
		Dialog result = null;
		switch (id) {
		case DIALOG_JOIN_ID: {
			DialogBuilder builder = new DialogBuilder();
			result = builder.createUseJoinDialog(this, mChatApplication);
		}
			break;
		case DIALOG_LEAVE_ID: {
			DialogBuilder builder = new DialogBuilder();
			result = builder.createUseLeaveDialog(this, mChatApplication);
		}
			break;
		case DIALOG_ALLJOYN_ERROR_ID: {
			DialogBuilder builder = new DialogBuilder();
			result = builder.createAllJoynErrorDialog(this, mChatApplication);
		}
			break;
		}
		return result;
	}

	public synchronized void update(Observable o, Object arg) {
		Log.i(TAG, "update(" + arg + ")");
		String qualifier = (String) arg;

		if (qualifier.equals(ChatApplication.APPLICATION_QUIT_EVENT)) {
			Message message = mHandler
					.obtainMessage(HANDLE_APPLICATION_QUIT_EVENT);
			mHandler.sendMessage(message);
		}

		if (qualifier.equals(ChatApplication.HISTORY_CHANGED_EVENT)) {
			Message message = mHandler
					.obtainMessage(HANDLE_HISTORY_CHANGED_EVENT);
			mHandler.sendMessage(message);
		}

		if (qualifier.equals(ChatApplication.USE_CHANNEL_STATE_CHANGED_EVENT)) {
			Message message = mHandler
					.obtainMessage(HANDLE_CHANNEL_STATE_CHANGED_EVENT);
			mHandler.sendMessage(message);
		}

		if (qualifier.equals(ChatApplication.ALLJOYN_ERROR_EVENT)) {
			Message message = mHandler
					.obtainMessage(HANDLE_ALLJOYN_ERROR_EVENT);
			mHandler.sendMessage(message);
		}
	}

	private void updateHistory() {
		Log.i(TAG, "updateHistory()");
		mHistoryList.clear();
		List<String> messages = mChatApplication.getHistory();
		for (String message : messages) {
			mHistoryList.add(message);
		}
		mHistoryList.notifyDataSetChanged();
	}

	private void updateChannelState() {
		Log.i(TAG, "updateHistory()");
		AllJoynService.UseChannelState channelState = mChatApplication
				.useGetChannelState();
		String name = mChatApplication.useGetChannelName();
		if (name == null) {
			name = "Not set";
		}
		mChannelName.setText(name);

		switch (channelState) {
		case IDLE:
			mChannelStatus.setText("Idle");

			mLeaveButton.setEnabled(false);
			break;
		case JOINED:
			mChannelStatus.setText("Joined");

			mLeaveButton.setEnabled(true);
			break;
		}
	}

	/**
	 * An AllJoyn error has happened. Since this activity pops up first we
	 * handle the general errors. We also handle our own errors.
	 */
	private void alljoynError() {
		if (mChatApplication.getErrorModule() == ChatApplication.Module.GENERAL
				|| mChatApplication.getErrorModule() == ChatApplication.Module.USE) {
			showDialog(DIALOG_ALLJOYN_ERROR_ID);
		}
	}

	private static final int HANDLE_APPLICATION_QUIT_EVENT = 0;
	private static final int HANDLE_HISTORY_CHANGED_EVENT = 1;
	private static final int HANDLE_CHANNEL_STATE_CHANGED_EVENT = 2;
	private static final int HANDLE_ALLJOYN_ERROR_EVENT = 3;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_APPLICATION_QUIT_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_APPLICATION_QUIT_EVENT");
				finish();
			}
				break;
			case HANDLE_HISTORY_CHANGED_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_HISTORY_CHANGED_EVENT");
				updateHistory();
				break;
			}
			case HANDLE_CHANNEL_STATE_CHANGED_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_CHANNEL_STATE_CHANGED_EVENT");
				updateChannelState();
				break;
			}
			case HANDLE_ALLJOYN_ERROR_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_ALLJOYN_ERROR_EVENT");
				alljoynError();
				break;
			}
			default:
				break;
			}
		}
	};

	private ChatApplication mChatApplication = null;

	private ArrayAdapter<String> mHistoryList;

	private Button mLeaveButton;
	
	private Button mAppsButton;

	private TextView mChannelName;

	private TextView mChannelStatus;
	
	int[] myImageList = new int[]{R.drawable.android_80_aussie, R.drawable.android_80_avatar2, R.drawable.android_80_bacon, R.drawable.android_80_bigburger, R.drawable.android_80_butterfly, R.drawable.android_80_c3po, R.drawable.android_80_camo, R.drawable.android_80_cyan, R.drawable.android_80_domokun, R.drawable.android_80_grass, R.drawable.android_80_h2g2};

	@SuppressLint({ "ParserError", "ParserError", "ParserError" })
	public class SampleAdapter extends ArrayAdapter<String> {
		static final int resid = R.layout.list_item;

		private LayoutInflater _inflater;

		public SampleAdapter(Context context) {
			super(context, resid);

			_inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView != null) {
				view = convertView;
			} else {
				view = _inflater.inflate(resid, null);
			}

			String item = this.getItem(position);

			((TextView) view.findViewById(R.id.textName)).setText(item);
			ImageView iv = (ImageView)view.findViewById(R.id.imageIcon);
			iv.setImageResource(myImageList[position]);

			return view;
		}
	}
}

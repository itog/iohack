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

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewChannelActivity extends Activity implements Observer {

	private static final String TAG = "chat.UseActivity";
	
	private static final int MENU_REFRESH = 0;
	private static final int MENU_ADDCHANNEL = 1;
	ChatApplication mChatApplication;
	
	
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewchannel);
		
		
		final Activity self = this;
		/*
		 * Keep a pointer to the Android Appliation class around. We use this as
		 * the Model for our MVC-based application. Whenever we are started we
		 * need to "check in" with the application so it can ensure that our
		 * required services are running.
		 */
		mChatApplication = (ChatApplication) getApplication();
		mChatApplication.checkin();

		ArrayAdapter<String> channelListAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1);
		final ListView channelList = (ListView) findViewById(R.id.useJoinChannelList);
		channelList.setAdapter(channelListAdapter);
      channelList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
				String str = ((TextView)v).getText().toString();
				mChatApplication.useSetChannelName(str);
				mChatApplication.useJoinChannel();
				
				Intent i = new Intent(self, UseActivity.class);
				//i.putExtra("name", ((TextView)v).getText());
				self.startActivity(i);
			}
      });


		List<String> channels = mChatApplication.getFoundChannels();
		for (String channel : channels) {
			int lastDot = channel.lastIndexOf('.');
			if (lastDot < 0) {
				continue;
			}
			channelListAdapter.add(channel.substring(lastDot + 1));
		}
		channelListAdapter.notifyDataSetChanged();

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_REFRESH, Menu.NONE, "Refresh");
		menu.add(Menu.NONE, MENU_ADDCHANNEL, Menu.NONE, "Add Ch.");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch (item.getItemId()) {
		case MENU_REFRESH:
			refreshChannelList();
			return true;
		case MENU_ADDCHANNEL:
			Intent i = new Intent(this, HostActivity.class);
			startActivity(i);
			return true;
		}
		return true;
	}

	private void refreshChannelList() {
		ArrayAdapter<String> channelListAdapter = new ArrayAdapter<String>(
				this, android.R.layout.test_list_item);
		final ListView channelList = (ListView) findViewById(R.id.useJoinChannelList);
		channelList.setAdapter(channelListAdapter);

		List<String> channels = mChatApplication.getFoundChannels();
		for (String channel : channels) {
			int lastDot = channel.lastIndexOf('.');
			if (lastDot < 0) {
				continue;
			}
			channelListAdapter.add(channel.substring(lastDot + 1));
		}
		channelListAdapter.notifyDataSetChanged();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}

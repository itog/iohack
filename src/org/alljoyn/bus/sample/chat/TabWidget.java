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

import android.os.Bundle;

import android.app.TabActivity;

import android.widget.TabHost;

import android.content.Intent;
import android.content.res.Resources;

import android.util.Log;

public class TabWidget extends TabActivity {
    private static final String TAG = "chat.TabWidget";
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, UseActivity.class);
        spec = tabHost.newTabSpec("use").setIndicator("", res.getDrawable(R.drawable.ic_tab_use)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, HostActivity.class);
        spec = tabHost.newTabSpec("host").setIndicator("", res.getDrawable(R.drawable.ic_tab_host)).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
}
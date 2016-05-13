/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain	a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS,	WITHOUT	WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
	
  From _The Busy Coder's Guide to Android Development_
    http://commonsware.com/Android
 */

package com.cg.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.cg.callservices.AudioCallScreen;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.util.SingleInstance;

public class PlayerService extends Service {
	public static final String EXTRA_PLAYLIST = "EXTRA_PLAYLIST";
	public static final String EXTRA_SHUFFLE = "EXTRA_SHUFFLE";
	public String INFORMATION = "";

	private boolean isPlaying = false;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// String playlist=intent.getStringExtra(EXTRA_PLAYLIST);
		// boolean useShuffle=intent.getBooleanExtra(EXTRA_SHUFFLE, false);
		Log.d("SER", "OnStart222222222");
		play();

		return (START_NOT_STICKY);
	}

	@Override
	public void onDestroy() {

		stop();
		Log.d("SER", "Ondestroy");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return (null);
	}

	private void play() {
		Log.d("SER", "OnStart");

		isPlaying = true;

		Notification note = new Notification(R.drawable.app_icon,
				SingleInstance.mainContext.getResources().getString(
						R.string.app_name), 0);

		Intent i = null;

		if (WebServiceReferences.contextTable.containsKey("callscreen")) {
			final Object screenInst = WebServiceReferences.contextTable
					.get("callscreen");

			if (screenInst instanceof AudioCallScreen) {
				INFORMATION = SingleInstance.mainContext.getResources().getString(R.string.audiocall_in_progress);
				i = new Intent(this, AudioCallScreen.class);
			}
		}

		// else if (screenInst instanceof AudioPagingSRWindow) {
		// INFORMATION = "Audio Paging in Progress";
		// i = new Intent(this, AudioPagingSRWindow.class);
		//
		// }
		//
		// else if (screenInst instanceof VideoCallScreen) {
		// INFORMATION = "Video Call in Progress";
		// i = new Intent(this, VideoCallScreen.class);
		// }
		//
		// else if (screenInst instanceof VideoPagingSRWindow) {
		// INFORMATION = "Video Paging in Progress";
		// i = new Intent(this, VideoPagingSRWindow.class);
		// }
		//
		// } else {
		// i = new Intent(this, CompleteListView.class);
		// INFORMATION = "";
		// // i.putExtra("HTAB_ID", 1);
		// // i.putExtra("from", "TodayList");
		// }

		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

//		note.setLatestEventInfo(this, SingleInstance.mainContext.getResources().getString(
//				R.string.app_name), INFORMATION, pi);
//		note.flags |= Notification.FLAG_NO_CLEAR;
//
//		startForeground(1337, note);
	}

	// }

	private void stop() {
		if (isPlaying) {
			Log.w(getClass().getName(), "Got to stop()!");
			isPlaying = false;
			stopForeground(true);
		}
	}
}

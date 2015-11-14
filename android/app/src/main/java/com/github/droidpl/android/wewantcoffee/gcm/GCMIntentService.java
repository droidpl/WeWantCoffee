package com.github.droidpl.android.wewantcoffee.gcm;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

public class GCMIntentService extends GcmListenerService {

	public static final String GCM_URL = "http://droidpl-go.appspot.com/regsave";
	public static final String ACTION_MESSAGE = "com.github.droidpl.android.wewantcoffee.NOTIFICATION";


	@Override
	public void onMessageReceived (String from, Bundle data) {
		super.onMessageReceived(from, data);
		//Decide what to do based on the notification behaviour
		Intent intent = new Intent(ACTION_MESSAGE);
		intent.putExtras(data);
		sendBroadcast(intent);
	}
}
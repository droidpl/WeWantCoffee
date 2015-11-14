package com.github.droidpl.android.wewantcoffee.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.github.droidpl.android.wewantcoffee.R;
import com.google.android.gms.gcm.GcmListenerService;

public class NotificationService extends GcmListenerService {

	public static final String ACTION_MESSAGE = "com.github.droidpl.android.wewantcoffee.NOTIFICATION";
	public static final String DEFAULT_INTENT = "com.github.droidpl.android.wewantcoffee.gcm.OPEN";


	@Override
	public void onMessageReceived (String from, Bundle data) {
		super.onMessageReceived(from, data);
		Log.i("Data received", data.toString());
		showNotification(data);
		sendBroadcastIntent(data);
	}

	private void showNotification(Bundle data){
		Intent intent = new Intent(DEFAULT_INTENT);
		intent.putExtras(data);
		intent.setPackage(getPackageName());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//Decide what to do based on the notification behaviour
		Notification notification = new NotificationCompat.Builder(this)
				.setContentTitle("New coffee notification")
				.setContentText(data.getString("message"))
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentIntent(PendingIntent.getActivity(this, 0, intent, Intent.FILL_IN_PACKAGE | PendingIntent.FLAG_UPDATE_CURRENT))
				.setAutoCancel(true)
				.build();
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, notification);

	}

	private void sendBroadcastIntent(Bundle data) {
		Intent intent = new Intent(ACTION_MESSAGE);
		intent.putExtras(data);
		sendBroadcast(intent);
	}
}
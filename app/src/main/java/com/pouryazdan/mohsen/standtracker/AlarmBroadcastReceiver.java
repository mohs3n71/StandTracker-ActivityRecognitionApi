package com.pouryazdan.mohsen.standtracker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Mohsen on 1/9/2017.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentText("It's more than 1 hour you didn't walk");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Stand Tracker");
        builder.addAction(R.mipmap.ic_launcher, "Snooze", pIntent);
        builder.setAutoCancel(true);
        NotificationManagerCompat.from(context).notify(0, builder.build());
    }
}

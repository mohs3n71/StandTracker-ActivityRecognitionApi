package com.pouryazdan.mohsen.standtracker;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mohsen on 1/7/2017.
 */

public class ActivityRecognizedService extends IntentService {

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    private static String LastActivity = null;

    private final int interval = 1000 * 60 * 60; // 1 Second * 60 * 60 = 1 hour


    private void notify_one_hour() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentText("It's more than one hour you didn't walk");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(getString(R.string.app_name));
        NotificationManagerCompat.from(this).notify(0, builder.build());
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getProbableActivities());
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        String activity_name = null;
        int mostConf = 0;
        for (DetectedActivity activity : probableActivities) {
            switch (activity.getType()) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.e("ActivityRecognition", "In Vehicle: " + activity.getConfidence());
                    if (activity.getConfidence() > mostConf) {
                        mostConf = activity.getConfidence();
                        activity_name = "In Vehicle";
                    }
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    Log.e("ActivityRecognition", "On Bicycle: " + activity.getConfidence());
                    if (activity.getConfidence() > mostConf) {
                        mostConf = activity.getConfidence();
                        activity_name = "On Bicycle";
                    }
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    Log.e("ActivityRecognition", "On Foot: " + activity.getConfidence());
                    if (activity.getConfidence() > mostConf) {
                        mostConf = activity.getConfidence();
                        activity_name = "On Foot";
                    }
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e("ActivityRecognition", "Running: " + activity.getConfidence());
                    if (activity.getConfidence() > mostConf) {
                        mostConf = activity.getConfidence();
                        activity_name = "Running";
                    }
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e("ActivityRecognition", "Still: " + activity.getConfidence());
                    if (activity.getConfidence() > mostConf) {
                        mostConf = activity.getConfidence();
                        activity_name = "Still";
                    }

                    break;
                }
                case DetectedActivity.TILTING: {
                    Log.e("ActivityRecognition", "Tilting: " + activity.getConfidence());
                    if (activity.getConfidence() > mostConf) {
                        mostConf = activity.getConfidence();
                        activity_name = "Tilting";
                    }
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e("ActivityRecognition", "Walking: " + activity.getConfidence());
                    if (activity.getConfidence() > mostConf) {
                        mostConf = activity.getConfidence();
                        activity_name = "Walking";
                    }
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.e("ActivityRecognition", "Unknown: " + activity.getConfidence());
                    if (activity.getConfidence() > mostConf) {
                        mostConf = activity.getConfidence();
                        activity_name = "unknown";
                    }
                    break;
                }
            }

            if (LastActivity != "Walking" && LastActivity != "On Foot" && LastActivity != "Running" &&
                    (activity_name == "Walking" || activity_name == "On Foot" || activity_name == "Running")) {


                Calendar c = Calendar.getInstance();

                SimpleDateFormat hour = new SimpleDateFormat("HH", Locale.UK);
                String hh = hour.format(c.getTime());
                Integer hour_int = Integer.parseInt(hh);

                if (hour_int >= 9 && hour_int < 21) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                    String formattedDate = df.format(c.getTime());

                    DbHandler db = new DbHandler(this);
                    StandData stand = db.findByDate(formattedDate);
                    if (stand != null) {
                        Log.e("found", "update " + stand.get_id() + " " + stand.get_count());
                        stand.set_count(stand.get_count() + 1);
                        db.updateStandData(stand);

                    } else {
                        stand = new StandData(formattedDate, 1);
                        db.addStandDate(stand);
                    }

                    Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            this.getApplicationContext(), 234324243, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                            + interval, pendingIntent);

                }
            }
            LastActivity = activity_name;
        }
    }
}


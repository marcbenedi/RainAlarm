package com.rainalarm.marcb.rainalarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

/*
* onReceiver method is exectued every time the app recives an Intent from the Alarm.
* */
public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null)
        {
            if (intent.getAction().equals("startAlarm")){

                MainActivity a = new MainActivity();
                String name = a.PREFS_NAME;
                SharedPreferences preferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
                int RefreshRatePos = preferences.getInt("refreshRate",0);
                int rate[] = {1,3,4,5};

                AlarmManager manager2 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context,AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,alarmIntent,0);
                int interval = 60000*60*rate[RefreshRatePos]; //60 seconds is the minimum
                manager2.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(),interval,pendingIntent);
            }
        }
        else{
            View v = new View(context);
            NotificationManager notificationManeger =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Utility u = new Utility(context);
            u.updateWeatherId(notificationManeger,v);
        }
    }
}

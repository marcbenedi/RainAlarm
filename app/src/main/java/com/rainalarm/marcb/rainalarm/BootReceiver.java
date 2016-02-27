package com.rainalarm.marcb.rainalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class BootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Executed every time the device turn on
            View v = new View(context);

            Intent a = new Intent(context,AlarmReceiver.class);
            a.setAction("startAlarm");
            context.sendBroadcast(a);
        }
    }
}

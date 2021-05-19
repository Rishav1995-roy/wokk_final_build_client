package com.app.wokk.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyNotificationPublisher extends BroadcastReceiver {

    static int NOTIFICATION_ID = 100;
    static String NOTIFICATION = "notification";
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // val notification = intent.getParcelableExtra<Notification>(NOTIFICATION)
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int notificationId = intent.getIntExtra(String.valueOf(NOTIFICATION_ID), 0);
        notificationManager.notify(notificationId, notification);
    }
}

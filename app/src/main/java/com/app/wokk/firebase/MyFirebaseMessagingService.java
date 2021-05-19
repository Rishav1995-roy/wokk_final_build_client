package com.app.wokk.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.app.wokk.R;
import com.app.wokk.activity.SplashActivity;
import com.app.wokk.preference.MyPreference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("TAG", "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        MyPreference myPreference=new MyPreference(this);
        myPreference.setFireBaseToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            int notificationId = new Random().nextInt(10000);
            Intent intentBroadCast = new Intent("MyData");
            Intent intent = new Intent(this, SplashActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "")
                    .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    //.setSmallIcon(R.drawable.defaulticon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_icon))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            notificationBuilder.setSmallIcon(R.drawable.notification_icon);
            notificationBuilder.setColor(getResources().getColor(R.color.white));
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // val badgeCount = 1
            //for 1.1.4+
            notificationManager.notify(notificationId, notificationBuilder.build());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

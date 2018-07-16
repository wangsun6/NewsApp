package com.becsupport.android.arunachalnews.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.becsupport.android.arunachalnews.R;
import com.becsupport.android.arunachalnews.notification.Notice;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Random;

/**
 * Created by WANGSUN on 10-Feb-17.
 */

public class FcmMessagingService extends FirebaseMessagingService {
    int num;

    public FcmMessagingService() {
        int min = 5;
        int max = 6;
        Random r = new Random();
        int random = r.nextInt((max + min) + 1) + min;
        num = random;
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //String title = remoteMessage.getNotification().getTitle();
        //String body = remoteMessage.getNotification().getBody();
        String message = remoteMessage.getData().get("message");

        NotificationCompat.BigTextStyle noti_message=new NotificationCompat.BigTextStyle();
        noti_message.bigText(message);
        noti_message.setBigContentTitle("AP NEWS");
        noti_message.setSummaryText("From AP-NEWS Team.");

        //SimpleDateFormat sdf = new SimpleDateFormat("dd_HHmmss");
        //String currentDateandTime = sdf.format(new Date());

        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("NEWS")
                        .setContentTitle("AP NEWS")
                        .setContentText(message)
                        .setStyle(noti_message)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .setDefaults(Notification.DEFAULT_ALL);
        Intent notificationIntent = new Intent(getApplicationContext(), Notice.class);
        notificationIntent.putExtra("noti",message);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), num, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(num, builder.build());
    }
}

package com.app.activeparks.push;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.app.activeparks.MainActivity;
import com.technodreams.activeparks.R;
import com.google.firebase.messaging.RemoteMessage;

public class PushManager {

    private NotificationManager mNotificationManager;


    public void sendNotification(Context ctx, RemoteMessage remoteMessage){
        String message = remoteMessage.getData().get("body");
        Log.d("push_message", ""+ remoteMessage.getData());

        Intent intent = new Intent(ctx, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        mNotificationManager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("activeparks", "Активні парки", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
        }

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(ctx, "activeparks");
        }else{
            builder = new Notification.Builder(ctx);
        }

        builder.setSmallIcon(R.drawable.logo_active_parks)
                .setContentTitle("Активні парки")
                .setContentText(message)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = builder.build();

        notificationManager.notify(0, notification);
    }
}

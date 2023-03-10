package com.app.activeparks.push;

import android.util.Log;

import com.app.activeparks.data.storage.Preferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushMessagingService extends FirebaseMessagingService {

    public PushManager pushManager = new PushManager();

    @Override
    public void onMessageReceived(RemoteMessage message) {
        if (message.getData() != null) {
            pushManager.sendNotification(this, message);
        }
    }

    @Override
    public void onNewToken(String token) {
        new Preferences(this).setPushToken(token);
        Log.d("token_push", ""+ token);
    }

}

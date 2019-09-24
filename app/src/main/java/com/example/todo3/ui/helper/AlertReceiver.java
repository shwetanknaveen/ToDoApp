package com.example.todo3.ui.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String taskTitle = intent.getStringExtra("taskTitle");
        Log.i("In AlertReceiver ",taskTitle);
        NotificationHelper notificationHelper = new NotificationHelper(context,taskTitle);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
        //notificationHelper.getManager().notify(new Random().nextInt(), nb.build());
        //in place of 1, new Random().nextInt() solves the overlapping issue
    }
}
package com.example.group44.newscollection;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class FavoriteReceiver extends BroadcastReceiver {
    private static final String STATICACTION = "favourite";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        Bundle bundle = intent.getExtras();
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentTitle("已收藏")
                .setContentText(bundle.getString("title"))
                .setSmallIcon(R.drawable.icon110)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        Intent intent1 = new Intent(context, CollectActivity.class);

        intent1.putExtra("message",bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        manager.notify(0, notification);
        // an Intent broadcast.

    }
}

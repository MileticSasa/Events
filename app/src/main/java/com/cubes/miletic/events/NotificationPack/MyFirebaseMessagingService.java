package com.cubes.miletic.events.NotificationPack;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.cubes.miletic.events.R;
import com.cubes.miletic.events.ui.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String message, title, imageUrl, urlAddress;
    private NotificationChannel channel;
    private String channelId = "my_channel";
    public static boolean notificationsTurnedOn;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        title = remoteMessage.getNotification().getTitle();
        message = remoteMessage.getNotification().getBody();
        imageUrl = String.valueOf(remoteMessage.getNotification().getImageUrl());
        urlAddress = remoteMessage.getData().get("address");

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationsTurnedOn) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String name = "Channel";
                String description = "Description";
                int importance = NotificationManager.IMPORTANCE_HIGH;

                channel = new NotificationChannel(channelId, name, importance);
                channel.setDescription(description);

                manager.createNotificationChannel(channel);

                int notificationId = new Random().nextInt();

                Intent notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.putExtra("addr", urlAddress);
                notificationIntent.putExtra("id", notificationId);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        notificationIntent, 0);

                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(getApplicationContext())
                            .asBitmap().load(Uri.parse(imageUrl)).submit().get();
                } catch (Exception e) {
                    Log.d("MOJ TAG", "onMessageReceived: " + e.getMessage());
                }

                Notification notification = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setLargeIcon(bitmap)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(pendingIntent)
                        .build();

                manager.notify(notificationId, notification);
            }
        }
        else{
            manager.cancelAll();

        }
    }
}

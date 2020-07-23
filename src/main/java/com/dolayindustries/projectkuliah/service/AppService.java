package com.dolayindustries.projectkuliah.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dolayindustries.projectkuliah.LoginActivity;
import com.dolayindustries.projectkuliah.R;

public class AppService extends Service {
    private static final String CHANEL_ID = "notif_untuk_admin";
    private static final int NOTIFIKASI_ID = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        tampilNotifikasi();
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void tampilNotifikasi() {
        Intent notifIntent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANEL_ID);
        builder.setSmallIcon(R.drawable.ic_notifications_active_black_24dp);
        builder.setContentTitle("Ada pemberitahuan baru");
        builder.setContentText("Ada pemberitahuan baru, klik untuk meninjau lebih lanjut");
        builder.setContentIntent(pendingIntent);

        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFIKASI_ID, builder.build());

        startForeground(1, builder.build());
    }
}

package com.dolayindustries.projectkuliah.notifikasi;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class AppNotif extends Application {
    private static final String CHANEL_ID = "notif_untuk_admin";

    @Override
    public void onCreate() {
        super.onCreate();
        buatChanelNotifikasi();
    }

    private void buatChanelNotifikasi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence nama = "go akademik notifikasi";
            String deskripsi = "notifikasi dari go akademik";
            int pentingOrTidak = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID, nama, pentingOrTidak);
            notificationChannel.setDescription(deskripsi);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }
}

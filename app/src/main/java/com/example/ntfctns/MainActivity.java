package com.example.ntfctns;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.example.ntfctns.alarm.NtcWorker;
import com.example.ntfctns.utils.Hours;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    static String TG_CHN_ID = "TG_NTC_ID";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        int hours = new Hours().getHours(this);
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(NtcWorker.class, hours, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "PeriodicWork",
                ExistingPeriodicWorkPolicy.KEEP,
                request
        );
    }

    private void createNotificationChannel() {
        CharSequence name = "Tg News";
        String description = "Tg News Desc";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(TG_CHN_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}

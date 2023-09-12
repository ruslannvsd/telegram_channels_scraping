package com.example.ntfctns.alarm;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ntfctns.R;

public class NtcWorker extends Worker {
    static String TG_CHN_ID = "TG_NTC_ID";
    static int TG_NTC_ID = 12345;
    public NtcWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public Result doWork() {
        // scraping logic here
        // String results = scrapeWebsite();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        NotificationChannel channel = new NotificationChannel(TG_CHN_ID, "Tg News", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Tg News Desc");
        notificationManager.createNotificationChannel(channel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), TG_CHN_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Tg News : ")
                .setContentText("Results")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Log.i("Custom DoWork, ", "Works");
        notificationManager.notify(TG_NTC_ID, builder.build());
        return Result.success();
    }
}

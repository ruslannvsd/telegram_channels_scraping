package com.example.ntfctns.alarm;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ntfctns.MainActivity;
import com.example.ntfctns.R;
import com.example.ntfctns.network.GetOfflineLinks;
import com.example.ntfctns.utils.Hours;

public class NtcWorker extends Worker {
    static String TG_CHN_ID = "TG_NTC_ID";
    static int TG_NTC_ID = 12345;
    Context ctx;
    public NtcWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public Result doWork() {
        ctx = getApplicationContext();
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        int hours = new Hours().getHours(ctx);
        String summary = new GetOfflineLinks().getArticles(ctx, hours);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        NotificationChannel channel = new NotificationChannel(TG_CHN_ID, "Tg News", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Tg News Desc");
        notificationManager.createNotificationChannel(channel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, TG_CHN_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Tg News : ")
                .setContentText(summary)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(summary))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(TG_NTC_ID, builder.build());
        return Result.success();
    }
}

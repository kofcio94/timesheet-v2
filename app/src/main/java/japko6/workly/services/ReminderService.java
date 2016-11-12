package japko6.workly.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import japko6.workly.R;
import japko6.workly.objects.Day;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.main.CountingListener;
import japko6.workly.ui.main.MainActivity;
import japko6.workly.utils.CalendarUtils;
import japko6.workly.utils.Logs;
import japko6.workly.utils.ReminderStarter;

public class ReminderService extends Service {

    public static CountingListener countingListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ArrayList<Day> days = Prefs.getDays();
        if (days == null) {
            onDestroy();
        }

        countingListener = new CountingListener() {
            @Override
            public void onBegin() {

            }

            @Override
            public void onNotWorking() {
                onDestroy();
            }
        };

        int avgH = CalendarUtils.getAvgWorkTime(days).getHour();
        if (avgH <= 4) {
            avgH = 4;
        }
        final int finalAvgH = avgH;
        new CountDownTimer(1000 * 60 * 60 * finalAvgH / 2, 1000 * 60) {
            @Override
            public void onTick(long l) {
                Logs.d(ReminderService.this.getClass().getSimpleName(), String.valueOf(l) + "counted minutes, left: " + String.valueOf(1000 * 60 * finalAvgH - l));
            }

            @Override
            public void onFinish() {
                if (Prefs.isWorking()) {
                    showNotification();
                    onDestroy();
                    ReminderStarter.startStopReminderService(ReminderService.this);
                }
            }
        }.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showNotification() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle(getString(R.string.app_name));
        builder.setSmallIcon(R.drawable.ic_work_white_24dp);
        int color = ContextCompat.getColor(this, R.color.button_bg);
        builder.setColor(color);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(getString(R.string.app_name));
        builder.setAutoCancel(true);
        builder.setContentText(getString(R.string.reminder_not_content));
        builder.setTicker(getString(R.string.reminder_not_content));

        long[] pattern = {500, 500};
        builder.setVibrate(pattern);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        manager.notify(69, builder.build());
    }
}

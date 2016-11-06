package japko6.workly.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import japko6.workly.prefs.Prefs;
import japko6.workly.services.ReminderService;

public class ReminderStarter {

    public static void startStopReminderService(Context context) {
        if (Prefs.isWorking()) {
            if (Prefs.isWorkNotificationEnabled()) {
                startService(context);
            } else {
                stopService(context);
            }
        } else {
            stopService(context);
        }
    }

    private static void startService(Context context) {
        if (!isMyServiceRunning(ReminderService.class, context))
            context.startService(new Intent(context, ReminderService.class));
    }

    private static void stopService(Context context) {
        if (isMyServiceRunning(ReminderService.class, context))
            context.stopService(new Intent(context, ReminderService.class));
    }

    private static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

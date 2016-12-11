package japko6.workly.services;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import japko6.workly.R;
import japko6.workly.objects.DateKey;
import japko6.workly.objects.Day;
import japko6.workly.objects.LocationItem;
import japko6.workly.objects.Time;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.main.MainActivity;
import japko6.workly.utils.Counting;
import japko6.workly.utils.Logs;

public class LocationCheckService extends Service {

    public String TAG = this.getClass().getSimpleName();

    private LocationListener locationListener;
    private LocationManager locationManager;

    private boolean showNotification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification = true;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Logs.d(TAG, String.valueOf(location.getLatitude()) + " , " + String.valueOf(location.getLongitude()));
                if (isInRange(location)) {
                    Logs.d(TAG, "IN RANGE");
                    if (Prefs.getGPSMode()) {
                        if (!Prefs.isWorking()) {
                            Time actualTime = new Time();
                            DateKey actualDate = new DateKey();
                            Counting.run(actualDate, actualTime, Day.DESC_GPS);
                            try {
                                MainActivity.countingListener.onBegin();
                            } catch (Exception e) {
                            }
                        }
                    }

                    if (showNotification) {
                        showNotification();
                        showNotification = false;
                    }
                } else {
                    Logs.d(TAG, "NOT IN RANGE");
                    if (Prefs.getGPSMode()) {
                        if (Prefs.isWorking()) {
                            Time actualTime = new Time();
                            Counting.interrupt(actualTime, Day.DESC_GPS);
                            try {
                                MainActivity.countingListener.onNotWorking();
                            } catch (Exception e) {
                            }
                        }
                    }
                    showNotification = true;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Logs.d(TAG, "GPS ENABLED");
                showNotification = true;
            }

            @Override
            public void onProviderDisabled(String provider) {
                Logs.d(TAG, "GPS DISABLED");
                showNotification = false;
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return super.onStartCommand(intent, flags, startId);
        }
        locationManager.requestLocationUpdates("gps", 15000, 0, locationListener);

        Prefs.setServiceLocationTrackingEnabled(true);
        Logs.d(TAG, "Service started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Prefs.setServiceLocationTrackingEnabled(false);
        Logs.d(TAG, "Service stopped");
    }

    private boolean isInRange(Location location) {
        ArrayList<LocationItem> locationItems = Prefs.getLocationsList();
        if (locationItems == null) {
            return false;
        }

        if (locationItems.size() == 0) {
            return false;
        }

        boolean inRange = false;
        for (LocationItem locationItem : locationItems) {
            if (inRange(location, locationItem)) {
                inRange = true;
            }
        }

        return inRange;

    }

    private boolean inRange(Location location, LocationItem locationItem) {
        Location location1 = new Location("");
        location1.setLatitude(locationItem.getLatLng().latitude);
        location1.setLongitude(locationItem.getLatLng().longitude);
        double distance = distance(location, location1);

        Logs.d(TAG, "\n____________________________________\n"
                + "Distance counted from :\n "
                + "____________________________________\n"
                + String.valueOf(location.getLatitude())
                + " , " + String.valueOf(location.getLongitude())
                + " (mine position)" + "\n"
                + String.valueOf(locationItem.getLatLng().latitude)
                + " , " + String.valueOf(locationItem.getLatLng().longitude)
                + " (Work position position)" + "\n"
                + "is " + String.valueOf(distance) + "\n");

        return distance <= locationItem.getRadius();
    }

    private double distance(Location l1, Location l2) {
        return l1.distanceTo(l2);
    }

    private void showNotification() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle(getString(R.string.app_name));
        builder.setSmallIcon(R.drawable.ic_work_white_24dp);
        int color = ContextCompat.getColor(this, R.color.button_bg);
        builder.setColor(color);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(getString(R.string.app_name));
        builder.setAutoCancel(true);
        builder.setContentText(getString(R.string.notification_content));
        builder.setTicker(getString(R.string.notification_content));

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

package japko6.workly.ui.gps.gpsSettings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BasePresenter;
import japko6.workly.utils.Logs;

public class LocationSettingsPresenter extends BasePresenter<LocationSettingsActivity> {

    protected interface view {
        void startService();

        void stopService();

        void startListActivity();

        void requestPermissions();
    }

    @Override
    public void onLoad(LocationSettingsActivity view) {
        super.onLoad(view);
    }

    protected void onBtList() {
        getView().startListActivity();
    }

    protected void onEnableClick(boolean checked) {
        if (checked) {
            if (ActivityCompat.checkSelfPermission(getView(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getView(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                getView().requestPermissions();
                Prefs.setServiceLocationTrackingEnabled(false);
                return;
            }
            getView().startService();
        } else {
            getView().stopService();
        }

        Prefs.setServiceLocationTrackingEnabled(checked);
        Logs.d(this.getClass().getSimpleName(), "SERVICE RUNNING : " + String.valueOf(checked));
    }

    protected void onModeClick(boolean checked) {
        Prefs.setGPSMode(checked);
        Logs.d(this.getClass().getSimpleName(), "MODE GPS SET : " + String.valueOf(checked));
    }
}

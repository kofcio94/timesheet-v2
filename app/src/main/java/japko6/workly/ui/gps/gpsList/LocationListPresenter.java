package japko6.workly.ui.gps.gpsList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import japko6.workly.objects.LocationItem;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BasePresenter;
import japko6.workly.ui.gps.gpsMap.LocationMapActivity;
import japko6.workly.utils.Logs;


public class LocationListPresenter extends BasePresenter<LocationListActivity> {

    protected interface View {
        void startAddActivity(LocationMapActivity.STATE state, @Nullable Integer position);

        void showEmptyListInfo();

        void showDeletedInfo();

        void updateListView();

        void enableNetworkFirsDialog();
    }

    @Override
    public void onLoad(LocationListActivity view) {
        super.onLoad(view);
    }

    protected void onAddClicked() {
        if (networkEnabled())
            getView().startAddActivity(LocationMapActivity.STATE.ADD, null);
        else
            getView().enableNetworkFirsDialog();
    }

    private boolean networkEnabled() {
        try {
            ConnectivityManager nInfo = (ConnectivityManager) getView().getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            nInfo.getActiveNetworkInfo().isConnectedOrConnecting();
            Logs.d("NETWORK ENABLED", "Net avail:"
                    + nInfo.getActiveNetworkInfo().isConnectedOrConnecting());
            ConnectivityManager cm = (ConnectivityManager) getView().getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                Logs.d("NETWORK ENABLED", "Network available:true");
                return true;
            } else {
                Logs.d("NETWORK ENABLED", "Network available:false");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    protected ArrayList<LocationItem> getData() {
        ArrayList<LocationItem> locationItems = Prefs.getLocationsList();
        if (locationItems == null) {
            locationItems = new ArrayList<>();
        }

        return locationItems;
    }

    protected void onDeleteClicked(LocationItem locationItem) {
        ArrayList<LocationItem> locationItems = getView().adapterList;
        if (locationItems == null) {
            getView().showEmptyListInfo();
            return;
        }

        locationItems.remove(locationItem);
        Prefs.setLocationsList(locationItems);
        getView().updateListView();
        getView().showDeletedInfo();
    }
}

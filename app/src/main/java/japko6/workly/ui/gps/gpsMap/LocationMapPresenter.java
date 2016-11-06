package japko6.workly.ui.gps.gpsMap;

import android.location.Address;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import japko6.workly.httpGeo.GeoCoderTask;
import japko6.workly.httpGeo.LocationSearchListener;
import japko6.workly.objects.LocationItem;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BasePresenter;
import japko6.workly.utils.Logs;


public class LocationMapPresenter extends BasePresenter<LocationMapActivity> {

    protected interface View {

        void focusCameraOnLocation(LatLng latLng);

        void showDialogListAddresses(List<Address> list);

        void showEmptySearchListInfo();

        void showNoTitleEnteredInfo();

        void showSuccessfullySavedInfo();

        void noLocationEnteredInfo();

        void showProgressBar();

        void hideProgressBar();

        void noDataGotInfo();

        void noContentInfo();
    }

    @Override
    public void onLoad(LocationMapActivity view) {
        super.onLoad(view);
    }

    protected void onSearchPressed(String searchContent) {
        GeoCoderTask geoCoderTask = new GeoCoderTask(getView(), new LocationSearchListener() {
            @Override
            public void onSearchSuccess(List<Address> list) {
                onSearchEnd(list);
            }

            @Override
            public void onSearchFailureOrEmpty() {
                getView().noDataGotInfo();
            }

            @Override
            public void onStart() {
                getView().showProgressBar();
            }

            @Override
            public void onStop() {
                getView().hideProgressBar();
            }
        });
        geoCoderTask.execute(searchContent);
    }

    public void onSearchEnd(List<Address> list) {
        if (list.size() > 0) {
            if (list.size() > 1) {
                for (Address address : list) {
                    Logs.d("Addresses", address.getFeatureName());
                }
                getView().showDialogListAddresses(list);
            } else {
                getView().focusCameraOnLocation(
                        new LatLng(
                                list.get(0).getLatitude(),
                                list.get(0).getLongitude()
                        )
                );
            }
        } else {
            getView().showEmptySearchListInfo();
        }
    }

    protected void onSaveButton(LocationItem locationItem, @Nullable Marker marker) {
        if (TextUtils.isEmpty(locationItem.getTitle())) {
            getView().showNoTitleEnteredInfo();
            return;
        }

        if (TextUtils.isEmpty(locationItem.getContent())) {
            getView().noContentInfo();
            return;
        }

        if (marker == null) {
            getView().noLocationEnteredInfo();
            return;
        }

        ArrayList<LocationItem> locationItems = Prefs.getLocationsList();
        if (locationItems == null) {
            locationItems = new ArrayList<>();
        }

        locationItem.setPosition(locationItems.size());
        locationItems.add(locationItem);

        Prefs.setLocationsList(locationItems);
        getView().showSuccessfullySavedInfo();
        getView();
        getView().finish();
    }
}

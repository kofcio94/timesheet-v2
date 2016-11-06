package japko6.workly.ui.gps.gpsMap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import japko6.workly.R;
import japko6.workly.objects.AddressItem;
import japko6.workly.objects.LocationItem;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BaseActivity;
import japko6.workly.ui.gps.gpsMap.adapter.AddressesAdapter;

public class LocationMapActivity extends BaseActivity implements LocationMapPresenter.View, OnMapReadyCallback {

    private LocationMapPresenter presenter;
    private ViewHolder viewHolder;

    public static String KEY_STATE = "state";
    public static String KEY_LOCATION_ITEM = "location_item";
    private String headerTitle;
    private LocationItem locationItem;
    private STATE actualState;

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;

    private Circle circle;
    private Marker marker;

    private List<Address> addresses;

    private boolean isShowingDialog = false;

    public enum STATE implements Serializable {
        ADD, SHOW
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        viewHolderSetUp();
        presenterSetUp();
        initViewFromExtras();
        setOnClickListeners();
        initMap();
        try {
            if (!Prefs.areAdvancedAnimationsEnabled()) {
                findViewById(R.id.moving_background).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOnClickListeners() {
        viewHolder.mIvSearch.setOnClickListener(clickListener);
        viewHolder.mIvBtSave.setOnClickListener(clickListener);
        viewHolder.mEtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                locationItem.setTitle(editable.toString());
            }
        });
        viewHolder.mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                locationItem.setContent(editable.toString());
            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == viewHolder.mIvBtSave) {
                presenter.onSaveButton(locationItem, marker);
            } else if (v == viewHolder.mIvSearch) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(viewHolder.mEtSearch.getWindowToken(), 0);
                presenter.onSearchPressed(viewHolder.mEtSearch.getText().toString());
            } else {
                showInDevToast();
            }
        }
    };


    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        viewHolder.mIvSearch.setClickable(true);

        this.googleMap = googleMap;
        LatLng location;
        int zoom;

        if (actualState == STATE.SHOW) {
            location = locationItem.getLatLng();
            googleMap.addMarker(new MarkerOptions().position(location).title(locationItem.getTitle()));
            zoom = 13;
            circle = googleMap.addCircle(new CircleOptions().radius(200)
                    .center(location)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.parseColor("#500084d3")));

            marker = googleMap.addMarker(new MarkerOptions()
                    .title(headerTitle)
                    .position(location)
                    .flat(false)
                    .draggable(false)
            );
        } else {
            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    if (circle != null) {
                        circle.remove();
                    }
                    circle = googleMap.addCircle(new CircleOptions()
                            .radius(200)
                            .center(latLng)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.parseColor("#500084d3"))
                    );

                    if (marker != null) {
                        marker.remove();
                    }

                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .draggable(true)
                            .flat(false)
                    );

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    locationItem.setLatLng(latLng);
                }
            });
            location = new LatLng(51.8335464, 14.6489061);
            zoom = 3;

            googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                    if (circle != null) {
                        circle.remove();
                    }
                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    circle = googleMap.addCircle(new CircleOptions()
                            .radius(200)
                            .center(marker.getPosition())
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.parseColor("#500084d3")));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    locationItem.setLatLng(marker.getPosition());
                }
            });
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
        googleMap.setBuildingsEnabled(true);
        googleMap.setIndoorEnabled(true);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                return false;
            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
        } else {
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);
                }
                break;
            default:
                break;
        }
    }

    private void initViewFromExtras() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        actualState = (STATE) extras.get(KEY_STATE);

        if (actualState == STATE.ADD) {
            headerTitle = getString(R.string.gps_map_toolbar);
            locationItem = new LocationItem("", "", 0, 0);
        } else {
            headerTitle = getString(R.string.tootlbar_add_gps);
            viewHolder.mIvBtSave.setVisibility(View.GONE);
            viewHolder.mLlSearch.setVisibility(View.GONE);
            locationItem = (LocationItem) extras.get(KEY_LOCATION_ITEM);

            if (locationItem != null && TextUtils.isEmpty(locationItem.getContent())) {
                viewHolder.mEtContent.setVisibility(View.GONE);
            }

            if (locationItem == null) {
                viewHolder.mEtContent.setVisibility(View.GONE);
            } else {
                viewHolder.mEtContent.setText(locationItem.getContent());
            }

            if (locationItem != null) {
                viewHolder.mEtTitle.setText(locationItem.getTitle());
            }

            viewHolder.mEtContent.setFocusable(false);
            viewHolder.mEtTitle.setFocusable(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpToolbar();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setUpToolbar() {
        setSupportActionBar(viewHolder.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setSubtitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        viewHolder.mTvToolbar.setText(headerTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void presenterSetUp() {
        presenter = new LocationMapPresenter();
        presenter.onLoad(this);
    }

    @Override
    public void viewHolderSetUp() {
        viewHolder = new ViewHolder(getActivityView());
    }


    @Override
    public void focusCameraOnLocation(LatLng latLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }

    @Override
    public void showDialogListAddresses(List<Address> list) {
        addresses = list;

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View inflatedView = layoutInflater.inflate(R.layout.dialog_addresses_view, null);
        ListView mLvAddresses = (ListView) inflatedView.findViewById(R.id.dialog_addresses_lv);

        List<AddressItem> addressItems = new ArrayList<>();
        for (Address address : list) {
            addressItems.add(new AddressItem(address.getLocality(), address.getPostalCode(), address.getAddressLine(0)));
        }

        AddressesAdapter addressesAdapter = new AddressesAdapter(this, R.layout.item_address, addressItems);
        mLvAddresses.setAdapter(addressesAdapter);

        final BottomDialog addressesDialog = new BottomDialog.Builder(this)
                .setCustomView(inflatedView)
                .setTitle(R.string.adr_dialog_title)
                .build();

        mLvAddresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                double lng = addresses.get(position).getLongitude();
                double lat = addresses.get(position).getLatitude();
                focusCameraOnLocation(new LatLng(lat, lng));
                addressesDialog.dismiss();
                isShowingDialog = false;
            }
        });

        if (!isShowingDialog) {
            addressesDialog.show();
            isShowingDialog = true;
        }
    }

    @Override
    public void showEmptySearchListInfo() {
        showNegativeToast(R.string.empty_search_list);
    }

    @Override
    public void showNoTitleEnteredInfo() {
        showNegativeToast(R.string.no_title_entered);
    }

    @Override
    public void showSuccessfullySavedInfo() {
        showPositiveToast(R.string.global_saved);
    }

    @Override
    public void noLocationEnteredInfo() {
        showNegativeToast(getString(R.string.no_location_entered) + "\n" + getString(R.string.no_location_entered_content));
    }

    private MaterialDialog progressDialog;

    @Override
    public void showProgressBar() {
        viewHolder.mIvBtSave.setClickable(false);
        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .content(R.string.loading)
                .theme(Theme.LIGHT)
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    @Override
    public void hideProgressBar() {
        viewHolder.mIvBtSave.setClickable(true);
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void noDataGotInfo() {
        showNegativeToast(R.string.no_data_gps);
    }

    @Override
    public void noContentInfo() {
        showNegativeToast(R.string.no_content_info);
    }

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.toolbar)
        Toolbar toolbar;

        @Bind(R.id.toolbar_tv)
        TextView mTvToolbar;

        @Bind(R.id.gps_map_ll_search)
        LinearLayout mLlSearch;

        @Bind(R.id.gps_map_search_tv)
        EditText mEtSearch;

        @Bind(R.id.gps_map_search_icon)
        ImageView mIvSearch;

        @Bind(R.id.toolbar_bt_save)
        ImageView mIvBtSave;

        @Bind(R.id.gps_map_title)
        EditText mEtTitle;

        @Bind(R.id.gps_map_content)
        EditText mEtContent;
    }
}

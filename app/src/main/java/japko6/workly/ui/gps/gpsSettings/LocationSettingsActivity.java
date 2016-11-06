package japko6.workly.ui.gps.gpsSettings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kyleduo.switchbutton.SwitchButton;

import at.markushi.ui.CircleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import japko6.workly.R;
import japko6.workly.prefs.Prefs;
import japko6.workly.services.LocationCheckService;
import japko6.workly.ui.base.BaseActivity;
import japko6.workly.ui.gps.gpsList.LocationListActivity;

public class LocationSettingsActivity extends BaseActivity implements LocationSettingsPresenter.view, View.OnClickListener {

    private LocationSettingsPresenter presenter;
    private ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        viewHolderSetUp();
        presenterSetUp();
        initWidgets();
        try {
            if (!Prefs.areAdvancedAnimationsEnabled()) {
                findViewById(R.id.moving_background).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initWidgets() {
        viewHolder.mSwEnable.setCheckedNoEvent(Prefs.getServiceLocationTrackingEnabled());
        viewHolder.mTbMode.setChecked(Prefs.getGPSMode());
        viewHolder.mBtList.setOnClickListener(this);
        viewHolder.mTbMode.setOnClickListener(this);
        viewHolder.llToList.setOnClickListener(this);
        viewHolder.mSwEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                presenter.onEnableClick(viewHolder.mSwEnable.isChecked());
            }
        });
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
        viewHolder.mTvToolbar.setText(R.string.gps_activity_toolbar);
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
        presenter = new LocationSettingsPresenter();
        presenter.onLoad(this);
    }

    @Override
    public void viewHolderSetUp() {
        viewHolder = new ViewHolder(getActivityView());
    }

    @Override
    public void onClick(View v) {
        if (v == viewHolder.mBtList) {
            presenter.onBtList();
        } else if (v == viewHolder.mTbMode) {
            presenter.onModeClick(viewHolder.mTbMode.isChecked());
        } else if (viewHolder.llToList == v) {
            presenter.onBtList();
        } else {
            showInDevToast();
        }
    }

    @Override
    public void startService() {
        startService(new Intent(this, LocationCheckService.class));
    }

    @Override
    public void stopService() {
        stopService(new Intent(this, LocationCheckService.class));
    }

    @Override
    public void startListActivity() {
        Intent intent = new Intent(this, LocationListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    @Override
    public void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);

            }
            viewHolder.mSwEnable.setCheckedNoEvent(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.onEnableClick(true);
                    viewHolder.mSwEnable.setCheckedNoEvent(true);
                }
                break;
            default:
                break;
        }
    }

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.toolbar)
        Toolbar toolbar;

        @Bind(R.id.toolbar_tv)
        TextView mTvToolbar;

        @Bind(R.id.activity_gps_cb_list_places)
        CircleButton mBtList;

        @Bind(R.id.activity_gps_settings_tb_mode)
        ToggleButton mTbMode;

        @Bind(R.id.activity_gps_settings_tb_enable)
        SwitchButton mSwEnable;

        @Bind(R.id.ll_location_to_location_list)
        LinearLayout llToList;
    }
}

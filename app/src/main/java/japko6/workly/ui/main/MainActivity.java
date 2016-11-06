package japko6.workly.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import japko6.workly.R;
import japko6.workly.objects.Time;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.aboutApp.AboutAppActivity;
import japko6.workly.ui.aboutAuthor.AboutAuthorActivity;
import japko6.workly.ui.base.BaseActivity;
import japko6.workly.ui.details.DetailsListFragment;
import japko6.workly.ui.gps.gpsList.LocationListActivity;
import japko6.workly.ui.gps.gpsSettings.LocationSettingsActivity;
import japko6.workly.utils.CalendarUtils;
import japko6.workly.utils.ReminderStarter;
import japko6.workly.widgets.CustomToast;

public class MainActivity extends BaseActivity implements MainPresenter.View {

    private MainPresenter presenter;
    private ViewHolder viewHolder;
    public static CountingListener countingListener;
    private boolean shouldUpdate = false;
    private BroadcastReceiver timeWatcher;
    public Drawer drawer;

    @Override
    protected void viewHolderSetUp() {
        viewHolder = new ViewHolder(getActivityView());
    }

    @Override
    protected void presenterSetUp() {
        presenter = new MainPresenter();
        presenter.onLoad(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewHolderSetUp();
        initSwipeView();
        initView();
        presenterSetUp();
        initDrawer();
    }

    private void initDrawer() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View headerView = layoutInflater.inflate(R.layout.drawer_header, null);

        viewHolder.headerAverage = (TextView) headerView.findViewById(R.id.header_avg);
        viewHolder.headerTotal = (TextView) headerView.findViewById(R.id.header_total);
        setSupportActionBar(viewHolder.toolbar);
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        CustomToast.cancelActualToast();
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .withToolbar(viewHolder.toolbar)
                .inflateMenu(R.menu.drawer)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withHeader(headerView)
                .withSelectedItem(-1)
                .build();

        drawer.deselect();
        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Intent intent;
                switch (view.getId()) {
                    case R.id.d_stats:
                        viewHolder.viewPager.setCurrentItem(0, true);
                        break;
                    case R.id.d_work_log:
                        viewHolder.viewPager.setCurrentItem(1, true);
                        break;
                    case R.id.d_send_report:
                        viewHolder.viewPager.setCurrentItem(2, true);
                        break;
                    case R.id.d_settings:
                        viewHolder.viewPager.setCurrentItem(3, true);
                        break;
                    case R.id.d_gps:
                        intent = new Intent(MainActivity.this, LocationSettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.d_gps_list:
                        intent = new Intent(MainActivity.this, LocationListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.d_about:
                        intent = new Intent(MainActivity.this, AboutAuthorActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.d_about_app:
                        intent = new Intent(MainActivity.this, AboutAppActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        showInDevToast();
                }
                drawer.deselect();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("\t\t" + getString(R.string.app_name));
        viewHolder.swButton.setCheckedNoEvent(Prefs.isWorking());
        if (Prefs.isWorking()) {
            presenter.updateOnScreenTime();
            ReminderStarter.startStopReminderService(this);
        }

        setHeaderValues();
    }

    private void setHeaderValues() {
        try {
            Time timeAvg = CalendarUtils.getAvgWorkTime(Prefs.getDays());
            Time timeTotal = CalendarUtils.getTotalTime(Prefs.getDays());
            String avgH, totalH;
            String avgM, totalM;

            if (timeAvg.getHour() < 10) {
                avgH = "0" + String.valueOf(timeAvg.getHour());
            } else {
                avgH = String.valueOf(timeAvg.getHour());
            }

            if (timeTotal.getHour() < 10) {
                totalH = "0" + String.valueOf(timeTotal.getHour());
            } else {
                totalH = String.valueOf(timeTotal.getHour());
            }

            if (timeAvg.getMinute() < 10) {
                avgM = "0" + String.valueOf(timeAvg.getMinute());
            } else {
                avgM = String.valueOf(timeAvg.getMinute());
            }

            if (timeTotal.getMinute() < 10) {
                totalM = "0" + String.valueOf(timeTotal.getMinute());
            } else {
                totalM = String.valueOf(timeTotal.getMinute());
            }
            viewHolder.headerTotal.setText(totalH + " : " + totalM);
            viewHolder.headerAverage.setText(avgH + " : " + avgM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        timeWatcher = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0)
                    if (shouldUpdate) {
                        presenter.updateOnScreenTime();
                        setHeaderValues();
                    }
            }
        };
        registerReceiver(timeWatcher, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(timeWatcher);
    }

    private void initView() {
        viewHolder.swButton.setCheckedNoEvent(Prefs.isWorking());
        viewHolder.swButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                presenter.onButtonStartStopPressed(b);
            }
        });

        countingListener = new CountingListener() {
            @Override
            public void onBegin() {
                shouldUpdate = true;
            }

            @Override
            public void onNotWorking() {
                shouldUpdate = false;
                viewHolder.titleToolbar.setText(R.string.app_name);

            }
        };

        viewHolder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position != 0)
                    CustomToast.cancelActualToast();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initSwipeView() {
        viewHolder.viewPager = (ViewPager) findViewById(R.id.view_pager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), this);
        viewHolder.viewPager.setAdapter(swipeAdapter);

        viewHolder.pagerTabStrip.setTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));
        viewHolder.pagerTabStrip.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.drawer_about) {
            Intent intent = new Intent(this, AboutAuthorActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.drawer_about_app) {
            Intent intent = new Intent(this, AboutAppActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTodayTimeWorkText(String time) {
        viewHolder.titleToolbar.setText(time);
        try {
            DetailsListFragment.listUpdateInterface.refreshList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.view_pager)
        ViewPager viewPager;

        @Bind(R.id.pager_tab_strip)
        PagerTabStrip pagerTabStrip;

        @Bind(R.id.toolbar)
        Toolbar toolbar;

        @Bind(R.id.sw_toolbar)
        SwitchButton swButton;

        @Bind(R.id.title_toolbar)
        TextView titleToolbar;

        TextView headerAverage, headerTotal;
    }
}

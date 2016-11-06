package japko6.workly.ui.gps.gpsList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

import at.markushi.ui.CircleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import japko6.workly.R;
import japko6.workly.objects.LocationItem;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BaseActivity;
import japko6.workly.ui.gps.gpsList.adapter.LocationAdapter;
import japko6.workly.ui.gps.gpsMap.LocationMapActivity;


public class LocationListActivity extends BaseActivity implements LocationListPresenter.View {

    private LocationListPresenter presenter;
    private ViewHolder viewHolder;
    public ArrayList<LocationItem> adapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        viewHolderSetUp();
        presenterSetUp();
        initListView();
        initWidgets();
        try {
            if (!Prefs.areAdvancedAnimationsEnabled()) {
                findViewById(R.id.moving_background).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpToolbar();
        updateListView();
    }

    @Override
    public void updateListView() {
        adapterList = presenter.getData();
        if (adapterList == null) {
            return;
        }
        LocationAdapter adapter = new LocationAdapter(this, R.layout.item_location, adapterList);
        viewHolder.mLv.setAdapter(adapter);
    }

    @Override
    public void enableNetworkFirsDialog() {
        showNegativeToast(R.string.no_network);
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
        viewHolder.mTvToolbar.setText(R.string.gps_activity_list);
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

    private void initWidgets() {
        viewHolder.mCbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == viewHolder.mCbAdd) {
                    presenter.onAddClicked();
                } else {
                    showInDevToast();
                }
            }
        });
        viewHolder.mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startAddActivity(LocationMapActivity.STATE.SHOW, position);
            }
        });
    }

    private void initListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        LocationListActivity.this);
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        LocationListActivity.this);

                deleteItem.setBackground(ContextCompat.getDrawable(LocationListActivity.this, R.drawable.ripple_transparent_oval));
                deleteItem.setWidth(200);
                deleteItem.setIcon(R.drawable.bin);
                menu.addMenuItem(deleteItem);
            }
        };
        viewHolder.mLv.setMenuCreator(creator);
        viewHolder.mLv.setDividerHeight(0);
        viewHolder.mLv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        presenter.onDeleteClicked(adapterList.get(position));
                        break;
                    default:
                        showInDevToast();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void presenterSetUp() {
        presenter = new LocationListPresenter();
        presenter.onLoad(this);
    }

    @Override
    public void viewHolderSetUp() {
        viewHolder = new ViewHolder(getActivityView());
    }

    @Override
    public void startAddActivity(LocationMapActivity.STATE state, Integer position) {
        Intent intent = new Intent(this, LocationMapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (state == LocationMapActivity.STATE.SHOW) {
            LocationItem i = adapterList.get(position);
            intent.putExtra(LocationMapActivity.KEY_LOCATION_ITEM, i);
        }

        intent.putExtra(LocationMapActivity.KEY_STATE, state);
        startActivity(intent);
    }

    @Override
    public void showEmptyListInfo() {
        showNegativeToast(R.string.empty_search_list);
    }

    @Override
    public void showDeletedInfo() {
        showPositiveToast(R.string.details_fragment_deleted_msg);
    }

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.toolbar)
        Toolbar toolbar;

        @Bind(R.id.toolbar_tv)
        TextView mTvToolbar;

        @Bind(R.id.activity_gps_cb_add)
        CircleButton mCbAdd;

        @Bind(R.id.activity_gps_lv)
        SwipeMenuListView mLv;
    }
}

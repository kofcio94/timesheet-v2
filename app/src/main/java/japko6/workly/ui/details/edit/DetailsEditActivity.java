package japko6.workly.ui.details.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.github.javiersantos.bottomdialogs.BottomDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import japko6.workly.R;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BaseActivity;
import japko6.workly.ui.details.DetailsListFragment;
import japko6.workly.ui.details.adapter.ItemDetail;

public class DetailsEditActivity extends BaseActivity implements DetailsEditPresenter.View {

    protected enum date {
        FROM, TO
    }

    private ItemDetail itemDetail;

    private DetailsEditPresenter presenter;
    private ViewHolder viewHolder;
    private RadialTimePickerDialogFragment mTimePicker;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_details_edit);
        try {
            if (!Prefs.areAdvancedAnimationsEnabled()) {
                findViewById(R.id.moving_background).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getBundleObj();
        viewHolderSetUp();
        presenterSetUp();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpToolbar();
    }

    @Override
    public void onBackPressed() {
        if (!presenter.changesMadeInLayout()) {
            backToMainActivity();
        } else {
            showChangesMadeDialog();
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(viewHolder.mToolbar);
        viewHolder.mToolbar.setTitle("");
        viewHolder.mToolbar.setSubtitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewHolder.mTvToolbar.setText(itemDetail.getDate());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!presenter.changesMadeInLayout()) {
                    backToMainActivity();
                } else {
                    showChangesMadeDialog();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void backToMainActivity() {
        finish();
        DetailsEditActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void validationError() {
        showNegativeToast(R.string.details_edit_validation_error);
    }

    @Override
    public void showCantDeleteEditItemMsg() {
        showNegativeToast(R.string.details_fragment_cant_delete_msg);
    }

    @Override
    public void showDeletedPositionMsg() {
        showPositiveToast(R.string.details_fragment_deleted_msg, true);
        backToMainActivity();
    }

    private void initView() {
        if (itemDetail != null) {
            setUpData();
        }
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        viewHolder.mBtSave.setOnClickListener(clickListener);
        viewHolder.mBtStop.setOnClickListener(clickListener);
        viewHolder.mBtStart.setOnClickListener(clickListener);
        viewHolder.ivDelete.setOnClickListener(clickListener);
        viewHolder.llTimeStart.setOnClickListener(clickListener);
        viewHolder.llTimeStop.setOnClickListener(clickListener);
    }

    private void setUpData() {
        viewHolder.mTvTotal.setText(itemDetail.getTime());

        String string = itemDetail.getWorkInterval();
        String[] parts = string.split(" - ");
        String intervalStart = parts[0];
        String intervalStop = parts[1];

        viewHolder.mTvStart.setText(intervalStart);
        viewHolder.mTvStop.setText(intervalStop);
    }

    @Override
    public void viewHolderSetUp() {
        viewHolder = new ViewHolder(getActivityView());
    }

    @Override
    protected void presenterSetUp() {
        presenter = new DetailsEditPresenter();
        presenter.onLoad(this);
    }

    private void getBundleObj() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemDetail = (ItemDetail) extras.get(DetailsListFragment.KEY_BUNDLE_ITEM_TO_EDIT);
        }
    }

    private void showChangesMadeDialog() {
        new BottomDialog.Builder(this)
                .setTitle(R.string.details_edit_dialog_changes_made_title)
                .setContent(R.string.details_edit_dialog_changes_made_content)
                .setPositiveText(R.string.global_OK)
                .setNegativeText(R.string.global_CANCEL)
                .setCancelable(false)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        bottomDialog.dismiss();
                        presenter.onButtonSave();
                    }
                })
                .onNegative(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        bottomDialog.dismiss();
                        backToMainActivity();
                    }
                })
                .show();
    }

    @Override
    public void showTimePicker(final date d) {
        String cancelText = getString(R.string.global_CANCEL);
        String okText = getString(R.string.global_OK);

        mTimePicker = new RadialTimePickerDialogFragment();

        mTimePicker
                .setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
                        presenter.onTimeChoose(d, hourOfDay, minute);
                    }
                })
                .setThemeLight()
                .setDoneText(okText)
                .setCancelText(cancelText)
                .setForced24hFormat()
                .show(getSupportFragmentManager(), "TIME PICKER");
    }

    @Override
    public void updateText(date d, String time) {
        if (d == date.FROM) {
            viewHolder.mTvStart.setText(time);
        } else {
            viewHolder.mTvStop.setText(time);
        }
    }

    @Override
    public ItemDetail getItemDetail() {
        return itemDetail;
    }

    @Override
    public void showNoChangesMadeInfo() {
        showNegativeToast(R.string.details_new_no_changes_info);
    }

    @Override
    public void updateTotalTime(String time) {
        viewHolder.mTvTotal.setText(time);
    }

    @Override
    public void showSuccessInfo() {
        showPositiveToast(R.string.details_edit_success_info);
        backToMainActivity();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == viewHolder.mBtSave) {
                presenter.onButtonSave();
            } else if (v == viewHolder.mBtStart || v == viewHolder.llTimeStart) {
                presenter.onButtonStart();
            } else if (v == viewHolder.mBtStop || viewHolder.llTimeStop == v) {
                presenter.onButtonStop();
            } else if (v == viewHolder.ivDelete) {
                showDeleteDialog();
            } else {
                showInDevToast();
            }
        }

        private void showDeleteDialog() {
            String date = getItemDetail().getDate();
            new BottomDialog.Builder(DetailsEditActivity.this)
                    .setTitle(R.string.do_you_want_to_delete)
                    .setContent(getString(R.string.item_from) + " " + date)
                    .setPositiveText(R.string.global_OK)
                    .setNegativeText(R.string.global_CANCEL)
                    .setCancelable(false)
                    .onPositive(new BottomDialog.ButtonCallback() {
                        @Override
                        public void onClick(@NonNull BottomDialog bottomDialog) {
                            presenter.onButtonDelete();
                        }
                    })
                    .onNegative(new BottomDialog.ButtonCallback() {
                        @Override
                        public void onClick(@NonNull BottomDialog bottomDialog) {
                            bottomDialog.dismiss();
                        }
                    })
                    .show();
        }
    };

    static class ViewHolder {
        public ViewHolder(android.view.View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.toolbar_details_edit)
        android.support.v7.widget.Toolbar mToolbar;

        @Bind(R.id.toolbar_details_edit_tv)
        TextView mTvToolbar;

        @Bind(R.id.toolbar_details_edit_bt_save)
        ImageView mBtSave;

        @Bind(R.id.activity_details_edit_iv_start)
        ImageView mBtStart;

        @Bind(R.id.activity_details_edit_iv_stop)
        ImageView mBtStop;

        @Bind(R.id.activity_details_edit_tv_start)
        TextView mTvStart;

        @Bind(R.id.activity_details_edit_tv_stop)
        TextView mTvStop;

        @Bind(R.id.activity_details_edit_tv_total)
        TextView mTvTotal;

        @Bind(R.id.toolbar_details_edit_bt_delete)
        ImageView ivDelete;

        @Bind(R.id.details_edit_ll_time_start)
        LinearLayout llTimeStart;

        @Bind(R.id.details_edit_ll_time_stop)
        LinearLayout llTimeStop;
    }
}

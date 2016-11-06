package japko6.workly.ui.details.detailsNew;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import japko6.workly.R;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BaseActivity;
import japko6.workly.utils.CalendarUtils;

public class DetailsNewActivity extends BaseActivity implements DetailsNewPresenter.View {

    private RadialTimePickerDialogFragment mTimePicker;
    private CalendarDatePickerDialogFragment mCalendar;

    protected enum date {
        FROM, TO
    }

    DetailsNewPresenter presenter;
    private ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_new);
        try {
            if (!Prefs.areAdvancedAnimationsEnabled()) {
                findViewById(R.id.moving_background).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolderSetUp();
        presenterSetUp();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpToolbar();
        updateTextViews();
    }

    private void updateTextViews() {
        viewHolder.mTvDate.setText(presenter.getDateString());
        viewHolder.mTvStart.setText(presenter.getStartTime());
        viewHolder.mTvStop.setText(presenter.getStopTime());
    }

    @Override
    public void onBackPressed() {
        backToMainActivity();
    }

    @Override
    protected void presenterSetUp() {
        presenter = new DetailsNewPresenter();
        presenter.onLoad(this);
    }

    private void initView() {
        viewHolder.mBtSave.setOnClickListener(clickListener);
        viewHolder.mBtDate.setOnClickListener(clickListener);
        viewHolder.mBtStart.setOnClickListener(clickListener);
        viewHolder.mBtStop.setOnClickListener(clickListener);
        viewHolder.llDate.setOnClickListener(clickListener);
        viewHolder.llTimeFrom.setOnClickListener(clickListener);
        viewHolder.llTimeTo.setOnClickListener(clickListener);
    }

    private void setUpToolbar() {
        setSupportActionBar(viewHolder.mToolbar);
        viewHolder.mToolbar.setTitle("");
        viewHolder.mToolbar.setSubtitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        viewHolder.mTvToolbar.setText(R.string.details_new_toolbar_tv);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToMainActivity();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showDatePicker() {
        String cancelText = getString(R.string.global_CANCEL);
        String okText = getString(R.string.global_OK);

        mCalendar = new CalendarDatePickerDialogFragment();

        CalendarDatePickerDialogFragment.OnDateSetListener listener =
                new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        presenter.onDateSelectedPressed(year, monthOfYear, dayOfMonth);
                    }
                };

        mCalendar
                .setPreselectedDate(CalendarUtils.getActualYear(), CalendarUtils.getActualMonth(), CalendarUtils.getActualDay())
                .setOnDateSetListener(listener)
                .setThemeLight()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setCancelText(cancelText)
                .setDoneText(okText);
        mCalendar.show(getSupportFragmentManager(), "CALENDAR");
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
    public void updateDateText(String date) {
        viewHolder.mTvDate.setText(date);
    }

    @Override
    public void validationError() {
        showNegativeToast(R.string.details_edit_validation_error);
    }

    @Override
    public void showSuccessMsg() {
        showPositiveToast(R.string.details_edit_success_info);
        backToMainActivity();
    }

    @Override
    public void cantAddDayInThisDateInfo() {
        showNegativeToast(R.string.details_new_cant_add_date_error);
    }

    @Override
    public void backToMainActivity() {
        finish();
    }

    @Override
    public void viewHolderSetUp() {
        viewHolder = new ViewHolder(getActivityView());
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == viewHolder.mBtSave) {
                presenter.onSaveButton();
            } else if (viewHolder.mBtDate == v || viewHolder.llDate == v) {
                presenter.onBtDate();
            } else if (viewHolder.mBtStart == v || viewHolder.llTimeFrom == v) {
                presenter.onBtStart();
            } else if (viewHolder.mBtStop == v || viewHolder.llTimeTo == v) {
                presenter.onBtStop();
            } else {
                showInDevToast();
            }
        }
    };

    static class ViewHolder {
        public ViewHolder(android.view.View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.toolbar_details_new)
        android.support.v7.widget.Toolbar mToolbar;

        @Bind(R.id.toolbar_details_new_tv)
        TextView mTvToolbar;

        @Bind(R.id.toolbar_details_new_bt_save)
        ImageView mBtSave;

        @Bind(R.id.activity_details_new_tv_date)
        TextView mTvDate;

        @Bind(R.id.activity_details_new_tv_start)
        TextView mTvStart;

        @Bind(R.id.activity_details_new_tv_stop)
        TextView mTvStop;

        @Bind(R.id.activity_details_new_iv_date)
        ImageView mBtDate;

        @Bind(R.id.activity_details_new_iv_start)
        ImageView mBtStart;

        @Bind(R.id.activity_details_new_iv_stop)
        ImageView mBtStop;

        @Bind(R.id.details_new_ll_date)
        LinearLayout llDate;

        @Bind(R.id.details_new_ll_time_start)
        LinearLayout llTimeFrom;

        @Bind(R.id.details_new_ll_time_stop)
        LinearLayout llTimeTo;
    }
}

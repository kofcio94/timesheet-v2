package japko6.workly.ui.mail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import japko6.workly.R;
import japko6.workly.objects.DateKey;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BaseFragment;
import japko6.workly.utils.CalendarUtils;

public class MailFragment extends BaseFragment implements MailPresenter.view {
    protected enum date {
        FROM, TO
    }

    private ViewHolder viewHolder;
    private MailPresenter presenter;
    private CalendarDatePickerDialogFragment mCalendar;
    private int textColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mail, container, false);
        try {
            if (!Prefs.areAdvancedAnimationsEnabled()) {
                root.findViewById(R.id.moving_background).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolderSetUp(root);
        presenterSetUp();
        setUpOnClickListeners();
        textColor = viewHolder.mTvDateTo.getCurrentTextColor();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMailContent();
    }

    private void setUpOnClickListeners() {
        viewHolder.mIvDateChangeTo.setOnClickListener(listener);
        viewHolder.mIvDateChangeFrom.setOnClickListener(listener);
        viewHolder.mBtSend.setOnClickListener(listener);
        viewHolder.llFrom.setOnClickListener(listener);
        viewHolder.llTo.setOnClickListener(listener);
    }

    private void setUpMailContent() {
        if (Prefs.getEmail().equals("") || Prefs.getEmail() == null) {
            return;
        }
        viewHolder.mEtMail.setText(Prefs.getEmail());
    }

    @Override
    protected void viewHolderSetUp(View view) {
        viewHolder = new ViewHolder(view);
    }

    @Override
    protected void presenterSetUp() {
        presenter = new MailPresenter();
        presenter.onLoad(this);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == viewHolder.mIvDateChangeTo || v == viewHolder.llTo) {
                presenter.onDateFromChangePressed(date.TO);
            } else if (v == viewHolder.mIvDateChangeFrom || v == viewHolder.llFrom) {
                presenter.onDateFromChangePressed(date.FROM);
            } else if (v == viewHolder.mBtSend) {
                viewHolder.mTvDateTo.setTextColor(textColor);
                viewHolder.mTvDateFrom.setTextColor(textColor);
                viewHolder.mEtMail.setTextColor(textColor);
                presenter.onButtonSend();
            } else {
                showInDevToast();
            }
        }
    };

    @Override
    public void showDatePicker(final date d) {
        String cancelText = getString(R.string.global_CANCEL);
        String okText = getString(R.string.global_OK);

        mCalendar = new CalendarDatePickerDialogFragment();

        CalendarDatePickerDialogFragment.OnDateSetListener listener =
                new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        presenter.onDateSelectedPressed(year, monthOfYear, dayOfMonth, d);
                        if (presenter.validateDate()) {
                            viewHolder.mTvDateFrom.setTextColor(textColor);
                            viewHolder.mTvDateTo.setTextColor(textColor);
                        } else {
                            viewHolder.mTvDateFrom.setTextColor(ContextCompat.getColor(getContext(), R.color.negative_toast));
                            viewHolder.mTvDateTo.setTextColor(ContextCompat.getColor(getContext(), R.color.negative_toast));
                        }
                    }
                };

        if (d == date.FROM) {
            mCalendar.setPreselectedDate(CalendarUtils.getActualYear(), CalendarUtils.getActualMonth(), 1);
        } else {
            mCalendar.setPreselectedDate(CalendarUtils.getActualYear(), CalendarUtils.getActualMonth(), CalendarUtils.getActualDay());
        }

        mCalendar
                .setOnDateSetListener(listener)
                .setThemeLight()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setCancelText(cancelText)
                .setDoneText(okText);
        mCalendar.show(getFragmentManager(), "CALENDAR");
    }

    @Override
    public void setMailEt(String mail) {
        viewHolder.mEtMail.setText(mail);
    }

    @Override
    public void setTimeStringFrom(int year, int month, int day) {
        setTimeString(year, month, day, date.FROM);
    }

    @Override
    public void setTimeStringTo(int year, int month, int day) {
        setTimeString(year, month, day, date.TO);
    }

    private void setTimeString(int year, int month, int day, date d) {
        String timeData = CalendarUtils.parseDateToString(year, month, day);
        if (d == date.FROM) {
            viewHolder.mTvDateFrom.setText(timeData);
        } else {
            viewHolder.mTvDateTo.setText(timeData);
        }
    }

    @Override
    public void showWrongDateInfoFrom() {
        viewHolder.mTvDateFrom.setTextColor(Color.RED);
        showDateError();
    }


    @Override
    public void showWrongDateInfoTo() {
        viewHolder.mTvDateTo.setTextColor(Color.RED);
        showDateError();
    }

    @Override
    public void showEmptyDaysInfo() {
        DateKey date = presenter.getFirstDayOfWork();
        String info;

        if (date != null) {
            info = getString(R.string.mail_fragment_no_data_plus);
        } else {
            info = getString(R.string.mail_fragment_no_data);
        }

        showNegativeToast(info);
    }


    @Override
    public void showNoMailClientInfo() {
        showNegativeToast(R.string.mail_fragment_no_mail_client, true);
    }

    @Override
    public void showNoMailEnteredInfo() {
        viewHolder.mEtMail.setTextColor(Color.RED);
        showDateError();
    }

    @Override
    public void notValidMailInfo() {
        showNegativeToast(R.string.not_valid_mail);
    }

    @Override
    public String getMailSubjectText() {
        return getString(R.string.mail_fragment_report_subject);
    }

    @Override
    public String getTitleOfMailIntent() {
        return getString(R.string.mail_fragment_intent_title);
    }

    @Override
    public String getMail() {
        return viewHolder.mEtMail.getText().toString();
    }

    private void showDateError() {
        showNegativeToast(R.string.wrong_date_entered);
    }

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.fragment_mail_tv_date_from)
        TextView mTvDateFrom;

        @Bind(R.id.fragment_mail_tv_date_to)
        TextView mTvDateTo;

        @Bind(R.id.fragment_mail_et_mail)
        EditText mEtMail;

        @Bind(R.id.fragment_mail__iv_date_picker_to)
        ImageView mIvDateChangeTo;

        @Bind(R.id.fragment_mail__iv_date_picker_from)
        ImageView mIvDateChangeFrom;

        @Bind(R.id.fragment_mail_bt_send)
        Button mBtSend;

        @Bind(R.id.ll_mail_from)
        LinearLayout llFrom;

        @Bind(R.id.ll_mail_to)
        LinearLayout llTo;
    }
}

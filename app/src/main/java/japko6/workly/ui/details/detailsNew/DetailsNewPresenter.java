package japko6.workly.ui.details.detailsNew;

import java.util.ArrayList;

import japko6.workly.objects.DateKey;
import japko6.workly.objects.Day;
import japko6.workly.objects.Time;
import japko6.workly.objects.WorkInterval;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BasePresenter;
import japko6.workly.ui.details.detailsNew.DetailsNewActivity;
import japko6.workly.utils.CalendarUtils;
import japko6.workly.utils.SortUtils;


public class DetailsNewPresenter extends BasePresenter<DetailsNewActivity> {

    private DateKey dateKey;
    private DateKey aDateKey;
    private Time startTime;
    private Time aStartTime;
    private Time stopTime;
    private Time aStopTIme;

    protected interface View {

        void backToMainActivity();

        void showDatePicker();

        void showTimePicker(final DetailsNewActivity.date d);

        void updateText(DetailsNewActivity.date d, String time);

        void updateDateText(String date);

        void validationError();

        void showSuccessMsg();

        void cantAddDayInThisDateInfo();
    }

    @Override
    public void onLoad(DetailsNewActivity view) {
        super.onLoad(view);
        prepareNewData();
    }

    private void prepareNewData() {
        dateKey = new DateKey(CalendarUtils.getActualYear(), CalendarUtils.getActualMonth(), CalendarUtils.getActualDay());
        aDateKey = new DateKey(dateKey.getYear(), dateKey.getMonth(), dateKey.getDay());
        startTime = new Time(0, 0);
        aStartTime = new Time(0, 0);
        stopTime = new Time(0, 0);
        aStopTIme = new Time(0, 0);
    }

    protected void onDateSelectedPressed(int year, int monthOfYear, int dayOfMonth) {
        dateKey.setYear(year);
        dateKey.setMonth(monthOfYear);
        dateKey.setDay(dayOfMonth);

        getView().updateDateText(CalendarUtils.parseDateToString(dateKey));
    }

    protected void onTimeChoose(DetailsNewActivity.date d, int hourOfDay, int minute) {
        String time = CalendarUtils.parseTimeToString(hourOfDay, minute);
        if (d == DetailsNewActivity.date.FROM) {
            if (startTime != null) {
                startTime.setHour(hourOfDay);
                startTime.setMinute(minute);
            } else {
                startTime = new Time(hourOfDay, minute);
            }
        } else {
            if (stopTime != null) {
                stopTime.setHour(hourOfDay);
                stopTime.setMinute(minute);
            } else {
                stopTime = new Time(hourOfDay, minute);
            }
        }
        getView().updateText(d, time);
    }

    protected String getStartTime() {
        return CalendarUtils.parseTimeToString(startTime.getHour(), startTime.getMinute());
    }

    protected String getStopTime() {
        return CalendarUtils.parseTimeToString(stopTime.getHour(), stopTime.getMinute());
    }

    protected String getDateString() {
        return CalendarUtils.parseDateToString(dateKey);
    }

    protected void onSaveButton() {
        if (startTime.getMinute() > stopTime.getMinute() && startTime.getHour() == stopTime.getHour()) {
            getView().validationError();
            return;
        } else if (startTime.getHour() > stopTime.getHour()) {
            getView().validationError();
            return;
        }

        try {
            if (SortUtils.isLeftGreaterThanRight(dateKey, new DateKey())) {
                getView().cantAddDayInThisDateInfo();
                return;
            }
        } catch (Exception e) {
            getView().cantAddDayInThisDateInfo();
            return;
        }

        ArrayList<Day> days = Prefs.getDays();

        if (days == null) {
            days = new ArrayList<>();
            days.add(new Day(dateKey));
            ArrayList<WorkInterval> workIntervals = new ArrayList<>();
            workIntervals.add(new WorkInterval(startTime, stopTime));
            days.get(0).setWorkIntervals(workIntervals);

            Prefs.setDays(days);
            getView().showSuccessMsg();
        } else {
            for (Day day : days) {
                if (day.getDate().equals(dateKey)) {
                    if (day.getWorkIntervals() == null) {
                        day.setWorkIntervals(new ArrayList<WorkInterval>());
                    }
                    day.getWorkIntervals().add(new WorkInterval(startTime, stopTime));

                    Prefs.setDays(days);
                    getView().showSuccessMsg();
                    return;
                }
            }

            days.add(new Day(dateKey));
            if (days.get(days.size() - 1).getWorkIntervals() == null) {
                ArrayList<WorkInterval> workIntervals = new ArrayList<>();
                days.get(days.size() - 1).setWorkIntervals(workIntervals);
            }
            days.get(days.size() - 1).getWorkIntervals().add(new WorkInterval(startTime, stopTime));

            Prefs.setDays(days);
            getView().showSuccessMsg();
        }
    }


    protected void onBtDate() {
        getView().showDatePicker();
    }

    protected void onBtStart() {
        getView().showTimePicker(DetailsNewActivity.date.FROM);
    }

    protected void onBtStop() {
        getView().showTimePicker(DetailsNewActivity.date.TO);
    }
}

package japko6.workly.ui.details.detailsEdit;

import java.util.ArrayList;

import japko6.workly.objects.Day;
import japko6.workly.objects.Time;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BasePresenter;
import japko6.workly.ui.details.adapter.ItemDetail;
import japko6.workly.utils.CalendarUtils;


public class DetailsEditPresenter extends BasePresenter<DetailsEditActivity> {

    ArrayList<Day> days;

    private Time startTime;
    private Time stopTime;

    private Time aStartTime;
    private Time aStopTime;

    public boolean changesMadeInLayout() {
        return !(startTime.equals(aStartTime) && stopTime.equals(aStopTime)) && getView().itemDetail.getDesc().equals(getView().desc);
    }

    protected interface View {

        void showTimePicker(final DetailsEditActivity.date d);

        void updateText(DetailsEditActivity.date d, String time);

        ItemDetail getItemDetail();

        void showNoChangesMadeInfo();

        void updateTotalTime(String time);

        void showSuccessInfo();

        void backToMainActivity();

        void validationError();

        void showCantDeleteEditItemMsg();

        void showDeletedPositionMsg();
    }

    @Override
    public void onLoad(DetailsEditActivity view) {
        super.onLoad(view);
        initValues();
    }

    public void onButtonDelete() {
        days = Prefs.getDays();
        ItemDetail itemDetail = getView().getItemDetail();
        if (days != null) {
            if (days.size() - 1 == itemDetail.getDaysPosition() &&
                    days.get(days.size() - 1).getWorkIntervals().size() - 1 == itemDetail.getWorkIntervalPosition()
                    && Prefs.isWorking()) {
                getView().showCantDeleteEditItemMsg();
                return;
            }
            days.get(itemDetail.getDaysPosition()).getWorkIntervals().remove(itemDetail.getWorkIntervalPosition());
            if (days.get(itemDetail.getDaysPosition()).getWorkIntervals().size() == 0) {
                days.remove(itemDetail.getDaysPosition());
            }
            Prefs.setDays(days);

            getView().showDeletedPositionMsg();
        }
    }

    private void initValues() {
        days = Prefs.getDays();
        if (days != null) {
            ItemDetail itemDetail = getView().getItemDetail();
            startTime = days.get(itemDetail.getDaysPosition()).getWorkIntervals().get(itemDetail.getWorkIntervalPosition()).getStartInterval();
            stopTime = days.get(itemDetail.getDaysPosition()).getWorkIntervals().get(itemDetail.getWorkIntervalPosition()).getStopInterval();
            aStartTime = new Time(startTime.getHour(), startTime.getMinute());
            aStopTime = new Time(stopTime.getHour(), stopTime.getMinute());
        }

    }

    protected void onTimeChoose(DetailsEditActivity.date d, int hourOfDay, int minute) {
        String time = CalendarUtils.parseTimeToString(hourOfDay, minute);
        if (d == DetailsEditActivity.date.FROM) {
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

        Time timeWork;
        if (startTime != null && stopTime != null) {
            timeWork = CalendarUtils.getWorkingTime(startTime, stopTime);
        } else if (startTime == null && stopTime != null) {
            timeWork = CalendarUtils.getWorkingTime(new Time(0, 0), stopTime);
        } else if (startTime != null && stopTime == null) {
            timeWork = CalendarUtils.getWorkingTime(startTime, new Time(23, 59));
        } else {
            timeWork = CalendarUtils.getWorkingTime(new Time(0, 0), new Time(23, 59));
        }
        getView().updateTotalTime(CalendarUtils.parseTimeToString(timeWork.getHour(), timeWork.getMinute()));
    }

    protected void onButtonSave() {
        ItemDetail itemDetail = getView().getItemDetail();
        if (days != null && startTime != null && stopTime != null) {
            if (startTime.getMinute() > stopTime.getMinute() && startTime.getHour() == stopTime.getHour()) {
                getView().validationError();
                return;
            } else if (startTime.getHour() > stopTime.getHour()) {
                getView().validationError();
                return;
            }
            days.get(itemDetail.getDaysPosition()).getWorkIntervals().get(itemDetail.getWorkIntervalPosition()).setStartInterval(startTime);
            days.get(itemDetail.getDaysPosition()).getWorkIntervals().get(itemDetail.getWorkIntervalPosition()).setStopInterval(stopTime);
            days.get(itemDetail.getDaysPosition()).getWorkIntervals().get(itemDetail.getWorkIntervalPosition()).setDescription(getView().desc);
            Prefs.setDays(days);
            getView().showSuccessInfo();
        } else {
            getView().showNoChangesMadeInfo();
        }
    }

    protected void onButtonStart() {
        getView().showTimePicker(DetailsEditActivity.date.FROM);
    }

    protected void onButtonStop() {
        getView().showTimePicker(DetailsEditActivity.date.TO);
    }
}

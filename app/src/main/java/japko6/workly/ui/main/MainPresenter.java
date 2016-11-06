package japko6.workly.ui.main;

import android.content.Intent;

import java.util.ArrayList;

import japko6.workly.objects.DateKey;
import japko6.workly.objects.Day;
import japko6.workly.objects.Time;
import japko6.workly.prefs.Prefs;
import japko6.workly.services.ReminderService;
import japko6.workly.ui.base.BasePresenter;
import japko6.workly.ui.details.DetailsListFragment;
import japko6.workly.utils.CalendarUtils;
import japko6.workly.utils.Counting;
import japko6.workly.utils.ReminderStarter;

public class MainPresenter extends BasePresenter<MainActivity> {

    protected interface View {
        void setTodayTimeWorkText(String time);
    }

    @Override
    public void onLoad(MainActivity view) {
        super.onLoad(view);
        if (Prefs.isWorking()) {
            MainActivity.countingListener.onBegin();
        } else {
            MainActivity.countingListener.onNotWorking();
        }
    }

    protected void onButtonStartStopPressed(boolean buttonState) {
        if (buttonState == Prefs.isWorking()) {
            return;
        }

        Time actualTime = new Time();
        DateKey actualDate = new DateKey();

        if (!buttonState) {
            Counting.interrupt(actualTime);
        } else {
            Counting.run(actualDate, actualTime);
            DetailsListFragment.listUpdateInterface.refreshList();
        }
        ReminderStarter.startStopReminderService(getView());
    }

    protected void updateOnScreenTime() {
        ArrayList<Day> days = Prefs.getDays();
        if (days != null) {
            if (days.size() == 0) {
                return;
            }
            if (!days.get(days.size() - 1).getDate().equals(new DateKey())) {
                Counting.interrupt(new Time());
                Counting.run(new DateKey(), new Time());
            }
            getUpdatedTodayTime();
        }
    }

    private void getUpdatedTodayTime() {
        ArrayList<Day> days = Prefs.getDays();
        DateKey todayDate = new DateKey();
        Time timeWork = null;

        if (days != null) {
            if (days.get(days.size() - 1).getDate().equals(todayDate)) {
                timeWork = days.get(days.size() - 1).getTotalWorkTime();
            }
            if (!Prefs.isWorking() && timeWork == null) {
                return;
            } else {
                timeWork = days.get(days.size() - 1).getTotalWorkTime();
            }

            String time = CalendarUtils.parseTimeToString(timeWork.getHour(), timeWork.getMinute());
            getView().setTodayTimeWorkText(time);
        }
    }
}

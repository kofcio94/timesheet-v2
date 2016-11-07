package japko6.workly.ui.details;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Collections;

import japko6.workly.objects.Day;
import japko6.workly.objects.Time;
import japko6.workly.objects.WorkInterval;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BasePresenter;
import japko6.workly.ui.details.adapter.ItemDetail;
import japko6.workly.utils.CalendarUtils;

public class DetailsListPresenter extends BasePresenter<DetailsListFragment> {

    protected interface View {

        void showEmptyDaysInfo();

        void updateListView();

        void hidePullToRefresh();

        void showWrongDate();

        void startActivityNewDetails();
    }

    private ArrayList<Day> days;

    @Override
    public void onLoad(DetailsListFragment view) {
        super.onLoad(view);
    }

    public void onPullToRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getView().updateListView();
                getView().hidePullToRefresh();
            }
        }, 500);
    }

    protected ArrayList<ItemDetail> getDetailsItems() {
        ArrayList<ItemDetail> itemDetailArray = new ArrayList<>();
        days = Prefs.getDays();

        if (days == null) {
            return itemDetailArray;
        }

        if (Prefs.isWorking()) {
            days.get(days.size() - 1).getWorkIntervals().get(days.get(days.size() - 1).getWorkIntervals().size() - 1).setStopInterval(new Time());
        }

        int positionDay = 0;
        int positionWorkInterval;
        for (Day day : days) {
            String date = CalendarUtils.parseDateToString(day.getDate().getYear(), day.getDate().getMonth(), day.getDate().getDay());
            positionWorkInterval = 0;
            for (WorkInterval workInterval : day.getWorkIntervals()) {
                String sWorkInterval = CalendarUtils.parseWorkIntervalToString(workInterval.getStartInterval(), workInterval.getStopInterval());
                Time time = CalendarUtils.getWorkingTime(workInterval.getStartInterval(), workInterval.getStopInterval());
                String sTime = CalendarUtils.parseTimeToString(time.getHour(), time.getMinute());

                ItemDetail itemDetail = new ItemDetail(date, sWorkInterval, sTime, day.getDescription());
                itemDetail.setWorkIntervalPosition(positionWorkInterval);
                itemDetail.setDaysPosition(positionDay);
                itemDetailArray.add(itemDetail);

                positionWorkInterval++;
            }

            positionDay++;
        }
        Collections.reverse(itemDetailArray);
        return itemDetailArray;
    }


    protected void onDeleteConfirmed(ItemDetail itemDetail) {

    }

    protected void onButtonAddClicked() {
        getView().startActivityNewDetails();
    }

}

package japko6.workly.objects;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import japko6.workly.utils.CalendarUtils;

public class Day {

    public static final String DESC_VACATION = "Vacation";
    public static final String DESC_GPS_START = "GPS";
    public static final String DESC_NORMAL_START = "Normal";

    private ArrayList<WorkInterval> workIntervals;
    private DateKey date;

    @Nullable
    private String description;

    public Day() {
        this.workIntervals = null;
        this.date = new DateKey();
    }

    public Day(int year, int month, int day, String description) {
        this.workIntervals = null;
        date = new DateKey(year, month, day);
        this.description = description;
    }

    public Day(DateKey date, String description) {
        this.workIntervals = new ArrayList<>();
        this.date = date;
        this.description = description;
    }

    public ArrayList<WorkInterval> getWorkIntervals() {
        return workIntervals;
    }

    public void setWorkIntervals(ArrayList<WorkInterval> workIntervals) {
        this.workIntervals = workIntervals;
    }

    public DateKey getDate() {
        return date;
    }

    public void setDate(DateKey date) {
        this.date = date;
    }

    public Time getTotalWorkTime() {
        Time workTime = new Time(0, 0);
        for (WorkInterval workInterval : workIntervals) {
            if (workInterval.startInterval != null && workInterval.stopInterval != null) {
                workTime.addTime(CalendarUtils.getWorkingTime(
                        workInterval.getStartInterval().getHour(),
                        workInterval.getStartInterval().getMinute(),
                        workInterval.getStopInterval().getHour(),
                        workInterval.getStopInterval().getMinute()
                ));
            } else if (workInterval.startInterval != null) {
                workTime.addTime(CalendarUtils.getWorkingTime(
                        workInterval.getStartInterval().getHour(),
                        workInterval.getStartInterval().getMinute(),
                        new Time().getHour(),
                        new Time().getMinute()
                ));
            } else {
                return workTime;
            }
        }

        return workTime;
    }

    public float getWorkTimeInFloat() {
        Time t = getTotalWorkTime();
        float h;
        float m;
        h = t.getHour();
        m = t.getMinute();

        return h + m / 60;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }
}

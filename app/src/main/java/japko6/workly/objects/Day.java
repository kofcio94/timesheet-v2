package japko6.workly.objects;

import java.util.ArrayList;

import japko6.workly.utils.CalendarUtils;

public class Day {

    public static final String DESC_VACATION = "Vacation";
    public static final String DESC_GPS = "GPS";
    public static final String DESC_NORMAL = "Normal";

    private ArrayList<WorkInterval> workIntervals;
    private DateKey date;

    public Day() {
        this.workIntervals = null;
        this.date = new DateKey();
    }

    public Day(int year, int month, int day) {
        this.workIntervals = null;
        date = new DateKey(year, month, day);
    }

    public Day(DateKey date) {
        this.workIntervals = new ArrayList<>();
        this.date = date;
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
}

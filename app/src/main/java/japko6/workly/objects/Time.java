package japko6.workly.objects;


import japko6.workly.utils.CalendarUtils;

public class Time {
    private int hour;
    private int minute;

    public Time() {
        this.hour = CalendarUtils.getActualHour();
        this.minute = CalendarUtils.getActualMinute();
    }

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void addTime(Time w) {
        this.hour += w.getHour();
        if (w.getMinute() + this.minute < 60) {
            this.minute += w.getMinute();
        } else {
            int hoursFromMinute = (w.getMinute() + this.minute) / 60;
            this.hour += hoursFromMinute;
            this.minute += (w.getMinute() % 60) - 60;
        }
    }

    @Override
    public boolean equals(Object o) {
        Time a = (Time) o;
        return this.getHour() == a.getHour() && this.getMinute() == ((Time) o).getMinute();
    }
}

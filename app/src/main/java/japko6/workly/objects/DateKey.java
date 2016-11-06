package japko6.workly.objects;


import japko6.workly.utils.CalendarUtils;

public class DateKey {

    private int year;
    private int month;
    private int day;

    public DateKey(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DateKey() {
        this.year = CalendarUtils.getActualYear();
        this.month = CalendarUtils.getActualMonth();
        this.day = CalendarUtils.getActualDay();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        DateKey b = (DateKey) o;
        return this.getDay() == b.getDay()
                && this.getMonth() == b.getMonth()
                && this.getYear() == b.getYear();
    }

    public boolean isGreater(DateKey d) {
        if (d.getMonth() > this.year) {
            return true;
        } else if (d.getMonth() > this.month) {
            return true;
        } else if (d.getDay() > this.day) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLower(DateKey d) {
        if (d.getMonth() < this.year) {
            return true;
        } else if (d.getMonth() < this.month) {
            return true;
        } else if (d.getDay() < this.day) {
            return true;
        } else {
            return false;
        }
    }

    public void setNextDateKey() {
        int monthLength = CalendarUtils.getMonthLength(this.getMonth());
        if (this.getDay() < monthLength) {
            this.day++;
        } else {
            this.day = 1;
            this.month++;
            if (month >= 12) {
                this.year++;
                month = 0;
            }
        }
    }
}

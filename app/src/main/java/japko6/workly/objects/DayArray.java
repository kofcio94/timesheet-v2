package japko6.workly.objects;

import android.support.annotation.Nullable;

import java.util.ArrayList;

public class DayArray {
    private ArrayList<Day> days;

    public DayArray(@Nullable ArrayList<Day> days) {
        this.days = days;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }
}

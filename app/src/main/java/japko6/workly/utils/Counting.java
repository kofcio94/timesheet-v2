package japko6.workly.utils;

import java.util.ArrayList;

import japko6.workly.objects.DateKey;
import japko6.workly.objects.Day;
import japko6.workly.objects.Time;
import japko6.workly.objects.WorkInterval;
import japko6.workly.prefs.Prefs;

public class Counting {

    public static void run(DateKey actualDate, Time actualTime) {
        ArrayList<Day> days = Prefs.getDays();
        ArrayList<WorkInterval> workIntervals;

        if (days == null || days.size() == 0) {
            days = new ArrayList<>();

            workIntervals = new ArrayList<>();
            workIntervals.add(new WorkInterval(actualTime, null));

            Day actualDay = new Day();
            actualDay.setWorkIntervals(workIntervals);
            days.add(actualDay);

            Prefs.setDays(days);
        } else {
            if (days.get(days.size() - 1).getDate().equals(actualDate)) {

                workIntervals = days.get(days.size() - 1).getWorkIntervals();
                workIntervals.add(new WorkInterval(actualTime, null));

                days.get(days.size() - 1).setWorkIntervals(workIntervals);
                Prefs.setDays(days);

            } else {
                Day newDay = new Day();

                workIntervals = new ArrayList<>();
                workIntervals.add(new WorkInterval(actualTime, null));

                newDay.setWorkIntervals(workIntervals);
                Prefs.addDay(newDay);
            }
        }
        Prefs.setWorkingProcess(true);
    }

    public static void interrupt(Time actualTime) {
        ArrayList<Day> days = Prefs.getDays();
        ArrayList<WorkInterval> workIntervals;

        if (days == null) {
            return;
        } else {
            if (days.get(days.size() - 1).getWorkIntervals().get(days.get(days.size() - 1).getWorkIntervals().size() - 1).getStopInterval() == null) {
                days.get(days.size() - 1).getWorkIntervals().get(days.get(days.size() - 1).getWorkIntervals().size() - 1).setStopInterval(new Time());
            }
            if (days.get(days.size() - 1).getDate().equals(new DateKey())) {
                workIntervals = days.get(days.size() - 1).getWorkIntervals();
                workIntervals.get(workIntervals.size() - 1).setStopInterval(actualTime);

                days.get(days.size() - 1).setWorkIntervals(workIntervals);

                Prefs.setDays(days);
            } else {
                days.get(days.size() - 1).getWorkIntervals().get(days.get(days.size() - 1).getWorkIntervals().size() - 1).setStopInterval(new Time(23, 59));
                days.add(new Day());
                ArrayList<WorkInterval> nextDayWorkIntervals = new ArrayList<>();
                nextDayWorkIntervals.add(new WorkInterval(new Time(0, 0), new Time()));
                days.get(days.size() - 1).setWorkIntervals(nextDayWorkIntervals);

                Prefs.setDays(days);
            }
        }

        Prefs.setWorkingProcess(false);
    }
}

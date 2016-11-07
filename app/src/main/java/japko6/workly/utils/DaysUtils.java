package japko6.workly.utils;

import java.util.ArrayList;

import japko6.workly.objects.DateKey;
import japko6.workly.objects.Day;


public class DaysUtils {

    private static int index;

    public static ArrayList<Day> cleanUpDaysArray(ArrayList<Day> days) {
        days = sortDays(days);

        return days;
    }

    private static ArrayList<Day> sortDays(ArrayList<Day> unsortedArrayList) {
        ArrayList<Day> sortedArrayList = new ArrayList<>();
        index = 0;
        while (unsortedArrayList.size() > 0) {
            sortedArrayList.add(getLowestDay(unsortedArrayList));
            unsortedArrayList.remove(index);
        }
        return sortedArrayList;
    }

    private static Day getLowestDay(ArrayList<Day> days) {
        Day day = new Day(new DateKey(20999, 113, 113));
        for (int i = 0; i < days.size(); i++) {
            try {
                if (SortUtils.isLeftGreaterThanRight(day.getDate(), days.get(i).getDate())) {
                    day = days.get(i);
                    index = i;
                }
            } catch (Exception e) {
                Logs.d("DAY UTILS", "EQUALS");
            }
        }

        return day;
    }
}

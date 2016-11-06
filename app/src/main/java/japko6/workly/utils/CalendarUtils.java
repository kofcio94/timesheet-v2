package japko6.workly.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

import japko6.workly.R;
import japko6.workly.objects.DateKey;
import japko6.workly.objects.Day;
import japko6.workly.objects.Time;
import japko6.workly.objects.WorkInterval;


public class CalendarUtils {

    public static int getActualYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getActualDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getActualMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static int getMonthLength(int month) {
        int length;
        switch (month) {
            case 0:
                length = 31;
                break;
            case 1:
                if (isLeapYear(getActualYear())) {
                    length = 29;
                } else length = 28;
                break;
            case 2:
                length = 31;
                break;
            case 3:
                length = 30;
                break;
            case 4:
                length = 31;
                break;
            case 5:
                length = 30;
                break;
            case 6:
                length = 31;
                break;
            case 7:
                length = 31;
                break;
            case 8:
                length = 30;
                break;
            case 9:
                length = 31;
                break;
            case 10:
                length = 30;
                break;
            case 11:
                length = 31;
                break;
            default:
                length = 0;
                break;
        }
        return length;
    }

    public static int getActualMonthLength() {
        int month = getActualMonth();
        return getMonthLength(month);
    }

    public static int getActualHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getActualMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static Time getWorkingTime(int startHour, int startMinute, int stopHour, int stopMinute) {
        Time timeWork = new Time();

        if (startHour > stopHour) {
            timeWork.setHour(startHour - stopHour);
        } else {
            timeWork.setHour(stopHour - startHour);
        }

        if (startMinute > stopMinute) {
            int minuteToSet;
            minuteToSet = 60 + (stopMinute - startMinute);
            timeWork.setMinute(minuteToSet);
            timeWork.setHour(timeWork.getHour() - 1);
        } else {
            timeWork.setMinute(stopMinute - startMinute);
        }

        return timeWork;
    }

    public static Time getWorkingTime(Time start, Time stop) {
        Time timeWork = new Time(0, 0);

        if (start.getHour() > stop.getHour()) {
            timeWork.setHour(start.getHour() - stop.getHour());
        } else {
            timeWork.setHour(stop.getHour() - start.getHour());
        }

        if (start.getMinute() > stop.getMinute()) {
            int minuteToSet;
            minuteToSet = 60 + (stop.getMinute() - start.getMinute());
            timeWork.setMinute(minuteToSet);
            timeWork.setHour(timeWork.getHour() - 1);
        } else {
            timeWork.setMinute(stop.getMinute() - start.getMinute());
        }

        return timeWork;
    }

    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    public static String parseDateToString(int year, int month, int day) {
        String sYear, sMonth, sDay;

        if (day < 10) {
            sDay = "0" + String.valueOf(day);
        } else {
            sDay = String.valueOf(day);
        }

        if (month + 1 < 10) {
            sMonth = "0" + String.valueOf(month + 1);
        } else {
            sMonth = String.valueOf(month + 1);
        }

        sYear = String.valueOf(year);

        return sDay + "." + sMonth + "." + sYear;
    }

    public static String parseDateToString(DateKey dateKey) {
        String sYear, sMonth, sDay;

        if (dateKey.getDay() < 10) {
            sDay = "0" + String.valueOf(dateKey.getDay());
        } else {
            sDay = String.valueOf(dateKey.getDay());
        }

        if (dateKey.getMonth() + 1 < 10) {
            sMonth = "0" + String.valueOf(dateKey.getMonth() + 1);
        } else {
            sMonth = String.valueOf(dateKey.getMonth() + 1);
        }

        sYear = String.valueOf(dateKey.getYear());

        return sDay + "." + sMonth + "." + sYear;
    }


    public static String parseTimeToString(int hour, int minute) {
        String sHour;
        String sMinute;

        if (minute < 10) {
            sMinute = "0" + String.valueOf(minute);
        } else {
            sMinute = String.valueOf(minute);
        }

        if (hour < 10) {
            sHour = "0" + String.valueOf(hour);
        } else {
            sHour = String.valueOf(hour);
        }
        return sHour + " : " + sMinute;
    }

    public static String parseWorkIntervalToString(Time start, Time stop) {
        String sStart = parseTimeToString(start.getHour(), start.getMinute());
        String sStop = parseTimeToString(stop.getHour(), stop.getMinute());

        return sStart + " - " + sStop;
    }


    public static Time getAvgWorkTime(ArrayList<Day> days) {
        Time totalTime = getTotalTime(days);

        int minutes = totalTime.getHour() * 60 + totalTime.getMinute();
        minutes /= days.size();

        Time avgTime = new Time(0, 0);
        avgTime.setHour(minutes / 60);
        avgTime.setMinute(minutes % 60);

        return avgTime;
    }

    public static float getAvgWorkTimeFloat(ArrayList<Day> days) {
        Time avg = getAvgWorkTime(days);
        float h;
        float m;
        h = avg.getHour();
        m = avg.getMinute();

        return h + m / 60;
    }

    public static Time getTotalTime(ArrayList<Day> days) {
        Time totalTime = new Time(0, 0);
        for (Day day : days) {
            totalTime.addTime(day.getTotalWorkTime());
        }

        return totalTime;
    }

    public static String prepareReportString(ArrayList<Day> days, Context context) {
        if (days == null) {
            return null;
        }

        Time workTime = new Time(0, 0);
        String report = "";
        for (Day day : days) {
            int mDay = day.getDate().getDay();
            int mMonth = day.getDate().getMonth();
            int mYear = day.getDate().getYear();

            report += parseDateToString(mYear, mMonth, mDay) + "\n";

            for (WorkInterval workInterval : day.getWorkIntervals()) {
                report += "       " +
                        parseTimeToString(workInterval.getStartInterval().getHour(), workInterval.getStartInterval().getMinute())
                        + "  -  " +
                        parseTimeToString(workInterval.getStopInterval().getHour(), workInterval.getStopInterval().getMinute())
                        + "\n";
            }
            report += "\n       " + context.getString(R.string.mail_fragment_time_work_mail) + " "
                    + parseTimeToString(day.getTotalWorkTime().getHour(), day.getTotalWorkTime().getMinute()) + "\n\n\n";
            workTime.addTime(day.getTotalWorkTime());
        }

        report += "\n\n______________________\n" + context.getString(R.string.mail_fragment_time_work_total_mail) + " "
                + parseTimeToString(workTime.getHour(), workTime.getMinute());
        return report;
    }
}


















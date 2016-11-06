package japko6.workly.prefs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import japko6.workly.objects.Day;
import japko6.workly.objects.DayArray;
import japko6.workly.objects.LocationArray;
import japko6.workly.objects.LocationItem;
import japko6.workly.services.ReminderService;
import japko6.workly.ui.main.MainActivity;
import japko6.workly.ui.stats.StatsFragment;
import japko6.workly.utils.DaysUtils;

public class Prefs {

    private static final String
            PREFS_NAME = "BlockerPrefs",
            KEY_IS_WORKING = "key_is_working",
            KEY_REQUIRE_PASSWORD = "key_require_password",
            KEY_PASSWORD = "key_password",
            KEY_EMAIL_TO_SEND = "key_email_to_send",
            KEY_DAYS_LIST = "key_days_list",
            KEY_GPS_MODE = "key_gps_mode",
            KEY_LOCATION_LIST = "key_location_list",
            KEY_SERVICE_LOCATION_TRACKING_ENABLED = "key_service_location_tracking",
            KEY_ADVANCED_ANIMATIONS = "key_advanced_animations",
            KEY_CHART_DAYS = "key_chart_days",
            KEY_WORK_NOTIFICATION = "key_work_notification",
            KEY_IS_REMINDER_WORKING = "key_is_reminder_working";

    private static ComplexPreferences complexPrefs;

    /**
     * Use this function on start app (Application onCreate)
     *
     * @param context
     */
    public static void initializeComplexPrefs(Context context) {
        if (complexPrefs == null) {
            complexPrefs = ComplexPreferences.getComplexPreferences(context, PREFS_NAME);
        }
    }

    public static void clearPrefs() {
        setPasswordRequire(false);
        setPassword("");
        setGPSMode(false);
        setServiceLocationTrackingEnabled(false);
        complexPrefs.commit();
    }

    public static void addDay(@NonNull Day day) {
        ArrayList<Day> days = getDays();
        if (days == null) {
            days = new ArrayList<>();
        }
        days.add(day);
        DayArray d = new DayArray(days);
        complexPrefs.putObject(KEY_DAYS_LIST, d);
        complexPrefs.commit();
    }

    public static void setDays(ArrayList<Day> days) {
        DayArray d = new DayArray(DaysUtils.cleanUpDaysArray(days));
        if (d.getDays().size() == 0) {
            d.setDays(null);
        }
        complexPrefs.putObject(KEY_DAYS_LIST, d);
        complexPrefs.commit();
        if (StatsFragment.daysChangeListener != null)
            StatsFragment.daysChangeListener.onNewDayAdded();
    }

    public static ArrayList<Day> getDays() {
        try {
            DayArray d = complexPrefs.getObject(KEY_DAYS_LIST, DayArray.class);
            if (d == null) {
                return null;
            }
            ArrayList<Day> days = d.getDays();
            if (days == null) {
                return null;
            }
            return days;
        } catch (Exception e) {
            return null;
        }
    }

    public static void setPasswordRequire(boolean require) {
        complexPrefs.putObject(KEY_REQUIRE_PASSWORD, require);
        complexPrefs.commit();
    }

    public static boolean isPasswordRequired() {
        try {
            Boolean isReg = complexPrefs.getObject(KEY_REQUIRE_PASSWORD, Boolean.class);
            if (isReg == null) {
                return false;
            }
            return isReg;
        } catch (Exception e) {
            return false;
        }
    }


    public static void setWorkingProcess(boolean isWorking) {
        complexPrefs.putObject(KEY_IS_WORKING, isWorking);
        complexPrefs.commit();
        if (isWorking) {
            if (MainActivity.countingListener != null)
                MainActivity.countingListener.onBegin();
        } else {
            if (MainActivity.countingListener != null)
                MainActivity.countingListener.onNotWorking();
            if (ReminderService.countingListener != null)
                ReminderService.countingListener.onNotWorking();
        }
    }

    public static boolean isWorking() {
        try {
            Boolean isReg = complexPrefs.getObject(KEY_IS_WORKING, Boolean.class);
            if (isReg == null) {
                return false;
            }
            return isReg;
        } catch (Exception e) {
            return false;
        }
    }

    public static void setAdvancedAnimations(boolean advancedAnimations) {
        complexPrefs.putObject(KEY_ADVANCED_ANIMATIONS, advancedAnimations);
        complexPrefs.commit();
    }

    public static boolean areAdvancedAnimationsEnabled() {
        try {
            Boolean adv = complexPrefs.getObject(KEY_ADVANCED_ANIMATIONS, Boolean.class);

            if (adv == null) {
                return true;
            }

            return adv;
        } catch (Exception e) {
            return true;
        }
    }

    public static String getEmail() {
        try {
            String email = complexPrefs.getObject(KEY_EMAIL_TO_SEND, String.class);

            if (email == null) {
                return "";
            }
            return email;
        } catch (Exception e) {
            return "";
        }
    }

    public static void setEmail(String email) {
        complexPrefs.putObject(KEY_EMAIL_TO_SEND, email);
        complexPrefs.commit();
    }

    public static void setPassword(String password) {
        complexPrefs.putObject(KEY_PASSWORD, password);
        complexPrefs.commit();
    }

    public static String getPassword() {
        try {
            String password = complexPrefs.getObject(KEY_PASSWORD, String.class);
            if (password == null) {
                return "";
            }
            return password;
        } catch (Exception e) {
            return "";
        }
    }

    public static void setChartDays(int amount) {
        complexPrefs.putObject(KEY_CHART_DAYS, amount);
        complexPrefs.commit();
    }

    public static int getChartDays() {
        try {
            Integer amount = complexPrefs.getObject(KEY_CHART_DAYS, Integer.class);
            if (amount == null) {
                return 30;
            }
            if (amount == 0) {
                amount = 1;
            }
            return amount;
        } catch (Exception e) {
            return 30;
        }
    }

    public static void setWorkNotification(boolean workNotification) {
        complexPrefs.putObject(KEY_WORK_NOTIFICATION, workNotification);
        complexPrefs.commit();
    }

    public static boolean isWorkNotificationEnabled() {
        try {
            Boolean enabled = complexPrefs.getObject(KEY_WORK_NOTIFICATION, Boolean.class);
            if (enabled == null) {
                return true;
            }
            return enabled;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * IF true - MODE AUTO
     * IF false - MODE NOTIFICATIONS
     *
     * @param b
     */
    public static void setGPSMode(boolean b) {
        complexPrefs.putObject(KEY_GPS_MODE, b);
        complexPrefs.commit();
    }

    /**
     * IF true - MODE AUTO
     * IF false - MODE NOTIFICATIONS
     */
    public static boolean getGPSMode() {
        try {
            Boolean mode = complexPrefs.getObject(KEY_GPS_MODE, Boolean.class);
            if (mode == null) {
                return false;
            }
            return mode;
        } catch (Exception e) {
            return false;
        }
    }

    public static void setServiceLocationTrackingEnabled(boolean b) {
        complexPrefs.putObject(KEY_SERVICE_LOCATION_TRACKING_ENABLED, b);
        complexPrefs.commit();
    }

    public static boolean getServiceLocationTrackingEnabled() {
        try {
            Boolean mode = complexPrefs.getObject(KEY_SERVICE_LOCATION_TRACKING_ENABLED, Boolean.class);
            if (mode == null) {
                return false;
            }
            return mode;
        } catch (Exception e) {
            return false;
        }
    }

    public static void setLocationsList(@Nullable ArrayList<LocationItem> locationList) {
        LocationArray locationArray;
        if (locationList == null) {
            locationList = new ArrayList<>();
        }
        locationArray = new LocationArray(locationList);
        complexPrefs.putObject(KEY_LOCATION_LIST, locationArray);
        complexPrefs.commit();
    }

    @Nullable
    public static ArrayList<LocationItem> getLocationsList() {
        LocationArray d = complexPrefs.getObject(KEY_LOCATION_LIST, LocationArray.class);
        ArrayList<LocationItem> locationItems = new ArrayList<>();
        if (d == null) {
            return locationItems;
        }

        locationItems = d.getLocationItems();
        if (locationItems == null) {
            return null;
        }
        return locationItems;
    }
}

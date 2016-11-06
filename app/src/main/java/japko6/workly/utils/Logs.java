package japko6.workly.utils;

import android.util.Log;

import japko6.workly.core.App;

public class Logs {
    public static void d(String tag, String message) {
        if (App.IS_TEST_ENV) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (App.IS_TEST_ENV) {
            Log.e(tag, message);
        }
    }
}

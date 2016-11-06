package japko6.workly.core;

import android.app.Application;

import japko6.workly.prefs.Prefs;

public class App extends Application {

    public static boolean IS_TEST_ENV = true;

    @Override
    public void onCreate() {
        Prefs.initializeComplexPrefs(this);
        super.onCreate();
    }
}

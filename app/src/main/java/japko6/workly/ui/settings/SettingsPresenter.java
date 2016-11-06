package japko6.workly.ui.settings;

import android.content.Intent;
import android.text.TextUtils;

import japko6.workly.prefs.Prefs;
import japko6.workly.services.ReminderService;
import japko6.workly.ui.base.BasePresenter;
import japko6.workly.utils.ReminderStarter;

public class SettingsPresenter extends BasePresenter<SettingsFragment> {

    protected interface view {

        void showToShortPasswordInfo();

        void showWrongPasswordInfo();

        void showSavedInfo();

        void setSwitchState(boolean isPassReq);

        void startLocationActivity();

        void tooShortPassword();

    }

    @Override
    public void onLoad(SettingsFragment view) {
        super.onLoad(view);
    }

    protected void onLocationPressed() {
        getView().startLocationActivity();
    }

    protected void setRequire(boolean require) {
        if (TextUtils.isEmpty(Prefs.getPassword())) {
            getView().tooShortPassword();
            return;
        }
        Prefs.setPasswordRequire(require);
    }

    public void onCheckedEffects(boolean b) {
        Prefs.setAdvancedAnimations(b);
    }

    public void onCheckedReminder(boolean b) {
        Prefs.setWorkNotification(b);
        ReminderStarter.startStopReminderService(getView().getContext());
    }
}



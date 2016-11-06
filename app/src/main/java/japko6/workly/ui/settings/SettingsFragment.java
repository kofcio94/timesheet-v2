package japko6.workly.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kyleduo.switchbutton.SwitchButton;

import at.markushi.ui.CircleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import japko6.workly.R;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BaseFragment;
import japko6.workly.ui.gps.gpsSettings.LocationSettingsActivity;

public class SettingsFragment extends BaseFragment implements SettingsPresenter.view {

    private SettingsPresenter presenter;
    private ViewHolder viewHolder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        try {
            if (!Prefs.areAdvancedAnimationsEnabled()) {
                root.findViewById(R.id.moving_background).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolderSetUp(root);
        presenterSetUp();
        setUpOnClickListeners();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewHolder.mSwOnOffPassword.setCheckedNoEvent(Prefs.isPasswordRequired());
        viewHolder.password.setText(Prefs.getPassword());
        viewHolder.mSwOnOffEffects.setCheckedNoEvent(Prefs.areAdvancedAnimationsEnabled());
        viewHolder.mSwOnOffReminder.setCheckedNoEvent(Prefs.isWorkNotificationEnabled());
    }

    @Override
    protected void viewHolderSetUp(View view) {
        viewHolder = new ViewHolder(view);
    }

    private void setUpOnClickListeners() {
        viewHolder.mIvEditLocation.setOnClickListener(listener);
        viewHolder.mLlGps.setOnClickListener(listener);
        viewHolder.mSwOnOffPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                presenter.setRequire(b);
            }
        });
        viewHolder.mSwOnOffEffects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                presenter.onCheckedEffects(b);
            }
        });
        viewHolder.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 4) {
                    Prefs.setPassword(editable.toString());
                } else {
                    Prefs.setPassword("");
                    Prefs.setPasswordRequire(false);
                    viewHolder.mSwOnOffPassword.setCheckedNoEvent(false);
                }
            }
        });
        viewHolder.mSwOnOffReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                presenter.onCheckedReminder(b);
            }
        });
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == viewHolder.mIvEditLocation) {
                viewHolder.mLlGps.performClick();
            } else if (v == viewHolder.mLlGps) {
                presenter.onLocationPressed();
            } else {
                showInDevToast();
            }
        }
    };

    @Override
    protected void presenterSetUp() {
        presenter = new SettingsPresenter();
        presenter.onLoad(this);
    }

    @Override
    public void showToShortPasswordInfo() {
        showNegativeToast(R.string.to_short_password);
    }

    @Override
    public void showWrongPasswordInfo() {
        showNegativeToast(R.string.settings_fragment_dialog_wrong_password);
    }

    @Override
    public void showSavedInfo() {
        showPositiveToast(R.string.global_saved);
    }

    @Override
    public void setSwitchState(boolean isPassReq) {
        viewHolder.mSwOnOffPassword.setCheckedNoEvent(Prefs.isPasswordRequired());
    }

    @Override
    public void startLocationActivity() {
        Intent intent = new Intent(getActivity(), LocationSettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void tooShortPassword() {
        viewHolder.mSwOnOffPassword.setCheckedNoEvent(false);
        showNegativeToast(R.string.to_short_password);
    }

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.fragment_settings__iv_location_edit)
        CircleButton mIvEditLocation;

        @Bind(R.id.fragment_settings__sw_password_on_off)
        SwitchButton mSwOnOffPassword;

        @Bind(R.id.settings_ll_location)
        LinearLayout mLlGps;

        @Bind(R.id.fragment_settings_et_password)
        EditText password;

        @Bind(R.id.fragment_settings__sw_effects_on_off)
        SwitchButton mSwOnOffEffects;

        @Bind(R.id.fragment_settings__sw_reminder_on_off)
        SwitchButton mSwOnOffReminder;
    }
}

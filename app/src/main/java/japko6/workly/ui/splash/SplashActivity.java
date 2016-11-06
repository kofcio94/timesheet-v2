package japko6.workly.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.nightonke.blurlockview.BlurLockView;
import com.nightonke.blurlockview.Directions.HideType;
import com.nightonke.blurlockview.Directions.ShowType;
import com.nightonke.blurlockview.Eases.EaseType;
import com.nightonke.blurlockview.Password;

import japko6.workly.R;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            if (!Prefs.areAdvancedAnimationsEnabled()) {
                findViewById(R.id.moving_background).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkPassword();
    }

    private void checkPassword() {
        final BlurLockView blurLockView = (BlurLockView) findViewById(R.id.blurlockview);
        if (Prefs.isPasswordRequired() && !TextUtils.isEmpty(Prefs.getPassword())) {
            blurLockView.setOverlayColor(ContextCompat.getColor(SplashActivity.this, R.color.colorPrimaryDark));
            blurLockView.setCorrectPassword(Prefs.getPassword());
            blurLockView.setType(Password.NUMBER, true);
            blurLockView.setOnPasswordInputListener(new BlurLockView.OnPasswordInputListener() {
                @Override
                public void correct(String inputPassword) {
                    hideSplash();
                    blurLockView.hide(300, HideType.FADE_OUT, EaseType.Linear);
                }

                @Override
                public void incorrect(String inputPassword) {
                    Toast.makeText(SplashActivity.this, R.string.password_incorrect, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void input(String inputPassword) {

                }
            });
            blurLockView.setBlurredView(findViewById(R.id.iv_splash));
            blurLockView.setLeftButton("");
            blurLockView.setRightButton(getString(R.string.delete));
            blurLockView.setTitle(getString(R.string.enter_password));
            blurLockView.setVisibility(View.VISIBLE);
            blurLockView.show(1000, ShowType.FADE_IN, EaseType.Linear);
        } else {
            hideSplash();
        }


    }

    private void hideSplash() {
        new CountDownTimer(3000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                startMainActivity();
            }
        }.start();
    }


    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        SplashActivity.this.startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}

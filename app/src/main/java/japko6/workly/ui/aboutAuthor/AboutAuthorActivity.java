package japko6.workly.ui.aboutAuthor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import japko6.workly.R;
import japko6.workly.prefs.Prefs;

public class AboutAuthorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_author);
        try {
            if (!Prefs.areAdvancedAnimationsEnabled()) {
                findViewById(R.id.moving_background).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        findViewById(R.id.bt_back_cv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
}

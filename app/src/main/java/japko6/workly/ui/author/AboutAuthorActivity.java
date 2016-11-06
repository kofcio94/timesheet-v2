package japko6.workly.ui.author;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import japko6.workly.R;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BaseActivity;

public class AboutAuthorActivity extends BaseActivity {

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
    }


    @Override
    protected void viewHolderSetUp() {

    }

    @Override
    protected void presenterSetUp() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
}

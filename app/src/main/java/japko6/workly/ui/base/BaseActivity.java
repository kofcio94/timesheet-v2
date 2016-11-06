package japko6.workly.ui.base;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.javiersantos.bottomdialogs.BottomDialog;

import japko6.workly.R;
import japko6.workly.widgets.CustomToast;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle("");
            supportActionBar.setSubtitle("");
        }
    }

    protected abstract void viewHolderSetUp();

    protected abstract void presenterSetUp();

    protected View getActivityView() {
        return findViewById(android.R.id.content);
    }

    protected void showNegativeToast(String text) {
        CustomToast.showNegativeToast(text, this);
    }

    protected void showNegativeToast(int resourceId) {
        CustomToast.showNegativeToast(resourceId, this);
    }

    protected void showPositiveToast(String text) {
        CustomToast.showPositiveToast(text, this);
    }

    protected void showPositiveToast(int resourceId) {
        CustomToast.showPositiveToast(resourceId, this);
    }

    protected void showNegativeToast(String text, boolean immediatelyShowToast) {
        CustomToast.showNegativeToast(text, immediatelyShowToast, this);
    }

    protected void showNegativeToast(int resourceId, boolean immediatelyShowToast) {
        CustomToast.showNegativeToast(resourceId, immediatelyShowToast, this);
    }

    protected void showPositiveToast(String text, boolean immediatelyShowToast) {
        CustomToast.showPositiveToast(text, immediatelyShowToast, this);
    }

    protected void showPositiveToast(int resourceId, boolean immediatelyShowToast) {
        CustomToast.showPositiveToast(resourceId, immediatelyShowToast, this);
    }

    protected void showInDevToast() {
        Toast.makeText(this, R.string.in_dev, Toast.LENGTH_SHORT).show();
    }

    protected void showSimpleDialog(String content) {
        BottomDialog bottomDialog = new BottomDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setContent(content)
                .setCancelable(false)
                .setPositiveText(R.string.ok)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        bottomDialog.dismiss();
                    }
                })
                .build();
        bottomDialog.show();
    }

    protected void showSimpleDialog(int content) {
        showSimpleDialog(getString(content));
    }
}

package japko6.workly.ui.base;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.github.javiersantos.bottomdialogs.BottomDialog;

import japko6.workly.R;
import japko6.workly.widgets.CustomToast;

public abstract class BaseFragment extends Fragment {

    protected abstract void viewHolderSetUp(View view);

    protected abstract void presenterSetUp();

    protected void showNegativeToast(String text) {
        CustomToast.showNegativeToast(text, getContext());
    }

    protected void showNegativeToast(int resourceId) {
        CustomToast.showNegativeToast(resourceId, getContext());
    }

    protected void showPositiveToast(String text) {
        CustomToast.showPositiveToast(text, getContext());
    }

    protected void showPositiveToast(int resourceId) {
        CustomToast.showPositiveToast(resourceId, getContext());
    }

    protected void showNegativeToast(String text, boolean immediatelyShowToast) {
        CustomToast.showNegativeToast(text, immediatelyShowToast, getContext());
    }

    protected void showNegativeToast(int resourceId, boolean immediatelyShowToast) {
        CustomToast.showNegativeToast(resourceId, immediatelyShowToast, getContext());
    }

    protected void showPositiveToast(String text, boolean immediatelyShowToast) {
        CustomToast.showPositiveToast(text, immediatelyShowToast, getContext());
    }

    protected void showPositiveToast(int resourceId, boolean immediatelyShowToast) {
        CustomToast.showPositiveToast(resourceId, immediatelyShowToast, getContext());
    }

    protected void showInDevToast() {
        Toast.makeText(getContext(), R.string.in_dev, Toast.LENGTH_SHORT).show();
    }

    protected void showSimpleDialog(String content) {
        BottomDialog bottomDialog = new BottomDialog.Builder(getContext())
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

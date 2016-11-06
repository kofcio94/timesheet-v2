package japko6.workly.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import japko6.workly.R;


public class CustomToast {

    private static Toast toast;

    public static void showNegativeToast(String text, Context context) {
        cancelPreviousToast();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.negative_toast));
        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);
        toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void showNegativeToast(int resourceId, Context context) {
        cancelPreviousToast();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.negative_toast));
        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(resourceId);
        toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void showPositiveToast(String text, Context context) {
        cancelPreviousToast();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.positive_toast));
        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);
        toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void showPositiveToast(int resourceId, Context context) {
        cancelPreviousToast();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.positive_toast));
        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(resourceId);
        toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void showNegativeToast(String text, boolean immediatelyShowToast, Context context) {
        if (immediatelyShowToast)
            cancelPreviousToast();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.negative_toast));
        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);
        toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void showNegativeToast(int resourceId, boolean immediatelyShowToast, Context context) {
        if (immediatelyShowToast)
            cancelPreviousToast();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.negative_toast));
        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(resourceId);
        toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void showPositiveToast(String text, boolean immediatelyShowToast, Context context) {
        if (immediatelyShowToast)
            cancelPreviousToast();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.positive_toast));
        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);
        toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void showPositiveToast(int resourceId, boolean immediatelyShowToast, Context context) {
        if (immediatelyShowToast)
            cancelPreviousToast();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.positive_toast));
        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(resourceId);
        toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private static void cancelPreviousToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public static void cancelActualToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}

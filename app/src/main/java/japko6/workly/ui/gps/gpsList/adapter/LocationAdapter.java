package japko6.workly.ui.gps.gpsList.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import japko6.workly.R;
import japko6.workly.objects.LocationItem;

public class LocationAdapter extends ArrayAdapter<LocationItem> {

    private Context context;
    private List<LocationItem> list;
    private int layoutResID;
    private ViewHolder viewHolder;

    public LocationAdapter(Context context, int layoutResID, List<LocationItem> list) {
        super(context, layoutResID, list);
        this.context = context;
        this.list = list;
        this.layoutResID = layoutResID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = ((Activity) context).getLayoutInflater().inflate(layoutResID, parent, false);
            viewHolder = new ViewHolder(view);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (TextUtils.isEmpty(list.get(position).getContent())) {
            viewHolder.content.setVisibility(View.GONE);
        } else {
            viewHolder.content.setText(list.get(position).getContent());
        }
        viewHolder.title.setText(list.get(position).getTitle());

        return view;
    }

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.item_location_tv_content)
        TextView content;

        @Bind(R.id.item_location_tv_title)
        TextView title;
    }
}

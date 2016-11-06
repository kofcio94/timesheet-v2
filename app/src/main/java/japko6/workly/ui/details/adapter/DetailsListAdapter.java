package japko6.workly.ui.details.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import japko6.workly.R;

public class DetailsListAdapter extends ArrayAdapter<ItemDetail> {

    private Context context;
    private List<ItemDetail> itemList;
    private int layoutResID;
    private ViewHolder viewHolder;


    public DetailsListAdapter(Context context, int layoutResourceID, List<ItemDetail> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.itemList = listItems;
        this.layoutResID = layoutResourceID;
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

        viewHolder.date.setText(itemList.get(position).getDate());
        viewHolder.workInterval.setText(itemList.get(position).getWorkInterval());
        viewHolder.time.setText(itemList.get(position).getTime());

        return view;
    }

    static class ViewHolder {

        public ViewHolder(android.view.View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.item_details_date)
        TextView date;

        @Bind(R.id.item_details_time_interval)
        TextView workInterval;

        @Bind(R.id.item_details_time_work)
        TextView time;
    }
}

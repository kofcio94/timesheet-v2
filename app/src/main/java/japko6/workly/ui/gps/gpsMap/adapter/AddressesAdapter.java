package japko6.workly.ui.gps.gpsMap.adapter;

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
import japko6.workly.objects.AddressItem;

public class AddressesAdapter extends ArrayAdapter<AddressItem> {

    private Context context;
    private List<AddressItem> list;
    private int layoutResID;
    private ViewHolder viewHolder;

    public AddressesAdapter(Context context, int layoutResID, List<AddressItem> list) {
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

        String city = list.get(position).getCity();
        String postal = list.get(position).getPostalCode();
        String street = list.get(position).getStreet();

        if (TextUtils.isEmpty(city)) {
            viewHolder.mCity.setVisibility(View.GONE);
            viewHolder.mCityT.setVisibility(View.GONE);
        } else {
            viewHolder.mCity.setText(city);
        }

        if (TextUtils.isEmpty(postal)) {
            viewHolder.mPostal.setVisibility(View.GONE);
            viewHolder.mPostalT.setVisibility(View.GONE);
        } else {
            viewHolder.mPostal.setText(postal);
        }

        if (TextUtils.isEmpty(street)) {
            viewHolder.mStreet.setVisibility(View.GONE);
            viewHolder.mStreetT.setVisibility(View.GONE);
        } else {
            viewHolder.mStreet.setText(street);
        }

        return view;
    }

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.item_adr_city)
        TextView mCity;

        @Bind(R.id.item_adr_postal)
        TextView mPostal;

        @Bind(R.id.item_adr_street)
        TextView mStreet;

        @Bind(R.id.item_adr_city_title)
        TextView mCityT;

        @Bind(R.id.item_adr_postal_title)
        TextView mPostalT;

        @Bind(R.id.item_adr_street_title)
        TextView mStreetT;
    }
}

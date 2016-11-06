package japko6.workly.httpGeo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeoCoderTask extends AsyncTask<String, Void, List<Address>> {

    private final Context context;
    private LocationSearchListener listener;

    public GeoCoderTask(Context context, LocationSearchListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onStart();
    }

    @Override
    protected void onPostExecute(List<Address> list) {
        super.onPostExecute(list);
        listener.onStop();
        if (list != null && list.size() > 0)
            listener.onSearchSuccess(list);
        else listener.onSearchFailureOrEmpty();
    }


    @Override
    protected List<Address> doInBackground(String... params) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = new ArrayList<>();

        try {
            addresses = geocoder.getFromLocationName(params[0], 100);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addresses;
    }
}

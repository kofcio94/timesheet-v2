package japko6.workly.httpGeo;

import android.location.Address;

import java.util.List;

public interface LocationSearchListener {
    void onSearchSuccess(List<Address> list);

    void onSearchFailureOrEmpty();

    void onStart();

    void onStop();
}

package japko6.workly.objects;

import java.util.ArrayList;



public class LocationArray {

    private ArrayList<LocationItem> locationItems;

    public LocationArray(ArrayList<LocationItem> locationItems) {
        this.locationItems = locationItems;
    }

    public ArrayList<LocationItem> getLocationItems() {
        return locationItems;
    }

    public void setLocationItems(ArrayList<LocationItem> locationItems) {
        this.locationItems = locationItems;
    }
}

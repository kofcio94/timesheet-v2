package japko6.workly.objects;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class LocationItem implements Serializable {
    private String content;
    private double w, l;
    private String title;
    private int position;
    private int radius;

    public LocationItem(String title, String content, double w, double l, int radius) {
        this.content = content;
        this.w = w;
        this.l = l;
        this.title = title;
        this.radius = radius;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LatLng getLatLng() {
        return new LatLng(this.w, this.l);
    }

    public void setLatLng(LatLng latLng) {
        this.w = latLng.latitude;
        this.l = latLng.longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}

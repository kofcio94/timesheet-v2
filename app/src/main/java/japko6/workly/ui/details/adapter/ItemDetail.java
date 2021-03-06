package japko6.workly.ui.details.adapter;

import java.io.Serializable;

public class ItemDetail implements Serializable {

    private String date;
    private String workInterval;
    private String time;
    private int workIntervalPosition;
    private int daysPosition;
    private String desc;

    public ItemDetail(String date, String workInterval, String time, String desc) {
        this.date = date;
        this.workInterval = workInterval;
        this.time = time;
        this.daysPosition = -1;
        this.daysPosition = -1;
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWorkInterval() {
        return workInterval;
    }

    public void setWorkInterval(String workInterval) {
        this.workInterval = workInterval;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getWorkIntervalPosition() {
        return workIntervalPosition;
    }

    public void setWorkIntervalPosition(int workIntervalPosition) {
        this.workIntervalPosition = workIntervalPosition;
    }

    public int getDaysPosition() {
        return daysPosition;
    }

    public void setDaysPosition(Integer daysPosition) {
        this.daysPosition = daysPosition;
    }

    public void setDaysPosition(int daysPosition) {
        this.daysPosition = daysPosition;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

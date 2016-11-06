package japko6.workly.objects;

import android.support.annotation.Nullable;

public class WorkInterval {
    public Time startInterval;
    public Time stopInterval;

    public WorkInterval(Time startInterval, @Nullable Time stopInterval) {
        this.startInterval = startInterval;
        this.stopInterval = stopInterval;
    }

    public Time getStartInterval() {
        return startInterval;
    }

    public void setStartInterval(Time startInterval) {
        this.startInterval = startInterval;
    }

    public Time getStopInterval() {
        return stopInterval;
    }

    public void setStopInterval(Time stopInterval) {
        this.stopInterval = stopInterval;
    }
}
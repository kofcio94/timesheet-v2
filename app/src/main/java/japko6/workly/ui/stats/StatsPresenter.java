package japko6.workly.ui.stats;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import japko6.workly.R;
import japko6.workly.objects.Day;
import japko6.workly.objects.Time;
import japko6.workly.objects.WorkInterval;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BasePresenter;
import japko6.workly.utils.CalendarUtils;

public class StatsPresenter extends BasePresenter<StatsFragment> {

    protected interface View {
        void initChart();
    }

    @Override
    public void onLoad(StatsFragment view) {
        super.onLoad(view);
    }

    public CombinedData getChartData(Context context) {
        boolean example = false;
        ArrayList<Day> days = Prefs.getDays();

        if (days == null || days.size() == 0) {
            days = new ArrayList<>();
            for (int i = 0; i < Prefs.getChartDays(); i++) {
                Day day = new Day();
                ArrayList<WorkInterval> workIntervals = new ArrayList<>();
                int randHStart, randMStart, randHStop, randMStop;
                randHStart = getRandomFromRange(7, 11);
                randHStop = getRandomFromRange(14, 20);
                randMStart = getRandomFromRange(0, 59);
                randMStop = getRandomFromRange(0, 59);
                workIntervals.add(new WorkInterval(new Time(randHStart, randMStart), new Time(randHStop, randMStop)));
                day.setWorkIntervals(workIntervals);
                days.add(day);
            }
            example = true;
        }
        Collections.reverse(days);

        List<BarEntry> entries = new ArrayList<>();
        List<Entry> entriesLine = new ArrayList<>();

        int j = Prefs.getChartDays() - 1;
        ArrayList<Day> daysTmp = new ArrayList<>();
        for (int i = 0; i < Prefs.getChartDays(); i++) {
            try {
                daysTmp.add(days.get(j));
                float YAverage = CalendarUtils.getAvgWorkTimeFloat(daysTmp);
                entries.add(new BarEntry(i + 1, days.get(j).getWorkTimeInFloat()));
                entriesLine.add(new Entry(i + 1, YAverage));
            } catch (Exception e) {
                e.printStackTrace();
            }
            j--;
        }

        BarDataSet dataSet = new BarDataSet(entries, context.getString(R.string.working_days));
        dataSet.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        dataSet.setHighlightEnabled(false);
        dataSet.setDrawValues(true);
        LineDataSet dataSetLine;
        if (example) {
            dataSetLine = new LineDataSet(entriesLine, context.getString(R.string.example));
        } else {
            dataSetLine = new LineDataSet(entriesLine, context.getString(R.string.average));
        }

        LineData lineData = new LineData(dataSetLine);
        lineData.setHighlightEnabled(false);
        dataSetLine.setColor(R.color.negative_toast);
        dataSetLine.setLineWidth(5);

        BarData barData = new BarData(dataSet);
        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);
        combinedData.setData(barData);

        return combinedData;
    }

    private int getRandomFromRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public void onSeekBarStateChanged(int i) {
        Prefs.setChartDays(i);
        getView().initChart();
    }
}

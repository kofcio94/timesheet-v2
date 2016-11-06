package japko6.workly.ui.stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.CombinedData;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import japko6.workly.R;
import japko6.workly.objects.Day;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BaseFragment;

public class StatsFragment extends BaseFragment implements StatsPresenter.View {

    private ViewHolder viewHolder;
    private StatsPresenter presenter;
    public static DaysChangeListener daysChangeListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        try {
            if (!Prefs.areAdvancedAnimationsEnabled()) {
                root.findViewById(R.id.moving_background).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolderSetUp(root);
        presenterSetUp();
        initListeners();
        return root;
    }

    private void initListeners() {
        daysChangeListener = new DaysChangeListener() {
            @Override
            public void onNewDayAdded() {
                try {
                    initSeek();
                    initChart();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        initSeek();
        initChart();
    }

    private void initSeek() {
        ArrayList<Day> days = Prefs.getDays();
        if (days == null || days.size() < 1) {
            Prefs.setChartDays(80);
            viewHolder.seekBar.setMax(80);
            viewHolder.seekBar.setEnabled(true);
        } else {
            if (days.size() <= 1) {
                viewHolder.seekBar.setEnabled(false);
            } else {
                viewHolder.seekBar.setEnabled(true);
            }
            Prefs.setChartDays(days.size());
            viewHolder.seekBar.setMax(days.size());
        }
        viewHolder.seekBar.setProgress(Prefs.getChartDays());
        viewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int i = seekBar.getProgress();
                if (i == 0) {
                    i = 1;
                }
                presenter.onSeekBarStateChanged(i);
            }
        });
    }

    @Override
    public void initChart() {
        CombinedData chartData = presenter.getChartData(getContext());
        viewHolder.chartData.setData(chartData);
        viewHolder.chartData.invalidate();
        if (Prefs.areAdvancedAnimationsEnabled()) {
            viewHolder.chartData.animateY(1000);
        }
        viewHolder.chartData.setDrawValueAboveBar(true);
        viewHolder.chartData.setGridBackgroundColor(R.color.transparent);
        viewHolder.chartData.setPinchZoom(false);
        viewHolder.chartData.setScaleXEnabled(false);
        viewHolder.chartData.setDrawGridBackground(false);
        viewHolder.chartData.setDrawBorders(false);
        Description description = new Description();
        description.setText("");
        viewHolder.chartData.setDescription(description);
    }

    @Override
    protected void viewHolderSetUp(View view) {
        viewHolder = new ViewHolder(view);
    }

    @Override
    protected void presenterSetUp() {
        presenter = new StatsPresenter();
        presenter.onLoad(this);
    }

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.chart)
        CombinedChart chartData;

        @Bind(R.id.seek_bar)
        AppCompatSeekBar seekBar;
    }
}

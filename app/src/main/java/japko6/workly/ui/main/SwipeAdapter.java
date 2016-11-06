package japko6.workly.ui.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import japko6.workly.R;
import japko6.workly.ui.details.DetailsListFragment;
import japko6.workly.ui.mail.MailFragment;
import japko6.workly.ui.settings.SettingsFragment;
import japko6.workly.ui.stats.StatsFragment;

public class SwipeAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public SwipeAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new StatsFragment();
            case 1:
                return new DetailsListFragment();
            case 2:
                return new MailFragment();
            case 3:
                return new SettingsFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.page_stats);
            case 1:
                return context.getString(R.string.page_details);
            case 2:
                return context.getString(R.string.page_report);
            case 3:
                return context.getString(R.string.page_settings);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}

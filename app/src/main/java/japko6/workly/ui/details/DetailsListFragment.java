package japko6.workly.ui.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import at.markushi.ui.CircleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import japko6.workly.R;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BaseFragment;
import japko6.workly.ui.details.adapter.DetailsListAdapter;
import japko6.workly.ui.details.adapter.ItemDetail;
import japko6.workly.ui.details.detailsNew.DetailsNewActivity;
import japko6.workly.ui.details.detailsEdit.DetailsEditActivity;

public class DetailsListFragment extends BaseFragment implements DetailsListPresenter.View {

    public static String KEY_BUNDLE_ITEM_TO_EDIT = "item_details";

    private DetailsListPresenter presenter;
    private ViewHolder viewHolder;
    private List<ItemDetail> itemDetailArray;

    public static ListUpdateInterface listUpdateInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        try {
            if (!Prefs.areAdvancedAnimationsEnabled()) {
                root.findViewById(R.id.moving_background).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolderSetUp(root);
        presenterSetUp();
        initInterfaces();
        initView();
        return root;
    }

    private void initInterfaces() {
        listUpdateInterface = new ListUpdateInterface() {
            @Override
            public void refreshList() {
                updateListView();
            }
        };
    }

    @Override
    protected void viewHolderSetUp(View view) {
        viewHolder = new ViewHolder(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setListeners() {
        viewHolder.mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onPullToRefresh();
            }
        });
        viewHolder.mBtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onButtonAddClicked();
            }
        });
        viewHolder.mLv.setOnItemClickListener(itemClickListener);
    }

    private void initView() {
        setUpListView();
        setListeners();
    }

    private void setUpListView() {
        viewHolder.mLv.setDividerHeight(0);
    }

    @Override
    public void updateListView() {
        itemDetailArray = presenter.getDetailsItems();
        if (itemDetailArray == null) {
            return;
        }
        try {
            DetailsListAdapter detailsListAdapter = new DetailsListAdapter(getContext(), R.layout.item_details, itemDetailArray);
            viewHolder.mLv.setAdapter(detailsListAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hidePullToRefresh() {
        viewHolder.mPullToRefresh.setRefreshing(false);
    }

    @Override
    public void showWrongDate() {
        showNegativeToast(R.string.details_fragment_data_wrong_format, true);
    }

    @Override
    public void startActivityNewDetails() {
        Intent intent = new Intent(getActivity(), DetailsNewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        getActivity().startActivity(intent);
    }


    @Override
    protected void presenterSetUp() {
        presenter = new DetailsListPresenter();
        presenter.onLoad(this);
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0 && Prefs.isWorking()) {
                showNegativeToast(R.string.details_fragment_cant_delete_msg, true);
                return;
            }
            Intent intent = new Intent(getActivity(), DetailsEditActivity.class);
            intent.putExtra(KEY_BUNDLE_ITEM_TO_EDIT, itemDetailArray.get(position));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            getActivity().startActivity(intent);
        }
    };

    @Override
    public void showEmptyDaysInfo() {
        showNegativeToast(R.string.details_fragment_no_data, true);
    }

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.fragment_details_lv)
        ListView mLv;

        @Bind(R.id.fragment_details_ptr)
        SwipeRefreshLayout mPullToRefresh;

        @Bind(R.id.fragment_details_cb_add)
        CircleButton mBtAdd;

    }
}

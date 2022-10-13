package com.projectpractice.lqlm.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.projectpractice.lqlm.R;
import com.projectpractice.lqlm.base.BaseFragment;
import com.projectpractice.lqlm.databinding.FragmentHomePagerBinding;
import com.projectpractice.lqlm.model.entity.Category;
import com.projectpractice.lqlm.model.entity.HomePagerContent;
import com.projectpractice.lqlm.presenter.ICategoryPagerPresenter;
import com.projectpractice.lqlm.presenter.ITicketPresenter;
import com.projectpractice.lqlm.ui.activity.TicketActivity;
import com.projectpractice.lqlm.ui.adapter.HomePagerContentListAdapter;
import com.projectpractice.lqlm.ui.adapter.LooperPagerAdapter;
import com.projectpractice.lqlm.ui.custom.AutoLoopViewPager;
import com.projectpractice.lqlm.utils.Constants;
import com.projectpractice.lqlm.utils.DensityUtil;
import com.projectpractice.lqlm.utils.PresenterManager;
import com.projectpractice.lqlm.view.ICategoryPagerCallback;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import java.util.List;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, HomePagerContentListAdapter.OnListItemClickListener, LooperPagerAdapter.OnLooperItemClicked {

    private static final String TAG = "HomePagerFragment";
    private FragmentHomePagerBinding binding;
    private ICategoryPagerPresenter categoryPagerPresenter;
    private int materialId;
    private String title;
    private RecyclerView contentList;
    private HomePagerContentListAdapter contentListAdapter;
    private AutoLoopViewPager looperPager;
    private LooperPagerAdapter looperPagerAdapter;
    private LinearLayout looperPointContainer;
    private SmartRefreshLayout homePagerRefresh;

    public static HomePagerFragment newInstance(Category.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomePagerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected void initView() {
        contentList = binding.homePagerContentList;
        looperPager = binding.looperPager;
        looperPointContainer = binding.looperPointContainer;
        homePagerRefresh = binding.homePagerRefresh;
        contentList.setLayoutManager(new LinearLayoutManager(getContext()));
        contentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });
        contentListAdapter = new HomePagerContentListAdapter();
        contentList.setAdapter(contentListAdapter);
        contentList.setItemViewCacheSize(10);
        //创建轮播图适配器
        looperPagerAdapter = new LooperPagerAdapter();
        //设置适配器
        looperPager.setAdapter(looperPagerAdapter);

        homePagerRefresh.setEnableRefresh(false);
        homePagerRefresh.setEnableLoadMore(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        looperPager.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        looperPager.stopLoop();
    }

    @Override
    protected void initListener() {
        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (looperPagerAdapter.getDataSize() == 0) {
                    return;
                }
                //切换轮播图指示器
                int targetPosition = position % looperPagerAdapter.getDataSize();
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        homePagerRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                categoryPagerPresenter.loadMore(materialId);
            }
        });
        contentListAdapter.setOnListItemClickListener(this);
        looperPagerAdapter.setOnLooperItemClickedListener(this);
    }

    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point = looperPointContainer.getChildAt(i);
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_point_normal);
            }
        }

    }

    @Override
    protected void initPresenter() {
        categoryPagerPresenter = PresenterManager.getInstance().getCategoryPagerPresenter();
        if (categoryPagerPresenter != null) {
            categoryPagerPresenter.registerCallback(this);
        }

    }

    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        materialId = bundle.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        title = bundle.getString(Constants.KEY_HOME_PAGER_TITLE);
        Log.d(TAG, "title ===> " + title);
        Log.d(TAG, "materialId ===> " + materialId);
        if (categoryPagerPresenter != null) {
            categoryPagerPresenter.getContentByCategoryId(materialId);
        }
    }

    @Override
    public int getCategoryId() {
        return materialId;
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        contentListAdapter.setData(contents);
        setUpState(State.SUCCESS);

    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onLoadMoreError() {
        Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
        if (homePagerRefresh != null) {
            homePagerRefresh.finishLoadMore();
        }
    }

    @Override
    public void onLoadMoreEmpty() {
        Toast.makeText(getContext(), "没有更多了...", Toast.LENGTH_SHORT).show();
        if (homePagerRefresh != null) {
            homePagerRefresh.finishLoadMore();
        }
    }

    @Override
    public void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents) {
        Log.d(TAG, "load more result ==> " + contents);
        //数据添加到适配器底部
        contentListAdapter.addData(contents);
        if (homePagerRefresh != null) {
            homePagerRefresh.finishLoadMore();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        looperPagerAdapter.setData(contents);
        looperPointContainer.removeAllViews();
        //轮播图设置到中心点
        int dx = (Integer.MAX_VALUE / 2) % contents.size();
        int targetCenterPosition = Integer.MAX_VALUE / 2 - dx;
        looperPager.setCurrentItem(targetCenterPosition);
        int size = DensityUtil.dip2px(getContext(), 8);
        for (int i = 0; i < contents.size(); i++) {
            View point = new View(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.setMargins(DensityUtil.dip2px(getContext(), 5), 0, DensityUtil.dip2px(getContext(), 8), 0);
            point.setLayoutParams(layoutParams);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_point_normal);
            }
            looperPointContainer.addView(point);
        }
    }

    @Override
    protected void release() {
        if (categoryPagerPresenter != null) {
            categoryPagerPresenter.unregisterCallback(this);
        }
    }

    @Override
    public void onListItemClick(HomePagerContent.DataBean item) {
//        Log.d(TAG, "onListItemClick ==> "+item.getTitle());
        handleItemClick(item);
    }

    @Override
    public void onLooperItemClicked(HomePagerContent.DataBean item) {
//        Log.d(TAG, "onLooperItemClicked ==> "+item.getTitle());
        handleItemClick(item);
    }

    private void handleItemClick(HomePagerContent.DataBean item) {
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(item.getTitle(),item.getClick_url(),item.getPict_url());
        Intent intent = new Intent(getContext(), TicketActivity.class);
        intent.putExtra("volume",item.getVolume());
        intent.putExtra("title",item.getTitle());
        intent.putExtra("coupon_amount",item.getCoupon_amount());
        intent.putExtra("original_price",item.getZk_final_price());
        startActivity(intent);
    }
}

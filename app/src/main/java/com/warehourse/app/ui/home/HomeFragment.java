package com.warehourse.app.ui.home;

import com.biz.base.BaseLazyFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.biz.widget.banner.LoopRecyclerViewPager;
import com.biz.widget.recyclerview.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.application.WareApplication;
import com.warehourse.app.event.MainPointEvent;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.HomeEntity;
import com.warehourse.app.ui.adv.AdvertisingFragment;
import com.warehourse.app.ui.base.EmptyViewHolder;
import com.warehourse.app.ui.message.MessagesFragment;
import com.warehourse.app.ui.search.SearchActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Space;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * Title: HomeFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:10/05/2017  16:37
 *
 * @author johnzheng
 * @version 1.0
 */

public class HomeFragment extends BaseLazyFragment {

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();


    private View mDotView;
    public final static long TIME = 4;

    private long time = TIME;

    //private LinearLayout mLayout;


    //private SwipeRefreshLayout mRefreshLayout;
    private HomeViewModel mViewModel;
    private XRecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;


    public void onEventMainThread(MainPointEvent event) {
        setDotView(event.isPoint());
    }

    private void setDotView(boolean show) {
        mDotView.postDelayed(() -> {
            mDotView.setVisibility(show ? View.VISIBLE : View.GONE);
        }, 500);
    }

    @Override
    public void error(String error) {
        emptySearch(error);
    }

    private void emptySearch(String error) {
        setHasLoaded(false);
        mRecyclerView.setRefreshing(false);
        EmptyViewHolder holder = EmptyViewHolder.createHolder(getContext())
                .setIcon(R.drawable.ic_failed_64)
                .setTitle(error);
        holder.itemView.setPadding(0, Utils.dip2px(80), 0, 0);
        mAdapter.setEmptyView(holder.itemView);
//        mLayout.removeAllViews();
//        mLayout.addView(holder.itemView);
    }

    private void emptyProgress() {
        mAdapter.setEmptyView(mRecyclerView.getProgressView());
        //View view = LayoutInflater.from(getContext()).inflate( R.layout.loading_layout, mLayout, false);
//        view.setBackgroundResource(R.color.color_transparent);
//        mLayout.removeAllViews();
//        mLayout.addView(view);
    }

    private void addLine(BaseQuickAdapter adapter) {
        Space space = new Space(mRecyclerView.getContext());
        space.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(8)));
        adapter.addHeaderView(space);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mViewModel = new HomeViewModel(this);
        initViewModel(mViewModel);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //setBannerSubscription();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mCompositeSubscription.clear();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mLayout = findViewById(R.id.layout);

        View actionView = LayoutInflater.from(getContext()).inflate(R.layout.toolbar_message_dot_layout, mToolbar, false);
        mToolbar.getMenu().add(0, 0, 0, R.string.title_message)
                .setIcon(R.drawable.vector_message)
                .setActionView(actionView)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        mRefreshLayout = getView(R.id.refresh);
//        mRefreshLayout.setDistanceToTriggerSync(Utils.dip2px(100));
        mRecyclerView = getView(R.id.list);
        mAdapter = new HomeAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mDotView = getView(actionView, R.id.text_unread);
        mDotView.setVisibility(UserModel.getInstance().isLogin() ? View.VISIBLE : View.GONE);
        //mRefreshLayout.setColorSchemeResources(BaseRecyclerView.COLORS);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams();
        lp.topMargin = Utils.getStatusBarHeight(getActivity());
        setDotView(false);
        bindUi(RxUtil.click(actionView), s -> {
            MessagesFragment.startMessage(getActivity());
            mDotView.setVisibility(View.GONE);

        });

        EditText searchEdit = getView(R.id.edit_search);
        searchEdit.setHint(SystemModel.getInstance().getPlaceHolder());
        bindUi(RxUtil.click(searchEdit), s -> {
            SearchActivity.startSearch(getContext());
        });

        emptyProgress();

        mRecyclerView.setRefreshListener(this::refresh);
    }


    private void setData(List<HomeEntity> list) {
        if (list != null && list.size() > 0) {
            for (HomeEntity entity : list) {
                setBanner(entity);
                GridView gridView = CategoryAdapter.createGridView(mRecyclerView, entity);
                if (gridView != null) {
                    mAdapter.addHeaderView(gridView);
                    addLine(mAdapter);
                }
                View view = ProductAdapter.createGridView(mRecyclerView, entity);
                if (view != null) {
                    mAdapter.addHeaderView(view);
                }
                PromoViewHolder holder = PromoViewHolder.setImageShowcase(mRecyclerView, entity);
                if (holder != null) {
                    mAdapter.addHeaderView(holder.itemView);
                    addLine(mAdapter);
                }
            }
        }
        setBannerSubscription();
    }

    private void setBannerSubscription() {
//        mCompositeSubscription.clear();
//        Observable.range(0, mLayout.getChildCount()).forEach(i -> {
//            LoopRecyclerViewPager viewpager = (LoopRecyclerViewPager)
//                    mLayout.getChildAt(i).findViewById(R.id.viewpager);
//            if (viewpager!=null) {
//                mCompositeSubscription.add(
//                        Observable.interval(time, time, TimeUnit.MILLISECONDS)
//                                .subscribe(s -> {
//                                    int currentIndex = viewpager.getActualCurrentPosition();
//                                    if (++currentIndex == viewpager.getAdapter().getItemCount()) {
//                                        viewpager.smoothScrollToPosition(0);
//                                    } else {
//                                        viewpager.smoothScrollToPosition(currentIndex);
//                                    }
//                                })
//                );
//            }
//
//        });
    }

    private void setBanner(HomeEntity entity) {
        if (HomeEntity.TYPE_SLIDE_BANNER.equals(entity.type) && entity.items != null && entity.items.size() > 0) {
            View view = BannerAdapter.createViewPager(mRecyclerView, entity);
            time = entity.interval == 0 ? TIME : entity.interval;
            mAdapter.addHeaderView(view);
            LoopRecyclerViewPager viewpager = (LoopRecyclerViewPager)
                    view.findViewById(R.id.viewpager);
            if (viewpager != null && entity.items != null && entity.items.size() > 1 && time > 0) {
                mCompositeSubscription.add(
                        Observable.interval(time, time, TimeUnit.SECONDS)
                                .subscribe(s -> {
                                    int currentIndex = viewpager.getActualCurrentPosition();
                                    if (++currentIndex == viewpager.getAdapter().getItemCount()) {
                                        viewpager.smoothScrollToPosition(0);
                                    } else {
                                        viewpager.smoothScrollToPosition(currentIndex);
                                    }
                                })
                );
            }
        }
    }


    private void refresh() {
        mViewModel.request(list -> {
            mAdapter.removeAllHeaderView();
            mRecyclerView.setRefreshing(false);
            setHasLoaded(true);
            setData(list);
            mAdapter.setNewData(Lists.newArrayList(""));

        });

    }

    @Override
    public void lazyLoad() {
        refresh();
        if (!WareApplication.getApplication().isShowAdv()) {
            mViewModel.showNotice(b -> {
                if (b && getActivity().getIntent().getData() == null) {
                    WareApplication.getApplication().setShowAdv(true);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                            .add(R.id.frame_home, new AdvertisingFragment(), AdvertisingFragment.class.getName())
                            .commitAllowingStateLoss();
                }
            });
        }
    }

    class HomeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public HomeAdapter() {
            super(R.layout.item_single_text_layout, Lists.newArrayList());
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, String s) {
            baseViewHolder.itemView.setBackgroundResource(R.color.color_fbfbfb);
        }
    }

}

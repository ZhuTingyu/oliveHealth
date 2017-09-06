package com.olive.ui.main.home;

import com.biz.base.BaseLazyFragment;
import com.biz.util.IntentBuilder;
import com.biz.widget.ExpandGridView;
import com.biz.widget.banner.ConvenientBanner;
import com.biz.widget.recyclerview.XRecyclerView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.olive.R;
import com.olive.model.UserModel;
import com.olive.model.entity.NoticeEntity;
import com.olive.ui.adapter.HomeNoticeAdapter;
import com.olive.ui.adapter.ProductAdapter;
import com.olive.ui.holder.ImageHolderView;
import com.olive.ui.login.LoginActivity;
import com.olive.ui.notice.NoticeDetailFragment;
import com.olive.ui.notice.NoticeListFragment;
import com.olive.ui.notice.NoticeViewModel;
import com.olive.ui.order.ProductDetailsFragment;
import com.olive.ui.search.SearchActivity;
import com.olive.util.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Title: HomeFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:20/07/2017  10:55
 *
 * @author johnzheng
 * @version 1.0
 */

public class HomeFragment extends BaseLazyFragment {

    XRecyclerView mRecyclerView;

    private ConvenientBanner banner;
    private ExpandGridView gridview;
    private XRecyclerView mNoticeTitleList;
    private EditText searchView;

    ProductAdapter mAdapter;
    HomeNoticeAdapter mNoticeAdapter;
    HomeCategoryAdapter adapter;

    private HomeViewModel viewModel;
    private NoticeViewModel noticeViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new HomeViewModel(context);
        noticeViewModel = new NoticeViewModel(context);
        initViewModel(viewModel);
    }

    @Override
    public void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_toolbar_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_recyclerview, getView(R.id.frame_holder));
        initListView();
        initData();

        searchView = getView(R.id.edit_search);
        searchView.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
            if ((keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER)
                    && event.getAction() == KeyEvent.ACTION_UP) {
                v.clearFocus();
                String key = getSearchText();
                if (!TextUtils.isEmpty(key)) {
                    IntentBuilder.Builder(getActivity(), SearchActivity.class).
                            putExtra(IntentBuilder.KEY_VALUE, key).startActivity();
                } else {
                    error(getString(R.string.message_input_search_key_word));
                }
            }
            return false;
        });

    }

    public String getSearchText() {
        return searchView.getText() == null ? "" : searchView.getText().toString();
    }

    private void initListView() {
        mRecyclerView = getView(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new ProductAdapter(R.layout.item_product_grid_layout);
        mAdapter.setViewModel(viewModel);
        mAdapter.addHeaderView(createBannerView());
        mRecyclerView.setRefreshListener(() -> {
            initData();
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {

        viewModel.getRecommendProductList(productEntities -> {
            mAdapter.setNewData(productEntities);
        });

        viewModel.getCategoryList(categoryEntities -> {
            adapter.setList(categoryEntities);
            gridview.setAdapter(adapter);
            mRecyclerView.setRefreshing(false);

        });
    }

    private View createBannerView() {
        View view = getLayoutInflater().inflate(R.layout.item_home_banner_layout, null);

        findViewById(view, R.id.icon_left).setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), NoticeListFragment.class, true);
        });

        initBanner(view);


        initNoticeList(view);


        initGirdView(view);


        return view;

    }

    private void initNoticeList(View view) {
        mNoticeTitleList = findViewById(view, R.id.notice_list);
        mNoticeTitleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        Utils.setRecyclerViewNestedSlide(mNoticeTitleList);
        mNoticeAdapter = new HomeNoticeAdapter();
        mNoticeTitleList.setAdapter(mNoticeAdapter);
        mNoticeAdapter.setOnLoadMoreListener(() -> {
            noticeViewModel.setLoadMore(o -> {
            });
        }, mNoticeTitleList.getRecyclerView());
        mNoticeAdapter.setOnItemClickListener((baseQuickAdapter, view1, i) -> {
            NoticeEntity entity = (NoticeEntity) baseQuickAdapter.getItem(i);
            IntentBuilder.Builder().putExtra(IntentBuilder.KEY_VALUE, entity.id)
                    .startParentActivity(getActivity(), NoticeDetailFragment.class, true);
        });

        noticeViewModel.getNoticeList(noticeEntities -> {
            mNoticeAdapter.setNewData(noticeEntities);
        });

        noticeViewModel.setRecyclerView(mNoticeTitleList);
    }

    private void initBanner(View view) {
        banner = (ConvenientBanner) view.findViewById(R.id.banner);
        View indicator = banner.findViewById(com.bigkoo.convenientbanner.R.id.loPageTurningPoint);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) indicator.getLayoutParams();
        lp.bottomMargin = com.biz.util.Utils.dip2px(30);
        viewModel.getAvertList(advertEntities -> {
            banner.setPages(
                    () -> new ImageHolderView(com.biz.util.Utils.dip2px(getActivity(), 180), ScalingUtils.ScaleType.FIT_XY), viewModel.getImageList())
                    .startTurning(3000)
                    .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focus})
                    .setPointViewVisible(true)
                    .setCanLoop(true);
        });
        banner.setOnItemClickListener(position -> {
            String id = viewModel.advertEntityList.get(position).sourceId;
            if(viewModel.isGoodsAdvert(position)){
                ProductDetailsFragment.startProductDetailsFragment(getActivity(), id);
            }else {
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_VALUE, id)
                        .startParentActivity(getActivity(), NoticeDetailFragment.class, true);
            }
        });
    }

    private void initGirdView(View view) {
        gridview = (ExpandGridView) view.findViewById(R.id.gridview);
        gridview.setNumColumns(5);
        adapter = new HomeCategoryAdapter(getActivity());
        gridview.setOnItemClickListener((parent, view1, position, id) -> {
            IntentBuilder.Builder(getActivity(), SearchActivity.class).
                    putExtra(IntentBuilder.KEY_DATA, adapter.getItem(position).code).startActivity();
        });
    }

    @Override
    public void error(int code, String error) {
        if(code == 1004){
            UserModel.getInstance().loginOut();
            IntentBuilder.Builder(getActivity(), LoginActivity.class)
                    .putExtra(IntentBuilder.KEY_TYPE, LoginActivity.TYPE_LOGIN_INVALID)
                    .startActivity();
            getActivity().finish();
        }
    }
}

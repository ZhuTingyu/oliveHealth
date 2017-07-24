package com.olive.ui.main.home;

import com.biz.base.BaseLazyFragment;
import com.biz.util.Lists;
import com.biz.util.Utils;
import com.biz.widget.ExpandGridView;
import com.biz.widget.banner.ConvenientBanner;
import com.biz.widget.recyclerview.XRecyclerView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.olive.R;
import com.olive.ui.adapter.HomeNoticeAdapter;
import com.olive.ui.adapter.ProductAdapter;
import com.olive.ui.holder.ImageHolderView;
import com.olive.ui.search.SearchActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

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
    private RecyclerView mNoticeList;

    ProductAdapter mAdapter;
    HomeNoticeAdapter mNoticeAdapter;

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
        mRecyclerView = getView(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new ProductAdapter();
        mAdapter.addHeaderView(createBannerView());
        mAdapter.setNewData(Lists.newArrayList("","","",""));
        mRecyclerView.setAdapter(mAdapter);

        EditText searchView = getView(R.id.edit_search);
        searchView.setFocusableInTouchMode(false);
        searchView.setOnClickListener(v->{
            SearchActivity.startSearch(getActivity());
        });

        mRecyclerView.setRefreshListener(()->{
            mRecyclerView.postDelayed(()->{mRecyclerView.setRefreshing(false);},2000);
        });
    }

    private View createBannerView(){
        View view = getLayoutInflater().inflate(R.layout.item_home_banner_layout, null);

        banner = (ConvenientBanner) view.findViewById(R.id.banner);
        gridview = (ExpandGridView) view.findViewById(R.id.gridview);
        gridview.setNumColumns(5);
        mNoticeList = findViewById(view, R.id.notice_list);
        mNoticeList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNoticeAdapter = new HomeNoticeAdapter();
        mNoticeAdapter.setNewData(Lists.newArrayList("","","",""));
        mNoticeList.setAdapter(mNoticeAdapter);
        View indicator = banner.findViewById(com.bigkoo.convenientbanner.R.id.loPageTurningPoint);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) indicator.getLayoutParams();
        lp.bottomMargin= Utils.dip2px(30);
        List list = Lists.newArrayList(
                "http://img.taopic.com/uploads/allimg/140326/235113-1403260G01561.jpg",
                "http://img.taopic.com/uploads/allimg/140326/235113-1403260G01561.jpg",
                "http://img.taopic.com/uploads/allimg/140326/235113-1403260G01561.jpg");
        banner.setPages(
                () -> new ImageHolderView(Utils.dip2px(getActivity(), 180), ScalingUtils.ScaleType.FIT_XY), list)
                .startTurning(3000)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focus})
                .setPointViewVisible(true)
                .setCanLoop(true);

        HomeCategoryAdapter adapter = new HomeCategoryAdapter(getActivity());
        adapter.setList(Lists.newArrayList(getContext().getResources().getStringArray(R.array.array_category)));
        gridview.setAdapter(adapter);


        return view;

    }


}

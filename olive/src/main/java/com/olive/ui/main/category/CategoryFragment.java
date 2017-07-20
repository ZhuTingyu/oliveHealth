package com.olive.ui.main.category;

import com.biz.base.BaseLazyFragment;
import com.biz.util.Lists;
import com.biz.widget.ExpandGridView;
import com.biz.widget.banner.ConvenientBanner;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.adapter.ProductAdapter;
import com.olive.ui.search.SearchActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Title: CategoryFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:20/07/2017  10:56
 *
 * @author johnzheng
 * @version 1.0
 */

public class CategoryFragment extends BaseLazyFragment {

    XRecyclerView mRecyclerView;
    RecyclerView mBrandView;

    ProductAdapter mAdapter;

    BrandAdapter mBrandAdapter;

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

        getLayoutInflater().inflate(R.layout.fragment_category_child_layout, getView(R.id.frame_holder));
        mRecyclerView = getView(R.id.list);
        mBrandView = getView(R.id.list_brand);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new ProductAdapter();
        mAdapter.setNewData(Lists.newArrayList("","","",""));

        mBrandAdapter = new BrandAdapter();
        addItemDecorationLine(mBrandView);
        mBrandView.setAdapter(mBrandAdapter);
        mRecyclerView.setAdapter(mAdapter);

        EditText searchView = getView(R.id.edit_search);
        searchView.setFocusableInTouchMode(false);
        searchView.setOnClickListener(v->{
            SearchActivity.startSearch(getActivity());
        });
    }


}

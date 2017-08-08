package com.olive.ui.main.my.favorite;

import com.biz.base.BaseFragment;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.adapter.ProductAdapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Title: SearchFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:20/07/2017  14:29
 *
 * @author johnzheng
 * @version 1.0
 */

public class FavoriteFragment extends BaseFragment {



    private XRecyclerView mRecyclerView;
    private ProductAdapter mAdapter;
    private FavoriteViewModel viewModel;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new FavoriteViewModel(context);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.text_my_favor);
        mRecyclerView = getView(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new ProductAdapter(R.layout.item_product_grid_layout);
        mRecyclerView.setAdapter(mAdapter);
        viewModel.getFavoriteList(productEntities -> {
            mAdapter.setNewData(productEntities);
        });



//        mRecyclerView.setRefreshListener(()->{
//
//        });



    }


}

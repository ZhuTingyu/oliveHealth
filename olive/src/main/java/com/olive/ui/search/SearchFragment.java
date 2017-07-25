package com.olive.ui.search;

import com.biz.base.BaseFragment;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
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

public class SearchFragment extends BaseFragment {


    private ViewGroup layout;
    private View totalView, priceView, saleView;
    private AppCompatTextView textTotal;
    private AppCompatTextView textPrice;
    private AppCompatTextView textSale;

    private XRecyclerView mRecyclerView;
    private ProductAdapter mAdapter;

    private EditText searchView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        searchView = getView(getActivity(), R.id.edit_search);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = findViewById(R.id.layout);
        textTotal =  findViewById(R.id.text_total);
        textPrice =  findViewById(R.id.text_price);
        textSale =  findViewById(R.id.text_sale);

        mRecyclerView = getView(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new ProductAdapter(R.layout.item_product_grid_layout);
        mAdapter.setNewData(Lists.newArrayList("", "", "", ""));
        mRecyclerView.setAdapter(mAdapter);

//        mRecyclerView.setRefreshListener(()->{
//
//        });

        totalView = layout.getChildAt(0);
        priceView = layout.getChildAt(1);
        saleView = layout.getChildAt(2);

        totalView.setOnClickListener(v -> {
            totalView.setSelected(true);
            priceView.setSelected(false);
            saleView.setSelected(false);
        });

        textPrice.setOnClickListener(v -> {
            totalView.setSelected(false);
            priceView.setSelected(true);
            saleView.setSelected(false);
        });

        saleView.setOnClickListener(v -> {
            totalView.setSelected(false);
            priceView.setSelected(false);
            saleView.setSelected(true);
        });

        searchView.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
            if ((keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER)
                    && event.getAction() == KeyEvent.ACTION_UP) {
                v.clearFocus();
                String key = getSearchText();
                if (TextUtils.isEmpty(key)) {

                }
            }
            return false;
        });

    }

    public String getSearchText() {
        return searchView.getText() == null ? "" : searchView.getText().toString();
    }
}

package com.olive.ui.search;

import com.biz.base.BaseFragment;
import com.biz.util.Lists;
import com.biz.util.RxUtil;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.BaseErrorFragment;
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

public class SearchFragment extends BaseErrorFragment {


    private ViewGroup layout;
    private View totalView, priceView, saleView;
    private AppCompatTextView textTotal;
    private AppCompatTextView textPrice;
    private AppCompatTextView textSale;

    private XRecyclerView mRecyclerView;
    private ProductAdapter mAdapter;

    private EditText searchView;

    private SearchViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        searchView = getView(getActivity(), R.id.edit_search);
        viewModel = new SearchViewModel(context);
        initViewModel(viewModel);
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
        mRecyclerView.setRefreshListener(() -> {
            viewModel.search(o -> {
                mRecyclerView.setRefreshing(false);
            });
        });
        mAdapter = new ProductAdapter(R.layout.item_product_grid_layout);
        mAdapter.setViewModel(viewModel);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(()->{
            viewModel.setLoadMore(o -> {
                setProgressVisible(false);
            });
        },mRecyclerView.getRecyclerView());
        viewModel.setRecyclerView(mRecyclerView);

        viewModel.getProductList(productEntities -> {
            mAdapter.setNewData(productEntities);
        });


        totalView = layout.getChildAt(0);
        priceView = layout.getChildAt(1);
        saleView = layout.getChildAt(2);

        totalView.setOnClickListener(v -> {
            setProgressVisible(true);
            totalView.setSelected(!v.isSelected());
            priceView.setSelected(false);
            saleView.setSelected(false);
            if(v.isSelected()){
                viewModel.setOrderUp();
            }else {
                viewModel.setOrderDOWN();
            }
            viewModel.setSort(SearchViewModel.TYPE_RANK_SYNTHESIZE);
            viewModel.search(o -> {
                setProgressVisible(false);
            });
        });

        textPrice.setOnClickListener(v -> {
            setProgressVisible(true);
            totalView.setSelected(false);
            priceView.setSelected(!v.isSelected());
            saleView.setSelected(false);
            if(v.isSelected()){
                viewModel.setOrderUp();
            }else {
                viewModel.setOrderDOWN();
            }
            viewModel.setSort(SearchViewModel.TYPE_RANK_PRICE);
            viewModel.search(o -> {
                setProgressVisible(false);
            });
        });

        saleView.setOnClickListener(v -> {
            setProgressVisible(true);
            totalView.setSelected(false);
            priceView.setSelected(false);
            saleView.setSelected(!v.isSelected());
            if(v.isSelected()){
                viewModel.setOrderUp();
            }else {
                viewModel.setOrderDOWN();
            }
            viewModel.setSort(SearchViewModel.TYPE_RANK_SALE);
            viewModel.search(o -> {
                setProgressVisible(false);
            });
        });

        bindUi(RxUtil.textChanges(searchView), viewModel.setKeyWord());
        searchView.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
            if ((keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER)
                    && event.getAction() == KeyEvent.ACTION_UP) {
                v.clearFocus();
                String key = getSearchText();
                if (!TextUtils.isEmpty(key)) {
                    setProgressVisible(true);
                    viewModel.search(o -> {
                        setProgressVisible(false);
                    });
                    dismissKeyboard();
                }else {
                    error(getString(R.string.message_input_search_key_word));
                }
            }
            return false;
        });

    }

    public String getSearchText() {
        return searchView.getText() == null ? "" : searchView.getText().toString();
    }
}
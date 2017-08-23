package com.olive.ui.main.category;

import com.biz.base.BaseLazyFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.RxUtil;
import com.biz.widget.ExpandGridView;
import com.biz.widget.banner.ConvenientBanner;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.adapter.ProductAdapter;
import com.olive.ui.order.ProductDetailsFragment;
import com.olive.ui.search.SearchActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
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

    private CategoryViewModel viewModel;
    private EditText searchView;

    @Override
    public void lazyLoad() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new CategoryViewModel(context);
        initViewModel(viewModel);
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

        initBrandView();

        searchView = getView(R.id.edit_search);
        bindUi(RxUtil.textChanges(searchView), viewModel.setKeyWord());
        searchView.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
            if ((keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER)
                    && event.getAction() == KeyEvent.ACTION_UP) {
                v.clearFocus();
                String key = getSearchText();
                if (!TextUtils.isEmpty(key)) {
                    viewModel.cleanPage();
                    viewModel.getProductList(productEntities -> {
                        mAdapter.setNewData(productEntities);
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

    private void initRecyclerView() {
        mRecyclerView = getView(R.id.list);
        viewModel.setRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new ProductAdapter(R.layout.item_product_grid_layout);
        mAdapter.setViewModel(viewModel);
        mAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_VALUE, mAdapter.getItem(i).productNo)
                    .startParentActivity(getActivity(), ProductDetailsFragment.class, true);
        });
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(()->{
            viewModel.setLoadMore(o -> {

            });
        },mRecyclerView.getRecyclerView());
        viewModel.getProductList(productEntities -> {
            mAdapter.setNewData(productEntities);
        });
    }

    private void initBrandView() {
        mBrandView = getView(R.id.list_brand);
        mBrandAdapter = new BrandAdapter();
        mBrandAdapter.setOnClickListener(v -> {
            int position = (int) v.getTag();
            viewModel.setCategoryCode(mBrandAdapter.getItem(position).code);
            mBrandAdapter.setSelected(position);
            viewModel.cleanPage();
            viewModel.getProductList(productEntities -> {
                mAdapter.setNewData(productEntities);
            });
        });
        addItemDecorationLine(mBrandView);
        mBrandView.setAdapter(mBrandAdapter);
        viewModel.getCategory(categoryEntities -> {
            mBrandAdapter.setList(categoryEntities);
            initRecyclerView();
        });
    }


}

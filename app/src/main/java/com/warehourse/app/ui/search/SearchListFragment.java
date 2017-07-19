package com.warehourse.app.ui.search;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.biz.widget.recyclerview.XRecyclerView;
import com.warehourse.app.R;
import com.warehourse.app.event.HotKeyClickEvent;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.entity.ProductSearchEntity;
import com.warehourse.app.model.entity.ProductSearchParaEntity;
import com.warehourse.app.ui.base.EmptyViewHolder;
import com.warehourse.app.ui.product.ProductDetailFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.greenrobot.event.EventBus;

/**
 * Title: SearchListFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:17/05/2017  15:51
 *
 * @author johnzheng
 * @version 1.0
 */

public class SearchListFragment extends BaseFragment {

    private static final String KEY = "keyword";
    private static final String CATEGORYID = "categoryId";
    private static final String SORT = "sort";
    private static final String FILEDS = "fields";

    private EditText mSearchText;
    private XRecyclerView mRecyclerView;
    private ProductItemAdapter mAdapter;
    private SearchViewModel mViewModel;
    private FilterViewHolder mFilterViewHolder;

    public void onEventMainThread(HotKeyClickEvent event) {
        progressView();
        mSearchText.setText(event.key);
        mViewModel.setKeyword(event.key,event.isPlaceHolder);
        mViewModel.search(this::search, list -> {
        }, mRecyclerView::setLoadMore);
    }

    private void search(ProductSearchEntity entity) {
        mRecyclerView.setEnabled(true);
        if (entity.getList().size() > 0) {
            mAdapter.setNewData(entity.getList());
        } else {
            mAdapter.setNewData(Lists.newArrayList());
            emptySearch();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mViewModel = new SearchViewModel(this);
        initViewModel(mViewModel);
        EventBus.getDefault().register(this);
    }

    @Override
    public void error(String error) {
        emptySearch();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void emptySearch() {
        mRecyclerView.setEnabled(true);
        EmptyViewHolder holder = EmptyViewHolder.createHolder(getContext())
                .setIcon(R.drawable.vector_empty_search)
                .setTitle(R.string.text_search_none_products);
        mAdapter.setEmptyView(holder.itemView);
    }

    private void progressView() {
        mRecyclerView.setEnabled(false);
        mAdapter.setEmptyView(mRecyclerView.getProgressView());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_list_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mSearchText = getView(getActivity(), R.id.edit_search);
        mFilterViewHolder = new FilterViewHolder(view);
        mRecyclerView = findViewById(R.id.list);

        mFilterViewHolder.mSaleView.setOnClickListener(v -> {
            int level = mFilterViewHolder.setTextSalesClick();
            mViewModel.setSort(level == 2 ?
                    ProductSearchParaEntity.SORT_SALES_VOLUME_DESC : ProductSearchParaEntity.SORT_SALES_VOLUME_ASC);
            onEventMainThread(new HotKeyClickEvent(getSearchText()));
        });

        mFilterViewHolder.mPriceView.setOnClickListener(v -> {
            int level = mFilterViewHolder.setTextPriceClick();
            mViewModel.setSort(level == 2 ?
                    ProductSearchParaEntity.SORT_SALE_PRICE_DESC : ProductSearchParaEntity.SORT_SALE_PRICE_ASC);
            onEventMainThread(new HotKeyClickEvent(getSearchText()));
        });

        mViewModel.setCategoryId(0);
        bindUi(RxUtil.textChanges(mSearchText), mViewModel::setKeyword);

        mSearchText.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
            if ((keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER)
                    && event.getAction() == KeyEvent.ACTION_UP) {
                v.clearFocus();
                String key = getSearchText();
                boolean isPlaceHolder = false;
                if (TextUtils.isEmpty(key)) {
                    key = SystemModel.getInstance().getPlaceHolderWithNone();
                    isPlaceHolder = true;
                }
                EventBus.getDefault().post(new HotKeyClickEvent(key,isPlaceHolder));
            }
            return false;
        });

        mAdapter = new ProductItemAdapter();

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new ProductLoadMoreView());
        addItemDecorationLine(mRecyclerView.getRecyclerView());
        mAdapter.setOnItemClickListener(((baseQuickAdapter, view1, i) -> {
            ProductDetailFragment.startDetail(getActivity(), mAdapter.getItem(i));
        }));

        mAdapter.setOnLoadMoreListener(() -> {
            mViewModel.loadMore(mAdapter::addData, mRecyclerView::setLoadMore);
        }, mRecyclerView.getRecyclerView());
        mRecyclerView.setRefreshListener(()->{
            onEventMainThread(new HotKeyClickEvent(getSearchText()));
        });
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(IntentBuilder.KEY_KEY)) {
            String key = intent.getStringExtra(IntentBuilder.KEY_KEY);
            EventBus.getDefault().post(new HotKeyClickEvent(key));
        }
        if (intent.getData() != null) {
            String key = intent.getData().getQueryParameter(KEY);
            key = TextUtils.isEmpty(key) ? "" : key;
            String categoryId = intent.getData().getQueryParameter(CATEGORYID);
            categoryId = TextUtils.isEmpty(categoryId) ? "" : categoryId;

            String sort = intent.getData().getQueryParameter(SORT);
            sort = TextUtils.isEmpty(sort) ? ProductSearchParaEntity.SORT_SALES_VOLUME_DESC : sort;
            mViewModel.setCategoryId(Utils.getInteger(categoryId));
            mViewModel.setSort(sort);
            mFilterViewHolder.setSort(sort);
            String fields = intent.getData().getQueryParameter(FILEDS);
            mViewModel.setFields(fields);
            EventBus.getDefault().post(new HotKeyClickEvent(key));

        }
    }

    public String getSearchText() {
        return mSearchText.getText() == null ? "" : mSearchText.getText().toString();
    }
}

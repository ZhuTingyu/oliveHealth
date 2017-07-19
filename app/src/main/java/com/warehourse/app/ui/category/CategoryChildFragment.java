package com.warehourse.app.ui.category;

import com.biz.base.BaseLazyFragment;
import com.biz.base.FragmentBackHelper;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.MD5;
import com.biz.util.Utils;
import com.biz.widget.recyclerview.XRecyclerView;
import com.warehourse.app.R;
import com.warehourse.app.event.LoginEvent;
import com.warehourse.app.event.SearchFilterEvent;
import com.warehourse.app.model.entity.CategoryEntity;
import com.warehourse.app.model.entity.ProductBrandEntity.BrandItemEntity;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.model.entity.ProductFilterEntity;
import com.warehourse.app.model.entity.ProductSearchEntity;
import com.warehourse.app.model.entity.ProductSearchParaEntity;
import com.warehourse.app.ui.base.EmptyViewHolder;
import com.warehourse.app.ui.product.ProductDetailFragment;
import com.warehourse.app.ui.search.FilterViewHolder;
import com.warehourse.app.ui.search.ProductLoadMoreView;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Title: CategoryChildFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:10/05/2017  18:08
 *
 * @author johnzheng
 * @version 1.0
 */

public class CategoryChildFragment extends BaseLazyFragment implements FragmentBackHelper {


    private RecyclerView mListBrand;
    private XRecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private ProductItemAdapter mAdapter;
    private CategoryChildViewModel mViewModel;
    private String keyCode = "";
    private FilterKeyFragment mFilterKeyFragment;
    private FilterViewHolder mFilterViewHolder;
    private boolean isBrandclick = false;


    public static CategoryChildFragment newInstance(CategoryEntity entity) {

        Bundle args = new Bundle();
        args.putParcelable(IntentBuilder.KEY_INFO, entity);

        CategoryChildFragment fragment = new CategoryChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onEventMainThread(SearchFilterEvent event) {
        if (event != null && event.fields != null &&
                !TextUtils.isEmpty(event.keyCode) && event.keyCode.equals(keyCode)) {
            mViewModel.setFilterItems(event.fields);
            mDrawerLayout.closeDrawer(GravityCompat.END);
            lazyLoad();

        }
    }

    public void onEventMainThread(LoginEvent event) {
        mViewModel.clearFilter();
        mAdapter.setNewData(Lists.newArrayList());
        mListBrand.setAdapter(null);
        emptySearch();
        setHasLoaded(false);
        if (mViewModel.getCategoryId()==1){
            lazyLoad();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mViewModel = new CategoryChildViewModel(this);
        initViewModel(mViewModel);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void error(String error) {
        isBrandclick = false;
        mAdapter.loadMoreFail();
        emptySearch();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_child_layout, container, false);
    }

    void initView() {
        mDrawerLayout = getView(getActivity(), R.id.drawer);

        mListBrand = findViewById(R.id.list_brand);
        mRecyclerView = findViewById(R.id.list);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        mFilterViewHolder = new FilterViewHolder(view);

        if (getArguments() != null) {
            CategoryEntity entity = getArguments().getParcelable(IntentBuilder.KEY_INFO);
            if (entity != null) {
                mViewModel.setPostUrl(entity.url);
                mViewModel.setCategoryId(Utils.getInteger(entity.id));
                keyCode = MD5.toMD5("" + entity.url + entity.postData);
            }
        }


        mFilterViewHolder.mSaleView.setOnClickListener(v -> {
            int level = mFilterViewHolder.setTextSalesClick();
            mViewModel.setSort(level == 2 ?
                    ProductSearchParaEntity.SORT_SALES_VOLUME_DESC : ProductSearchParaEntity.SORT_SALES_VOLUME_ASC);
            lazyLoad();
        });

        mFilterViewHolder.mPriceView.setOnClickListener(v -> {
            int level = mFilterViewHolder.setTextPriceClick();
            mViewModel.setSort(level == 2 ?
                    ProductSearchParaEntity.SORT_SALE_PRICE_DESC : ProductSearchParaEntity.SORT_SALE_PRICE_ASC);
            lazyLoad();
        });

        mFilterViewHolder.mFilterView.setOnClickListener(v -> {
            mDrawerLayout.openDrawer(GravityCompat.END);

        });
        mFilterViewHolder.mFilterView.setEnabled(false);

        addItemDecorationLine(mListBrand);
        mListBrand.setVisibility(View.GONE);
        mAdapter = new ProductItemAdapter();
        mAdapter.setEnableLoadMore(false);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new ProductLoadMoreView());

        mAdapter.setOnItemClickListener((baseQuickAdapter, view1, i) -> {
            ProductDetailFragment.startDetail(getActivity(), mAdapter.getItem(i));
        });
        mAdapter.setOnLoadMoreListener(() -> {
            mViewModel.loadMore(mAdapter::addData, mRecyclerView::setLoadMore);
        }, mRecyclerView.getRecyclerView());

        mRecyclerView.setRefreshListener(this::refresh);
    }


    private void setProductSearchEntity(ProductSearchEntity entity) {
        setHasLoaded(true);
        mRecyclerView.setEnabled(true);
        if (mListBrand.getAdapter() == null || mListBrand.getAdapter().getItemCount() == 0) {
            BrandAdapter adapter = new BrandAdapter();
            adapter.setList(entity.getBrands().getItems());
            mListBrand.setAdapter(adapter);
            mListBrand.setVisibility(View.VISIBLE);
            adapter.setOnClickListener(v -> {
                int p = (int) v.getTag();
                if (!adapter.isSelected(p)) {
                    adapter.setSelected(p);
                    BrandItemEntity itemEntity = adapter.getItem(p);
                    mViewModel.setBrandEntity(itemEntity);
                    isBrandclick= true;
                    lazyLoad();
                }
            });
            mFilterViewHolder.mFilterView.setEnabled(true);
        }
        setFilterFragment(entity.getFilters());
        setProductList(entity.getList());
    }

    private void setFilterFragment(List<ProductFilterEntity> list) {
        if (mFilterKeyFragment==null) {
            mFilterKeyFragment = new FilterKeyFragment();
            Bundle bundle = new Bundle();
            bundle.putString(IntentBuilder.KEY_KEY, keyCode);
            bundle.putString(IntentBuilder.KEY_TYPE, mViewModel.getSort());
            bundle.putParcelableArrayList(IntentBuilder.KEY_DATA, new ArrayList<>(list));
            mFilterKeyFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    //.setCustomAnimations(R.anim.right_in, R.anim.right_out)
                    .add(R.id.drawer_right, mFilterKeyFragment, keyCode)
                    .show(mFilterKeyFragment)
                    .commitAllowingStateLoss();
        } else if ( isBrandclick){
            mFilterKeyFragment.setFields(new ArrayList<>(list));
        }

        isBrandclick = false;
    }

    private void setProductList(List<ProductEntity> list) {
        if (list.size() > 0) {
            mAdapter.setNewData(list);
        } else {
            emptySearch();
        }

    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        if (mFilterKeyFragment != null && mFilterKeyFragment.isAdded()) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .hide(mFilterKeyFragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (mFilterKeyFragment != null && mFilterKeyFragment.isAdded()) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .show(mFilterKeyFragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void lazyLoad() {
        mRecyclerView.setEnabled(false);
        mAdapter.setEmptyView(mRecyclerView.getProgressView());
        mAdapter.setNewData(Lists.newArrayList());
        refresh();
    }

    private void refresh() {
        mViewModel.search(
                this::setProductSearchEntity,
                mAdapter::setNewData,
                mRecyclerView::setLoadMore);
    }

    private void emptySearch() {
        mRecyclerView.setEnabled(true);
        EmptyViewHolder holder = EmptyViewHolder.createHolder(getContext())
                .setIcon(R.drawable.vector_empty_search)
                .setTitle(R.string.text_search_none_products);
        mAdapter.setEmptyView(holder.itemView);
    }

    @Override
    public boolean onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
            return true;
        }
        return false;
    }
}

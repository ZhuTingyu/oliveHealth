package com.olive.ui.search;

import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.biz.widget.recyclerview.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.model.ProductsModel;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.BaseLoadMoreViewModel;
import com.olive.ui.main.home.ProductsViewModel;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/2.
 */

public class SearchViewModel extends ProductsViewModel {

    private String keyWord;
    private String categoryCode;
    private int sort;
    private int order;


    public static final int TYPE_RANK_SYNTHESIZE = 0;
    public static final int TYPE_RANK_SALE = 1;
    public static final int TYPE_RANK_PRICE = 2;

    private static final int TYPE_DOWN_ORDER = 0;
    private static final int TYPE_UP_ORDER = 1;


    public SearchViewModel(Object activity) {
        super(activity);
        keyWord = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_VALUE);
        categoryCode = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_DATA);
    }

    public void getProductList(Action1<List<ProductEntity>> action1) {
        submitRequestThrowError(ProductsModel.productList(keyWord, categoryCode, page, sort, order).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void search(Action1<Object> action1){
        cleanPage();
        getProductList(productEntities -> {
            Observable.just(new Object()).subscribe(action1);
            BaseQuickAdapter adapter = (BaseQuickAdapter) recyclerView.getAdapter();
            adapter.setNewData(productEntities);
        });
    }


    public Action1<String> setKeyWord(){
        return s -> {
          keyWord = s;
        };
    }

    public void setRecyclerView(XRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void setLoadMore(Action1<Object> action1) {
        page++;
        getProductList(productEntities -> {
            loadMore(productEntities, action1);
        });
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setOrderUp(){
        order = TYPE_UP_ORDER;
    }

    public void setOrderDOWN(){
        order = TYPE_DOWN_ORDER;
    }

}

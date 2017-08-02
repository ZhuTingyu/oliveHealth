package com.olive.ui.search;

import com.biz.base.BaseViewModel;
import com.biz.base.RestErrorInfo;
import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.biz.widget.recyclerview.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.ProductsModel;
import com.olive.model.entity.ProductEntity;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/2.
 */

public class SearchViewModel extends BaseViewModel {

    private String keyWord;
    private String categoryCode;
    private int page = 1;
    private int sort;
    private int order;

    private XRecyclerView recyclerView;

    public static final int TYPE_RANK_SYNTHESIZE = 0;
    public static final int TYPE_RANK_SALE = 1;
    public static final int TYPE_RANK_PRICE = 2;

    private static final int TYPE_DOWN_ORDER = 0;
    private static final int TYPE_UP_ORDER = 1;


    public SearchViewModel(Object activity) {
        super(activity);
        keyWord = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_VALUE);
    }

    public void getProductList(Action1<List<ProductEntity>> action1) {
        submitRequestThrowError(ProductsModel.productList(keyWord, categoryCode, page, sort, order).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1);
    }


    public void loadMore(Action1<Object> action1) {
        getProductList(productEntities -> {
            Observable.just(new Object()).subscribe(action1);
            if (productEntities.isEmpty()) {
                error.onNext(new RestErrorInfo(getString(R.string.message_no_more)));
                recyclerView.setLoadMore(true);
            } else {
                BaseQuickAdapter adapter = (BaseQuickAdapter) recyclerView.getAdapter();
                adapter.addData(productEntities);
                recyclerView.setLoadMore(false);
            }
        });
    }

    public void search(Action1<Object> action1){
        cleanPage();
        getProductList(productEntities -> {
            Observable.just(new Object()).subscribe(action1);
            BaseQuickAdapter adapter = (BaseQuickAdapter) recyclerView.getAdapter();
            adapter.setNewData(productEntities);
        });
    }

    public void cleanPage(){
        page = 1;
    }

    public Action1<String> setKeyWord(){
        return s -> {
          keyWord = s;
        };
    }

    public void setRecyclerView(XRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
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

    public void addPage() {
        page++;
    }

    public void setOrderUp(){
        order = TYPE_UP_ORDER;
    }

    public void setOrderDOWN(){
        order = TYPE_DOWN_ORDER;
    }

}

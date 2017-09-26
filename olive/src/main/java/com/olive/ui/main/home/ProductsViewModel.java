package com.olive.ui.main.home;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.Lists;
import com.biz.util.ToastUtils;
import com.olive.R;
import com.olive.model.CartModel;
import com.olive.model.ProductsModel;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.BaseLoadMoreViewModel;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/4.
 */

public  class ProductsViewModel extends BaseLoadMoreViewModel {

    public String productNo;
    protected List<ProductEntity> addProductList;

    public static final int IS_FAVORITE = 1;
    public static final int NOT_FAVORITE = 0;


    public ProductsViewModel(Object activity) {
        super(activity);
    }


    public void addProductFavorites(Action1<String> action1){
        getActivity().setProgressVisible(true);
        submitRequestThrowError(ProductsModel.addProductFavorites(productNo).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),s -> {
            getActivity().setProgressVisible(false);
            ToastUtils.showShort(getActivity(),getString(R.string.text_add_favorites));
            Observable.just("").subscribe(action1);
        });
    }

    public void cancelProductFavorites(Action1<String> action1){
        getActivity().setProgressVisible(true);
        submitRequestThrowError(ProductsModel.cancelProductFavorites(productNo).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),s -> {
            getActivity().setProgressVisible(false);
            ToastUtils.showShort(getActivity(), getString(R.string.text_cancel_favorites));
            Observable.just("").subscribe(action1);
        });
    }

    public void addCart(Action1<String> action1){
        submitRequestThrowError(CartModel.addCart(addProductList).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public void setAddProductList(ProductEntity product) {
        this.addProductList = Lists.newArrayList(product);
    }

    public void setAddProductList(List<ProductEntity> products) {
        this.addProductList = products;
    }
}

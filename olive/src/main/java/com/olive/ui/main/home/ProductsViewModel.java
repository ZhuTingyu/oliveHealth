package com.olive.ui.main.home;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.Lists;
import com.olive.model.CartModel;
import com.olive.model.ProductsModel;
import com.olive.model.entity.ProductEntity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/4.
 */

public class ProductsViewModel extends BaseViewModel {

    private String productNo;
    private List<ProductEntity> addProductList;


    public ProductsViewModel(Object activity) {
        super(activity);
    }

    public void addProductFavorites(Action1<String> action1){
        submitRequestThrowError(ProductsModel.addProductfavorites(productNo).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
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
}

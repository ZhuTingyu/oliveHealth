package com.olive.ui.favorite;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.model.ProductsModel;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.main.home.ProductsViewModel;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/8.
 */

public class FavoriteViewModel extends ProductsViewModel {
    public FavoriteViewModel(Object activity) {
        super(activity);
    }

    @Override
    public void setLoadMore(Action1<Object> action1) {

    }

    public void getFavoriteList(Action1<List<ProductEntity>> action1){
        submitRequestThrowError(ProductsModel.favoriteList().map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }
}

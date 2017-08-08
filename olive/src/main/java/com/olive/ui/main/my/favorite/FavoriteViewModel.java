package com.olive.ui.main.my.favorite;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.model.ProductsModel;
import com.olive.model.entity.ProductEntity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/8.
 */

public class FavoriteViewModel extends BaseViewModel {
    public FavoriteViewModel(Object activity) {
        super(activity);
    }

    public void getFavoriteList(Action1<List<ProductEntity>> action1){
        submitRequestThrowError(ProductsModel.favoriteList().map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }
}

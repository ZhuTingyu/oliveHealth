package com.olive.ui.main.my.stock;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.model.ProductsModel;
import com.olive.model.entity.ProductEntity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/9.
 */

public class StockViewModel extends BaseViewModel {

    private String key;

    public StockViewModel(Object activity) {
        super(activity);
    }

    public void getStockList(Action1<List<ProductEntity>> action1){
        submitRequestThrowError(ProductsModel.stockList(key).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public Action1<String> setKey(){
        return s -> {
          key = s;
        };
    }

}

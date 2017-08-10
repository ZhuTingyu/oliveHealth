package com.olive.ui.refund.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.model.ProductsModel;
import com.olive.model.entity.ProductEntity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/10.
 */

public class ChooseRefundGoodViewModel extends BaseViewModel {

    public ChooseRefundGoodViewModel(Object activity) {
        super(activity);
    }

    public void chooseRefundProductsList(Action1<List<ProductEntity>> action1){
        submitRequestThrowError(ProductsModel.chooseRefundProductsList().map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

}

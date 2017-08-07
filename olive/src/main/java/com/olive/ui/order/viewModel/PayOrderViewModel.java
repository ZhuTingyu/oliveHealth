package com.olive.ui.order.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.model.AccountModel;
import com.olive.model.entity.BankEntity;
import com.olive.model.entity.OrderEntity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class PayOrderViewModel extends BaseViewModel {

    private OrderEntity orderEntity;

    public PayOrderViewModel(Object activity) {
        super(activity);
    }

    public void getBankCards(Action1<List<BankEntity>> action1){
        submitRequestThrowError(AccountModel.bankCards().map(r -> {
            if(r.isOk()){
               return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

}

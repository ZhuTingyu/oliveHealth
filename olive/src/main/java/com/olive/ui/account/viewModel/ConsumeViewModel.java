package com.olive.ui.account.viewModel;

import com.biz.http.HttpErrorException;
import com.olive.model.OrderModel;
import com.olive.model.entity.OrderEntity;
import com.olive.ui.BaseLoadMoreViewModel;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/9.
 */

public class ConsumeViewModel extends BaseLoadMoreViewModel {

    public ConsumeViewModel(Object activity) {
        super(activity);
    }

    @Override
    public void setLoadMore(Action1<Object> action1) {
        super.setLoadMore(action1);
        getConsumerDetails(orderEntities -> {
            loadMore(orderEntities, action1);
        });
    }

    public void getConsumerDetails(Action1<List<OrderEntity>> action1){
        submitRequestThrowError(OrderModel.orderConsumeDetail(page).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public void getDebtDetails(Action1<List<OrderEntity>> action1){
        submitRequestThrowError(OrderModel.orderDebtDetail().map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

}

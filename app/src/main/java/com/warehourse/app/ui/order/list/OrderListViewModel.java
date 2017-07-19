package com.warehourse.app.ui.order.list;


import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.warehourse.app.model.OrderModel;
import com.warehourse.app.model.entity.OrderEntity;
import com.warehourse.app.ui.order.BaseOrderOperationViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by wangwei on 2016/3/23.
 */
class OrderListViewModel extends BaseOrderOperationViewModel {
    private String lastFlag ="";
    private int status = OrderEntity.STATUS_ALL;

    public OrderListViewModel(Object activity) {
        super(activity);

    }

    public OrderListViewModel(Object activity, int status) {
        super(activity);
        this.status = status;
    }

    public void refresh(Action1<List<OrderEntity>> onNext,  Action1<Boolean> isMore){
        lastFlag = "";
        submitRequestThrowError(OrderModel.list(status, lastFlag).map(r->{
            if (r.isOk()) {
                lastFlag = r.data.lastFlag;
                return r.data.list==null?new ArrayList<OrderEntity>():r.data.list;
            }else throw new HttpErrorException(r);
        }), list->{
            onNext.call(list);
            Observable.just(list.isEmpty()).subscribe(isMore);
        });
    }


    public void loadMore(Action1<List<OrderEntity>> onNext, Action1<Boolean> isMore) {
        submitRequestThrowError(OrderModel.list(status, lastFlag).map(r -> {
            if (r.isOk()) {
                lastFlag = r.data.lastFlag;
                return r.data.list==null?new ArrayList<OrderEntity>():r.data.list;
            }else throw new HttpErrorException(r);
        }), list -> {

            onNext.call(list);
            Observable.just(list.isEmpty()).subscribe(isMore);
        });
    }
}

package com.olive.ui.refund.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.model.RefundModel;
import com.olive.model.entity.OrderEntity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/10.
 */

public class RefundListViewModel extends BaseViewModel {

    public RefundListViewModel(Object activity) {
        super(activity);
    }

    public void getRefundApplyList(Action1<List<OrderEntity>> action1){
        submitRequestThrowError(RefundModel.refundApplyList().map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public void getRefundList(Action1<List<OrderEntity>> action1){
        submitRequestThrowError(RefundModel.refundList().map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

}

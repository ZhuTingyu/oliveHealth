package com.olive.ui.refund.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.olive.model.RefundModel;
import com.olive.model.entity.OrderEntity;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/11.
 */

public class LookApplyRefundDetailViewModel extends BaseViewModel {

    String orderNo;

    public static final int STATUS_WAIT_CHECK = 1;
    public static final int STATUS_PASS_CHECK = 2;
    public static final int STATUS_NOT_PASS_CHECK = 3;

    public LookApplyRefundDetailViewModel(Object activity) {
        super(activity);
        orderNo = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_VALUE);
    }

    public void getApplyRefundDetail(Action1<OrderEntity> action1){
        submitRequestThrowError(RefundModel.applyRefundDetail(orderNo).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

}

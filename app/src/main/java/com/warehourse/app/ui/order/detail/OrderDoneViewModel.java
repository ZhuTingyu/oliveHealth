package com.warehourse.app.ui.order.detail;


import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.warehourse.app.model.OrderModel;
import com.warehourse.app.model.entity.OrderPreviewParaEntity;
import com.warehourse.app.model.entity.OrderStatusEntity;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by wangwei on 2016/3/27.
 */
public class OrderDoneViewModel extends BaseViewModel {
    private String orderId;
    private OrderStatusEntity orderPayStatusInfo;

    public OrderDoneViewModel(Object activity) {
        super(activity);
        orderId = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_ID);
    }

    public void request(Action1<Boolean> onNext) {
        submitRequestThrowError(OrderModel.queryPayStatus(orderId).map(r -> {
            if (r.isOk()) {
                orderPayStatusInfo = r.data;
               return true;
            } else throw new HttpErrorException(r);
        }), onNext);
    }

    public boolean isMoneyPay() {
        if (orderPayStatusInfo == null) return false;
        if (orderPayStatusInfo.paymentType == OrderPreviewParaEntity.TYPE_PAY_WEIXIN
                || orderPayStatusInfo.paymentType == OrderPreviewParaEntity.TYPE_PAY_ALIPAY)
            return true;
        return false;
    }

    public boolean isPayOk() {
        if (orderPayStatusInfo == null) return false;
        if (orderPayStatusInfo.paymentType == OrderPreviewParaEntity.TYPE_PAY_WEIXIN
                || orderPayStatusInfo.paymentType == OrderPreviewParaEntity.TYPE_PAY_ALIPAY) {
            return orderPayStatusInfo.payStatus == 100 || orderPayStatusInfo.payStatus == 30;
        }
        return false;
    }

    public String getPayStatusName() {
        if (orderPayStatusInfo == null) return "";
        return orderPayStatusInfo.payStatusName;
    }

    public String getPaymentTypeName() {
        if (orderPayStatusInfo == null) return "";
        return orderPayStatusInfo.paymentTypeName;
    }

    public boolean isArrived() {
        if (orderPayStatusInfo == null) return false;
        return orderPayStatusInfo.paymentType == OrderPreviewParaEntity.TYPE_PAY_DELIVER;
    }
}

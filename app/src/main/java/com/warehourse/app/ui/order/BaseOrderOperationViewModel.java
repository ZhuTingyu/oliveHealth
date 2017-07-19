package com.warehourse.app.ui.order;

import com.biz.http.HttpErrorException;
import com.biz.http.ResponseJson;
import com.warehourse.app.R;
import com.warehourse.app.model.OrderModel;
import com.warehourse.app.model.entity.AlipayPayEntity;
import com.warehourse.app.model.entity.OrderEntity;
import com.warehourse.app.model.entity.OrderPreviewParaEntity;
import com.warehourse.app.model.entity.WeiXinPayEntity;
import com.warehourse.app.ui.base.BasePayViewModel;

import android.app.Activity;

import rx.Observable;
import rx.functions.Action1;

/**
 * Title: BaseOrderOperationViewModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/6/1  15:31
 *
 * @author wangwei
 * @version 1.0
 */
public class BaseOrderOperationViewModel extends BasePayViewModel {
    private String operationOrderId;

    public BaseOrderOperationViewModel(Object activity) {
        super(activity);
    }

    public void setOperationOrderId(String operationOrderId) {
        this.operationOrderId = operationOrderId;
    }
    public void clearPayStatus(){
        orderId="";
    }

    @Override
    protected Observable<ResponseJson<AlipayPayEntity>> getAlipayPay() {
        return OrderModel.rePayAlipay(operationOrderId);
    }

    @Override
    protected Observable<ResponseJson<WeiXinPayEntity>> getWeiXinPay() {
        return OrderModel.rePayWeiXin(operationOrderId);
    }

    public void rePay(Activity activity, OrderEntity orderEntity) {
        if (orderEntity == null) return;
        if (orderEntity.paymentType == OrderPreviewParaEntity.TYPE_PAY_ALIPAY) {
            alipayPay(activity);
        } else if (orderEntity.paymentType == OrderPreviewParaEntity.TYPE_PAY_WEIXIN) {
            weixinPay();
        } else {
            error.onNext(getErrorString(R.string.text_error_pay));
        }

    }

    public void cancelOrder(Action1<Boolean> onNext) {
        submitRequestThrowError(OrderModel.cancel(operationOrderId).map(r -> {
            if (r.isOk()) {
                return true;
            } else throw new HttpErrorException(r);
        }), onNext);
    }
}

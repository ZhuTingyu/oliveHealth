package com.warehourse.app.ui.base;

import com.biz.base.BaseViewModel;
import com.biz.base.RestErrorInfo;
import com.biz.http.ResponseJson;
import com.biz.share.weixin.SendWX;
import com.biz.share.weixin.WeiXinPayEvent;
import com.warehourse.app.R;
import com.warehourse.app.model.OrderModel;
import com.warehourse.app.model.entity.AlipayPayEntity;
import com.warehourse.app.model.entity.PayCompleteEntity;
import com.warehourse.app.model.entity.WeiXinPayEntity;

import android.app.Activity;
import android.text.TextUtils;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by wangwei on 2016/3/26.
 */
public abstract class BasePayViewModel extends BaseViewModel {
    protected String orderId;
    private Action1<PayCompleteEntity> payComplete;

    public BasePayViewModel(Object activity) {
        super(activity);
        EventBus.getDefault().register(this);
    }

    public void setPayComplete(Action1<PayCompleteEntity> payComplete) {
        this.payComplete = payComplete;
    }

    protected void payComplete(String orderId, String message) {
        if (payComplete != null) {
            Observable.just(new PayCompleteEntity(orderId, message)).subscribe(payComplete);
        }
    }

    protected void payComplete(String orderId) {
        if (payComplete != null) {
            Observable.just(new PayCompleteEntity(orderId)).subscribe(payComplete);
        }
    }

    protected abstract Observable<ResponseJson<AlipayPayEntity>> getAlipayPay();

    protected void clearCart() {
    }

    public void alipayPay(Activity activity) {
        submitRequest(getAlipayPay(), e -> {
            if (e.isOk()) {
                clearCart();
                alipay(activity, e.data);
            } else {
                error.onNext(getErrorString(e.msg));
            }
        }, throwable -> error.onNext(getError(throwable)));
    }

    protected void alipay(Activity activity, AlipayPayEntity alipayPayInfo) {
        submitRequest(OrderModel.payAlipay(alipayPayInfo.getAlipayPayString(), activity), a -> {
            if ("9000".equals(a.getResultStatus())) {
                payComplete(alipayPayInfo.orderId);
            } else if ("8000".equals(a.getResultStatus())) {
                payComplete(alipayPayInfo.orderId, getString(R.string.resultcode_alipay_ERROR_8000));
            } else if ("4000".equals(a.getResultStatus())) {
                payComplete(alipayPayInfo.orderId, getString(R.string.resultcode_alipay_ERROR_4000));
            } else if ("6001".equals(a.getResultStatus())) {
                payComplete(alipayPayInfo.orderId, getString(R.string.resultcode_alipay_ERROR_6001));
            } else if ("6002".equals(a.getResultStatus())) {
                payComplete(alipayPayInfo.orderId, getString(R.string.resultcode_alipay_ERROR_6002));
            } else {
                payComplete(alipayPayInfo.orderId, getString(R.string.resultcode_alipay_ERROR_4000));
            }
        }, throwable -> error.onNext(new RestErrorInfo(getString(R.string.resultcode_alipay_ERROR_4000))));
    }

    protected abstract Observable<ResponseJson<WeiXinPayEntity>> getWeiXinPay();

    public void weixinPay() {
        submitRequest(getWeiXinPay(), e -> {
            if (e.isOk()) {
                clearCart();
                weixinPay(e.data);
            } else {
                error.onNext(new RestErrorInfo(e.msg));
            }
        }, throwable -> error.onNext(getError(throwable)));
    }

    protected void weixinPay(WeiXinPayEntity weiXinPayInfo) {
        orderId = weiXinPayInfo.orderId;
        weiXinPayInfo.extData = "" + orderId;
        getActivity().setProgressVisible(false);
        SendWX sendWX = new SendWX(getActivity());
        sendWX.payWeiXin(weiXinPayInfo.getPayReq());
    }


    public void onEventMainThread(WeiXinPayEvent event) {
        if (!TextUtils.isEmpty(event.orderId)&&event.orderId.equals(orderId)) {
            if (event.code == 0) {
                if (payComplete != null)
                    payComplete(orderId);
            } else {
                payComplete(orderId, event.code == -1 ? getString(R.string.resultcode_weixin_cancel) : getString(R.string.resultcode_weixin_error));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

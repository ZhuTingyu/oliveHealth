package com.warehourse.app.model;

import com.google.gson.reflect.TypeToken;

import com.alipay.sdk.app.PayTask;
import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.biz.share.weixin.SendWX;
import com.warehourse.app.R;
import com.warehourse.app.event.CartStatusChangeEvent;
import com.warehourse.app.event.OrderCountEvent;
import com.warehourse.app.model.entity.AlipayPayEntity;
import com.warehourse.app.model.entity.OrderApplyReturnEntity;
import com.warehourse.app.model.entity.OrderCountEntity;
import com.warehourse.app.model.entity.OrderEntity;
import com.warehourse.app.model.entity.OrderListEntity;
import com.warehourse.app.model.entity.OrderPreviewEntity;
import com.warehourse.app.model.entity.OrderPreviewParaEntity;
import com.warehourse.app.model.entity.OrderStatusEntity;
import com.warehourse.app.model.entity.PayResult;
import com.warehourse.app.model.entity.WeiXinPayEntity;
import com.warehourse.app.util.HttpRequest;

import android.app.Activity;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Title: OrderModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/26  15:46
 *
 * @author wangwei
 * @version 1.0
 */
public class OrderModel {
    private static OrderModel mOrderModel;
    private final CompositeSubscription subscription = new CompositeSubscription();

    public static OrderModel getInstance() {
        if (mOrderModel == null) {
            synchronized (OrderModel.class) {
                mOrderModel = new OrderModel();
            }
        }
        return mOrderModel;
    }

    public static Observable<ResponseJson<OrderListEntity>> list(int status, String lastFlag) {
        return HttpRequest.<ResponseJson<OrderListEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("status", status)
                .addBody("lastFlag", lastFlag)
                .url(R.string.api_order_list)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<OrderListEntity>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<ResponseJson<OrderEntity>> detail(String id) {
        return HttpRequest.<ResponseJson<OrderEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("id", id)
                .url(R.string.api_order_detail)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<OrderEntity>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<ResponseJson<OrderPreviewEntity>> preview(OrderPreviewParaEntity paraEntity) {
        return HttpRequest.<ResponseJson<OrderPreviewEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody(paraEntity.toPreviewJson())
                .url(R.string.api_order_preview)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<OrderPreviewEntity>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<ResponseJson<OrderEntity>> createDeliver(OrderPreviewParaEntity paraEntity) {
        return HttpRequest.<ResponseJson<OrderEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody(paraEntity.toCreateJson())
                .url(R.string.api_order_pay)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<OrderEntity>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        CartModel.postCartCount(0);
                        EventBus.getDefault().post(new CartStatusChangeEvent());
                        OrderModel.getInstance().updateOrderCount();
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<AlipayPayEntity>> createAlipay(OrderPreviewParaEntity paraEntity) {
        return HttpRequest.<ResponseJson<AlipayPayEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody(paraEntity.toCreateJson())
                .url(R.string.api_order_pay_alipay)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<AlipayPayEntity>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        CartModel.postCartCount(0);
                        EventBus.getDefault().post(new CartStatusChangeEvent());
                        OrderModel.getInstance().updateOrderCount();
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<AlipayPayEntity>> rePayAlipay(String orderId) {
        return HttpRequest.<ResponseJson<AlipayPayEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("id", orderId)
                .url(R.string.api_order_pay_re_alipay)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<AlipayPayEntity>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        OrderModel.getInstance().updateOrderCount();
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<WeiXinPayEntity>> createWeiXin(OrderPreviewParaEntity paraEntity) {
        return HttpRequest.<ResponseJson<WeiXinPayEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody(paraEntity.toCreateWeiXinJson())
                .url(R.string.api_order_pay_weixin)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<WeiXinPayEntity>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        CartModel.postCartCount(0);
                        EventBus.getDefault().post(new CartStatusChangeEvent());
                        OrderModel.getInstance().updateOrderCount();
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<WeiXinPayEntity>> rePayWeiXin(String orderId) {
        return HttpRequest.<ResponseJson<WeiXinPayEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("id", orderId)
                .addBody("appId", SendWX.getAppId())
                .addBody("tradeType", "app")
                .url(R.string.api_order_pay_re_weixin)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<WeiXinPayEntity>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        OrderModel.getInstance().updateOrderCount();
                    }
                    return r;
                });
    }


    public static Observable<ResponseJson<OrderStatusEntity>> queryPayStatus(String orderId) {
        return HttpRequest.<ResponseJson<OrderStatusEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("id", orderId)
                .url(R.string.api_order_pay_query_status)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<OrderStatusEntity>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        OrderModel.getInstance().updateOrderCount();
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<Object>> cancel(String orderId) {
        return HttpRequest.<ResponseJson<Object>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("id", orderId)
                .url(R.string.api_order_cancel)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<Object>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        OrderModel.getInstance().updateOrderCount();
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<Object>> applyReturn(OrderApplyReturnEntity orderApplyReturnEntity) {
        return HttpRequest.<ResponseJson<Object>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody(orderApplyReturnEntity.toJson())
                .url(R.string.api_order_apply_return)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<Object>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        OrderModel.getInstance().updateOrderCount();
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<OrderCountEntity>> getOrderCount() {
        return HttpRequest.<ResponseJson<OrderCountEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_order_count)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<OrderCountEntity>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<PayResult> payAlipay(String orderInfo, Activity activity) {
        return Observable.create(subscriber -> {
            PayTask alipay = new PayTask(activity);
            String result = alipay.pay(orderInfo, false);
            subscriber.onNext(result);
            subscriber.onCompleted();
        }).map(s -> {
            return new PayResult((String) s);
        });
    }


    public void updateOrderCount() {
        subscription.clear();
        subscription.add(getOrderCount()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderCountEntityResponseJson -> {
                    if (orderCountEntityResponseJson.isOk()) {
                        EventBus.getDefault().post(new OrderCountEvent(orderCountEntityResponseJson.data));
                    }
                }, throwable -> {
                }));
        subscription.add(UserModel.mainPoint().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(mainPointEvent -> {
                    EventBus.getDefault().post(mainPointEvent);
                }));
    }
}

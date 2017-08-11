package com.olive.model;

import com.biz.http.ResponseJson;
import com.google.gson.reflect.TypeToken;
import com.olive.R;
import com.olive.model.entity.OrderEntity;
import com.olive.model.entity.ProductEntity;
import com.olive.model.entity.RefundReasonEntity;
import com.olive.util.HttpRequest;

import java.util.List;

import rx.Observable;

/**
 * Created by TingYu Zhu on 2017/8/10.
 */

public class RefundModel {
    public static Observable<ResponseJson<List<OrderEntity>>> refundApplyList(){
        return HttpRequest.<ResponseJson<List<OrderEntity>>>builder()
                .setToJsonType(new TypeToken<ResponseJson<List<OrderEntity>>>() {
                }.getType())
                .url(R.string.api_refund_apply_list)
                .requestPI();
    }

    public static Observable<ResponseJson<List<OrderEntity>>> refundList(){
        return HttpRequest.<ResponseJson<List<OrderEntity>>>builder()
                .setToJsonType(new TypeToken<ResponseJson<List<OrderEntity>>>() {
                }.getType())
                .url(R.string.api_refund_list)
                .requestPI();
    }

    public static Observable<ResponseJson<String>> applyRefund(List<ProductEntity> products, int refundReasonId, List<String> image, String description){
        return HttpRequest.<ResponseJson<String>>builder()
                .setToJsonType(new TypeToken<ResponseJson<String>>() {
                }.getType())
                .addBody("products", products)
                .addBody("refundReasonId", refundReasonId)
                .addBody("image", image)
                .addBody("description", description)
                .url(R.string.api_refund_apply)
                .requestPI();
    }

    public static Observable<ResponseJson<List<RefundReasonEntity>>> applyRefundReasons(){
        return HttpRequest.<ResponseJson<List<RefundReasonEntity>>>builder()
                .setToJsonType(new TypeToken<ResponseJson<List<RefundReasonEntity>>>() {
                }.getType())
                .url(R.string.api_refund_apply_reasons)
                .requestPI();
    }
}

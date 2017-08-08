package com.olive.model;

import com.biz.http.ResponseJson;
import com.google.gson.reflect.TypeToken;
import com.olive.R;
import com.olive.model.entity.OrderEntity;
import com.olive.model.entity.ProductEntity;
import com.olive.util.HttpRequest;

import java.util.List;

import rx.Observable;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class OrderModel {

    public static Observable<ResponseJson<OrderEntity>> createOrder(List<ProductEntity> products, int addressId, String checkNumber){
        return HttpRequest.<ResponseJson<OrderEntity>>builder()
                .setToJsonType(new TypeToken<ResponseJson<OrderEntity>>() {
                }.getType())
                .addBody("products", products)
                .addBody("addressId", addressId)
                .addBody("checkNumber", checkNumber)
                .url(R.string.api_order_create)
                .requestPI();
    }

    public static Observable<ResponseJson<List<OrderEntity>>> orderList(int page, int status){
        return HttpRequest.<ResponseJson<List<OrderEntity>>>builder()
                .setToJsonType(new TypeToken<ResponseJson<List<OrderModel>>>() {
                }.getType())
                .addBody("page", page)
                .addBody("status", status)
                .url(R.string.api_order_list)
                .requestPI();
    }

    public static Observable<ResponseJson<String>> cancelOrder(String orderNo){
        return HttpRequest.<ResponseJson<String>>builder()
                .setToJsonType(new TypeToken<ResponseJson<String>>() {
                }.getType())
                .addBody("orderNo", orderNo)
                .url(R.string.api_order_cancel)
                .requestPI();
    }

    public static Observable<ResponseJson<String>> confireOrder(String orderNo){
        return HttpRequest.<ResponseJson<String>>builder()
                .setToJsonType(new TypeToken<ResponseJson<String>>() {
                }.getType())
                .addBody("orderNo", orderNo)
                .url(R.string.api_order_confirm)
                .requestPI();
    }

    public static Observable<ResponseJson<OrderEntity>> orderDetail(String orderNo){
        return HttpRequest.<ResponseJson<OrderEntity>>builder()
                .setToJsonType(new TypeToken<ResponseJson<OrderEntity>>() {
                }.getType())
                .addBody("orderNo", orderNo)
                .url(R.string.api_order_detail)
                .requestPI();
    }


}

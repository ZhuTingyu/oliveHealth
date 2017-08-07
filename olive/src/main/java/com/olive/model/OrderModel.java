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

}

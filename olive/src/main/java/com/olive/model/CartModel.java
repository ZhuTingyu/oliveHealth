package com.olive.model;

import com.biz.http.ResponseJson;
import com.google.gson.reflect.TypeToken;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.util.HttpRequest;

import java.util.List;

import rx.Observable;

/**
 * Created by TingYu Zhu on 2017/8/3.
 */

public class CartModel {
    public static Observable<ResponseJson<String>> addCart(List<ProductEntity> productEntities){
        return HttpRequest.<ResponseJson<String>>builder()
                .setToJsonType(new TypeToken<ResponseJson<String>>() {
                }.getType())
                .addBody("products", productEntities)
                .url(R.string.api_add_cart)
                .requestPI();
    }
}

package com.olive.model;

import android.support.annotation.Nullable;

import com.biz.http.ResponseJson;
import com.google.gson.reflect.TypeToken;
import com.olive.R;
import com.olive.model.entity.AdvertEntity;
import com.olive.model.entity.ProductEntity;
import com.olive.util.HttpRequest;

import java.util.List;

import rx.Observable;

/**
 * Created by TingYu Zhu on 2017/8/1.
 */

public class ProductsModel {

    public static Observable<ResponseJson<List<ProductEntity>>> recommendProductList(){
        return HttpRequest.<ResponseJson<List<ProductEntity>>>builder()
                .setToJsonType(new TypeToken<ResponseJson<List<ProductEntity>>>() {
                }.getType())
                .url(R.string.api_recommend_product_list)
                .requestPI();
    }

    public static Observable<ResponseJson<List<ProductEntity>>> productList(@Nullable String key, @Nullable String categoryCode, int page, int sort, int order){
        return HttpRequest.<ResponseJson<List<ProductEntity>>>builder()
                .setToJsonType(new TypeToken<ResponseJson<List<ProductEntity>>>() {
                }.getType())
                .addBody("key", key)
                .addBody("categoryCode", categoryCode)
                .addBody("page", page)
                .addBody("sort", sort)
                .addBody("order", order)
                .url(R.string.api_products_list)
                .requestPI();
    }


}

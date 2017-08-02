package com.olive.model;

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

}

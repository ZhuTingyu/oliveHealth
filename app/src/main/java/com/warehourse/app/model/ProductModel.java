package com.warehourse.app.model;

import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.google.gson.reflect.TypeToken;

import com.biz.http.UrlSinger;
import com.biz.util.UrlUtils;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.model.entity.ProductSearchEntity;
import com.warehourse.app.model.entity.ProductSearchParaEntity;
import com.warehourse.app.util.HttpRequest;

import rx.Observable;

/**
 * Title: ProductModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/23  10:37
 *
 * @author wangwei
 * @version 1.0
 */
public class ProductModel {
    public static Observable<ResponseJson<ProductEntity>> productDetail(String id)
    {
        return HttpRequest.<ResponseJson<ProductEntity>>builder()
                .addBody("id", id)
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_product_detail)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<ProductEntity>>() {
                }.getType())
                .requestPI();
    }


    public static Observable<ResponseJson<ProductSearchEntity>> search(ProductSearchParaEntity  searchParaEntity)
    {
        return HttpRequest.<ResponseJson<ProductSearchEntity>>builder()
                .addBody(searchParaEntity.toJson())
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_product_search)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<ProductSearchEntity>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<ResponseJson<ProductSearchEntity>> search(String url,
                                                                       ProductSearchParaEntity  searchParaEntity)
    {
        return HttpRequest.<ResponseJson<ProductSearchEntity>>builder()
                .addBody(searchParaEntity.toJson())
                .restMethod(RestMethodEnum.REST_POST)
                .url(UrlUtils.deleteBeginSeparator(url))
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<ProductSearchEntity>>() {
                }.getType())
                .requestPI();
    }
}

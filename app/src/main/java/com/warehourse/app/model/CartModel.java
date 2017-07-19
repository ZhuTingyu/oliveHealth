package com.warehourse.app.model;

import com.google.gson.reflect.TypeToken;

import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.warehourse.app.R;
import com.warehourse.app.event.CartCountEvent;
import com.warehourse.app.event.CartStatusChangeEvent;
import com.warehourse.app.model.entity.CartEntity;
import com.warehourse.app.model.entity.UserEntity;
import com.warehourse.app.util.HttpRequest;

import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Observable;

/**
 * Title: CartModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/26  14:51
 *
 * @author wangwei
 * @version 1.0
 */
public class CartModel {
    public static void postCartCount(int count) {
        EventBus.getDefault().post(new CartCountEvent(count));
        UserEntity userEntity = UserModel.getInstance().getUserEntity();
        userEntity.purchaseCount = count;
        UserModel.getInstance().setUserInfo(userEntity);
    }

    public static Observable<ResponseJson<CartEntity>> getCartCount() {
        return HttpRequest.<ResponseJson<CartEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_cart_get_count)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<CartEntity>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk() && r.data != null) {
                        CartModel.postCartCount(r.data.cartNum);
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<CartEntity>> list() {
        return HttpRequest.<ResponseJson<CartEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_cart_list)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<CartEntity>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk() && r.data != null) {
                        CartModel.postCartCount(r.data.cartNum);
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<CartEntity>> add(String productId, int quantity) {
        return HttpRequest.<ResponseJson<CartEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_cart_add)
                .addBody("quantity", quantity)
                .addBody("productId", productId)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<CartEntity>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk() && r.data != null) {
                        CartModel.postCartCount(r.data.cartNum);
                        EventBus.getDefault().post(new CartStatusChangeEvent());
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<CartEntity>> delete(List<String> productIds) {
        return HttpRequest.<ResponseJson<CartEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_cart_delete)
                .addBody("productIds", productIds)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<CartEntity>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk() && r.data != null) {
                        CartModel.postCartCount(r.data.cartNum);
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<CartEntity>> update(String productId, int quantity) {
        return HttpRequest.<ResponseJson<CartEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_cart_update)
                .addBody("quantity", quantity)
                .addBody("productId", productId)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<CartEntity>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk() && r.data != null) {
                        CartModel.postCartCount(r.data.cartNum);
                    }
                    return r;
                });
    }
}

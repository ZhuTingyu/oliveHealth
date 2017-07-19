package com.warehourse.app.model;

import android.text.TextUtils;

import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.biz.util.GsonUtil;
import com.biz.widget.picker.ProvinceEntity;
import com.google.gson.reflect.TypeToken;
import com.warehourse.app.R;
import com.warehourse.app.application.WareApplication;
import com.warehourse.app.event.UserEvent;
import com.warehourse.app.model.entity.AddressStatusEntity;
import com.warehourse.app.model.entity.ShopDetailEntity;
import com.warehourse.app.model.entity.ShopPhotoEntity;
import com.warehourse.app.model.entity.ShopTypeEntity;
import com.warehourse.app.model.entity.UserEntity;
import com.warehourse.app.util.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Observable;

/**
 * Title: ShopModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/18  10:44
 *
 * @author wangwei
 * @version 1.0
 */
public class ShopModel {
    public static Observable<ResponseJson<Object>> changeDeliveryName(String deliveryName) {
        return HttpRequest.<ResponseJson<Object>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("deliveryName", deliveryName)
                .url(R.string.api_shop_change_delivery_name)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<Object>>() {}.getType())
                .requestPI();
    }


    public static Observable<ResponseJson<ShopPhotoEntity>> shopPhoto() {
        return HttpRequest.<ResponseJson<ShopPhotoEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("shopId", UserModel.getInstance().getShopId())
                .url(R.string.api_user_latest_shop_photo)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<ShopPhotoEntity>>() {}.getType())
                .requestPI();
    }

    public static Observable<ResponseJson<Object>> saveQualification(String businessLicence,String shopPhoto,String liquorSellLicence,String corporateIdPhoto)
    {
        return HttpRequest.<ResponseJson<Object>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("shopId", UserModel.getInstance().getShopId())
                .addBody("businessLicence", businessLicence)
                .addBody("shopPhoto", shopPhoto)
                .addBody("liquorSellLicence", TextUtils.isEmpty(liquorSellLicence)?null:liquorSellLicence)
                .addBody("corporateIdPhoto", TextUtils.isEmpty(corporateIdPhoto)?null:corporateIdPhoto)
                .userId(UserModel.getInstance().getUserId())
                .url(R.string.api_shop_update_photo)
                .setToJsonType(new TypeToken<ResponseJson<Object>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        UserEntity userInfo = UserModel.getInstance().getUserEntity();
                        if (userInfo != null) {
                            userInfo.qualificationAuditStatus = 2;
                            UserModel.getInstance().setUserInfo(userInfo);
                        }
                    }
                    return r;
                });
    }

    private static Observable<String> getAreaJson() {
        return Observable.create(subscriber -> {
                    try {
                        StringBuffer sb = new StringBuffer();
                        InputStream is = WareApplication.getAppContext().getAssets().open("simple-geo.json");
                        int len = -1;
                        byte[] buf = new byte[is.available()];
                        while ((len = is.read(buf)) != -1) {
                            sb.append(new String(buf, 0, len, "utf-8"));
                        }
                        is.close();
                        subscriber.onNext(sb.toString());
                        subscriber.onCompleted();
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }
        );
    }

    public static Observable<List<ProvinceEntity>> getProvince() {
        return getAreaJson().map(s -> {
            List<ProvinceEntity> list =
                    GsonUtil.fromJson(s, new TypeToken<List<ProvinceEntity>>() {
                    }.getType());
            return list;
        });
    }
    public static Observable<ResponseJson<ShopDetailEntity>> shopDetail() {
        return HttpRequest.<ResponseJson<ShopDetailEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("shopId", UserModel.getInstance().getShopId())
                .userId(UserModel.getInstance().getUserId())
                .url(R.string.api_user_latest_shop_detail)
                .setToJsonType(new TypeToken<ResponseJson<ShopDetailEntity>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<ResponseJson<Object>> saveShopDetail(String name, long shopTypeId, int provinceId, int cityId, int districtId, String deliveryAddress,String corporateName) {
        return HttpRequest.<ResponseJson<Object>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("shopId", UserModel.getInstance().getShopId())
                .addBody("name", name)
                .addBody("shopTypeId", shopTypeId)
                .addBody("provinceId", provinceId)
                .addBody("cityId", cityId)
                .addBody("districtId", districtId)
                .addBody("deliveryAddress", deliveryAddress)
                .addBody("corporateName",corporateName)
                .userId(UserModel.getInstance().getUserId())
                .url(R.string.api_shop_update_detail)
                .setToJsonType(new TypeToken<ResponseJson<Object>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        UserEntity userInfo = UserModel.getInstance().getUserEntity();
                        if (userInfo != null) {
                            userInfo.detailAddress = deliveryAddress;
                            UserModel.getInstance().setUserInfo(userInfo);
                            EventBus.getDefault().post(new UserEvent(UserEvent.TYPE_REGISTER));
                        }
                    }
                    return r;
                });
    }


    public static Observable<ResponseJson<List<ShopTypeEntity>>> getShopTypes() {
        return HttpRequest.<ResponseJson<List<ShopTypeEntity>>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_shop_type)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<List<ShopTypeEntity>>>() {
                }.getType())
                .requestPI();
    }
    public static Observable<ResponseJson<Object>> updateShopAddressDetail(int provinceId, int cityId, int districtId, String deliveryAddress, String reason) {
        return HttpRequest.<ResponseJson<Object>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("shopId", UserModel.getInstance().getShopId())
                .addBody("provinceId", provinceId)
                .addBody("cityId", cityId)
                .addBody("districtId", districtId)
                .addBody("deliveryAddress", deliveryAddress)
                .addBody("reason", reason)
                .userId(UserModel.getInstance().getUserId())
                .url(R.string.api_shop_update_address)
                .setToJsonType(new TypeToken<ResponseJson<Object>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        UserEntity userInfo = UserModel.getInstance().getUserEntity();
                        if (userInfo != null) {
                            userInfo.detailAddress = deliveryAddress;
                            UserModel.getInstance().setUserInfo(userInfo);
                            EventBus.getDefault().post(new UserEvent(UserEvent.TYPE_CHANGE_ADDRESS));
                        }
                    }
                    return r;
                });
    }

    public  static Observable<ResponseJson<AddressStatusEntity>> getUpdateAddressStatus()
    {
        return HttpRequest.<ResponseJson<AddressStatusEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_shop_update_address_status)
                .userId(UserModel.getInstance().getUserId())
                .addBody("shopId",UserModel.getInstance().getShopId())
                .setToJsonType(new TypeToken<ResponseJson<AddressStatusEntity>>() {
                }.getType())
                .requestPI();
    }

}

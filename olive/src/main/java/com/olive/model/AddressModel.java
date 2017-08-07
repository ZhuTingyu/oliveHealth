package com.olive.model;

import com.biz.http.ResponseJson;
import com.google.gson.reflect.TypeToken;
import com.olive.R;
import com.olive.model.entity.AddressEntity;
import com.olive.model.entity.ProductEntity;
import com.olive.util.HttpRequest;

import java.util.List;

import rx.Observable;

/**
 * Created by TingYu Zhu on 2017/8/6.
 */

public class AddressModel {

    public static Observable<ResponseJson<List<AddressEntity>>> AddressList(){
        return HttpRequest.<ResponseJson<List<AddressEntity>>>builder()
                .setToJsonType(new TypeToken<ResponseJson<List<AddressEntity>>>() {
                }.getType())
                .url(R.string.api_address_list)
                .requestPI();
    }

    public static Observable<ResponseJson<String>> AddressDelete(int id){
        return HttpRequest.<ResponseJson<String>>builder()
                .setToJsonType(new TypeToken<ResponseJson<String>>() {
                }.getType())
                .url(R.string.api_address_delete)
                .requestPI();
    }

    public static Observable<ResponseJson<AddressEntity>> AddressUpdate(AddressEntity entity){
        return HttpRequest.<ResponseJson<AddressEntity>>builder()
                .setToJsonType(new TypeToken<ResponseJson<AddressEntity>>() {
                }.getType())
                .addBody("id", entity.id)
                .addBody("consignee", entity.consignee)
                .addBody("mobile", entity.mobile)
                .addBody("provinceCode", entity.province)
                .addBody("cityCode", entity.city)
                .addBody("districtCode", entity.district)
                .addBody("detailAddress", entity.detailAddress)
                .addBody("isDefault", entity.isDefault)
                .url(R.string.api_address_update)
                .requestPI();
    }

}

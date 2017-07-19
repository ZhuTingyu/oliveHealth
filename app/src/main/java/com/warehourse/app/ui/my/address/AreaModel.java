package com.warehourse.app.ui.my.address;

import com.google.gson.reflect.TypeToken;

import com.biz.http.Request;
import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.AreaInfo;

import java.util.List;

import rx.Observable;

/**
 * Title: AreaModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2016/9/6  14:49
 *
 * @author wenfeng
 * @version 1.0
 */
public class AreaModel {

    public static Observable<ResponseJson<List<AreaInfo>>> getProvince() {
        return Request.<ResponseJson<List<AreaInfo>>>builder()
                .url(R.string.api_geo_find_provinces)
                .userId(UserModel.getInstance().getUserId())
                .restMethod(RestMethodEnum.REST_POST)
                .setToJsonType(new TypeToken<ResponseJson<List<AreaInfo>>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<ResponseJson<List<AreaInfo>>> getCity(long id) {
        return Request.<ResponseJson<List<AreaInfo>>>builder()
                .addBody("id",id)
                .url(R.string.api_geo_find_city)
                .userId(UserModel.getInstance().getUserId())
                .restMethod(RestMethodEnum.REST_POST)
                .setToJsonType(new TypeToken<ResponseJson<List<AreaInfo>>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<ResponseJson<List<AreaInfo>>> getDistrict(long id) {
        return Request.<ResponseJson<List<AreaInfo>>>builder()
                .addBody("id",id)
                .url(R.string.api_geo_find_district)
                .userId(UserModel.getInstance().getUserId())
                .restMethod(RestMethodEnum.REST_POST)
                .setToJsonType(new TypeToken<ResponseJson<List<AreaInfo>>>() {
                }.getType())
                .requestPI();
    }


}

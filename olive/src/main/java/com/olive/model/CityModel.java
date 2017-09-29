package com.olive.model;

import android.support.annotation.Nullable;

import com.biz.http.ResponseJson;
import com.google.gson.reflect.TypeToken;
import com.olive.R;
import com.olive.model.entity.CityEntity;
import com.olive.util.HttpRequest;

import java.util.List;

import rx.Observable;

/**
 * Created by TingYu Zhu on 2017/8/13.
 */

public class CityModel {
    public static Observable<ResponseJson<List<CityEntity>>> cityList(@Nullable String parentCode) {
        return HttpRequest.<ResponseJson<List<CityEntity>>>builder()
                .setToJsonType(new TypeToken<ResponseJson<List<CityEntity>>>() {
                }.getType())
                .addBody("parentCode", parentCode)
                .url(R.string.api_cities_list)
                .requestPI();
    }
}

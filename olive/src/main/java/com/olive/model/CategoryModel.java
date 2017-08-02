package com.olive.model;

import com.biz.http.ResponseJson;
import com.google.gson.reflect.TypeToken;
import com.olive.R;
import com.olive.model.entity.CategoryEntity;
import com.olive.util.HttpRequest;

import java.util.List;

import rx.Observable;

/**
 * Created by TingYu Zhu on 2017/8/2.
 */

public class CategoryModel {

    public static Observable<ResponseJson<List<CategoryEntity>>> categroyList(int type){
        return HttpRequest.<ResponseJson<List<CategoryEntity>>>builder()
                .setToJsonType(new TypeToken<ResponseJson<List<CategoryEntity>>>() {
                }.getType())
                .addBody("type", type)
                .url(R.string.api_category_ist)
                .requestPI();
    }
}

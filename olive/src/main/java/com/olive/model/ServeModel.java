package com.olive.model;

import android.support.annotation.Nullable;

import com.biz.http.ResponseJson;
import com.google.gson.reflect.TypeToken;
import com.olive.R;
import com.olive.model.entity.CityEntity;
import com.olive.model.entity.ServeEntity;
import com.olive.util.HttpRequest;

import org.json.JSONObject;

import java.util.List;

import rx.Observable;

/**
 * Created by TingYu Zhu on 2017/8/25.
 */

public class ServeModel {

    public static Observable<ResponseJson<ServeEntity>> serveContent() {
        return HttpRequest.<ResponseJson<ServeEntity>>builder()
                .setToJsonType(new TypeToken<ResponseJson<ServeEntity>>() {
                }.getType())
                .url(R.string.api_serveContent)
                .requestPI();
    }
}

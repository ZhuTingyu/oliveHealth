package com.olive.model;


import com.biz.http.ResponseJson;
import com.google.gson.reflect.TypeToken;
import com.olive.R;
import com.olive.model.entity.AdvertEntity;
import com.olive.model.entity.NoticeDetailEntity;
import com.olive.model.entity.NoticeEntity;
import com.olive.util.HttpRequest;

import java.util.List;

import rx.Observable;

/**
 * Created by TingYu Zhu on 2017/8/1.
 */

public class NoticeModel {

    public static Observable<ResponseJson<List<AdvertEntity>>> advertList() {
        return HttpRequest.<ResponseJson<List<AdvertEntity>>>builder()
                .setToJsonType(new TypeToken<ResponseJson<List<AdvertEntity>>>() {
                }.getType())
                .url(R.string.api_advert_list)
                .requestPI();
    }


    public static Observable<ResponseJson<List<NoticeEntity>>> noticeList(int page) {
        return HttpRequest.<ResponseJson<List<NoticeEntity>>>builder()
                .setToJsonType(new TypeToken<ResponseJson<List<NoticeEntity>>>() {
                }.getType())
                .url(R.string.api_notice_list)
                .addBody("page", page)
                .requestPI();
    }

    public static Observable<ResponseJson<NoticeDetailEntity>> noticeDetail(int id) {
        return HttpRequest.<ResponseJson<NoticeDetailEntity>>builder()
                .setToJsonType(new TypeToken<ResponseJson<NoticeDetailEntity>>() {
                }.getType())
                .url(R.string.api_notice_detail)
                .addBody("id", id)
                .requestPI();
    }

}

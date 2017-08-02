package com.olive.ui.main.home;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.Lists;
import com.olive.model.NoticeModel;
import com.olive.model.entity.AdvertEntity;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/1.
 */

public class HomeViewModel extends BaseViewModel {

    public static final int TYPE_GOODS = 0;
    public static final int TYPE_NOTICE = 1;

    private int type = 0;
    private ArrayList<AdvertEntity> goodsList;
    private ArrayList<String> noticeList;

    public HomeViewModel(Object activity) {
        super(activity);
    }

    public void getAvertList(Action1<List<AdvertEntity>> action1){
        submitRequestThrowError(NoticeModel.AdvertList().map(r -> {
            if(r.isOk()){
                List<AdvertEntity> list = r.data;
                getLists(list);
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    private void getLists(List<AdvertEntity> list) {
        goodsList = Lists.newArrayList();
        noticeList = Lists.newArrayList();
        for(AdvertEntity entity : list){
            if(TYPE_GOODS == entity.type){
                goodsList.add(entity);
            }else {
                noticeList.add(entity.image);
            }
        }
    }

    public ArrayList<AdvertEntity> getGoodsList() {
        return goodsList;
    }

    public ArrayList<String> getNoticeImageList() {
        return noticeList;
    }
}

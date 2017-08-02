package com.olive.ui.notice;

import android.view.View;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.model.NoticeModel;
import com.olive.model.entity.NoticeEntity;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;


/**
 * Created by TingYu Zhu on 2017/8/1.
 */

public class NoticeViewModel extends BaseViewModel{

    private int page;

    public NoticeViewModel(Object activity) {
        super(activity);
    }

    public void getNoticeList(Action1<List<NoticeEntity>> action1){
        submitRequestThrowError(NoticeModel.NoticeList(page).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public void setPage(int page) {
        this.page = page;
    }
}

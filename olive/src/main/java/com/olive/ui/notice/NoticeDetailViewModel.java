package com.olive.ui.notice;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.olive.model.NoticeModel;
import com.olive.model.entity.NoticeDetailEntity;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/1.
 */

public class NoticeDetailViewModel extends BaseViewModel {

    private int id;

    public NoticeDetailViewModel(Object activity) {
        super(activity);
        id = getActivity().getIntent().getIntExtra(IntentBuilder.KEY_VALUE,0);
    }

    public void getNoticeDetail(Action1<NoticeDetailEntity> action1){
        submitRequestThrowError(NoticeModel.noticeDetail(id).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

}

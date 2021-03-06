package com.olive.ui.notice;

import android.view.View;

import com.biz.base.BaseViewModel;
import com.biz.base.RestErrorInfo;
import com.biz.http.HttpErrorException;
import com.biz.widget.recyclerview.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.NoticeModel;
import com.olive.model.entity.NoticeEntity;
import com.olive.ui.BaseLoadMoreViewModel;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;


/**
 * Created by TingYu Zhu on 2017/8/1.
 */

public class NoticeViewModel extends BaseLoadMoreViewModel {

    public NoticeViewModel(Object activity) {
        super(activity);
    }

    public void getNoticeList(Action1<List<NoticeEntity>> action1) {
        submitRequestThrowError(NoticeModel.noticeList(page).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    @Override
    public void setLoadMore(Action1<Object> action1) {
        super.setLoadMore(action1);
        getNoticeList(noticeEntities -> {
            loadMore(noticeEntities,action1);
        });
    }
}

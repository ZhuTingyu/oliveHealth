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

import java.util.List;

import rx.Observable;
import rx.functions.Action1;


/**
 * Created by TingYu Zhu on 2017/8/1.
 */

public class NoticeViewModel extends BaseViewModel {

    public int page = 1;
    private XRecyclerView recyclerView;

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

    public void setPage(int page) {
        this.page = page;
    }

    public void setRecyclerView(XRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void loadMore(Action1<Object> action1) {
        getNoticeList(noticeEntities -> {
            Observable.just(new Object()).subscribe(action1);
            if (noticeEntities.isEmpty()) {
                error.onNext(new RestErrorInfo(getString(R.string.message_no_more)));
                recyclerView.setLoadMore(true);
            } else {
                BaseQuickAdapter adapter = (BaseQuickAdapter) recyclerView.getAdapter();
                adapter.addData(noticeEntities);
                recyclerView.setLoadMore(false);
            }
        });
    }

}

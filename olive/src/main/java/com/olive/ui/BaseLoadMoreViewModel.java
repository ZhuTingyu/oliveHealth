package com.olive.ui;

import com.biz.base.BaseViewModel;
import com.biz.base.RestErrorInfo;
import com.biz.widget.recyclerview.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/3.
 */

public abstract class BaseLoadMoreViewModel extends BaseViewModel {
    protected XRecyclerView recyclerView;
    protected int page = 1;

    public BaseLoadMoreViewModel(Object activity) {
        super(activity);
    }

    public void loadMore(List entities, Action1<Object> action1) {
        Observable.just(new Object()).subscribe(action1);
        if (entities.isEmpty()) {
            error.onNext(new RestErrorInfo(getString(R.string.message_no_more)));
            recyclerView.setLoadMore(true);
        } else {
            BaseQuickAdapter adapter = (BaseQuickAdapter) recyclerView.getAdapter();
            adapter.addData(entities);
            recyclerView.setLoadMore(false);
        }
    }

    public void setRecyclerView(XRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public  abstract void setLoadMore(Action1<Object> action1);

    public void cleanPage(){
        page = 1;
    }

}
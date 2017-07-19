package com.biz.base;

import com.biz.http.BuildConfig;
import com.biz.http.HttpConfig;
import com.biz.util.ToastUtils;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by wangwei on 2016/3/21.
 */
public abstract class RxBaseActivity extends AppCompatActivity {
    private BaseViewModel viewModel;
    public final CompositeSubscription subscription = new CompositeSubscription();

    public void initViewModel(BaseViewModel viewModel) {
        this.viewModel = viewModel;
        if (this.viewModel != null) {
            bindData(viewModel.getError(), error -> {
                if (error!=null) {
                    if (viewModel != null) {
                        viewModel.clearError();
                    }
                    error(error.code,error.message);
                }
            });
        }
    }

    public abstract void error(int code,String error);

    public <T> void bindData(Observable<T> observable, Action1<? super T> onNext, Action1<Throwable> onError) {
        subscription.add(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError));
    }

    public <T> void bindData(Observable<T> observable, Action1<? super T> onNext) {
        subscription.add(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext,
                throwable -> {
                    if (HttpConfig.isLog())
                        ToastUtils.showLong(getActivity(),throwable.getMessage());
                }
        ));
    }

    public <T> void bindUi(Observable<T> observable, Action1<? super T> onNext, Action1<Throwable> onError) {
        subscription.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError));
    }

    public <T> void bindUi(Observable<T> observable, Action1<? super T> onNext) {
        subscription.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribe(onNext,
                throwable -> {
                    if (HttpConfig.isLog())
                        ToastUtils.showLong(getActivity(), throwable.getMessage());
                }
        ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        if (viewModel != null)
            viewModel.onDestroy();
    }

    public Activity getActivity() {
        return this;
    }
}

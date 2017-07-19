package com.biz.base;


import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import com.biz.http.HttpErrorException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by wangwei on 2016/3/15.
 */
public class BaseViewModel {
    private Object baseActivity;
    protected final CompositeSubscription subscription = new CompositeSubscription();
    protected final BehaviorSubject<RestErrorInfo> error = BehaviorSubject.create();

    public BaseViewModel(Object activity) {
        this.baseActivity = activity;
    }

    public BaseActivity getActivity() {
        if (null != baseActivity && baseActivity instanceof BaseActivity) {
            return (BaseActivity) baseActivity;
        } else if (null != baseActivity && baseActivity instanceof BaseFragment) {
            return ((BaseFragment) baseActivity).getBaseActivity();
        }
        return null;
    }

    public Object getBaseActivity() {
        return baseActivity;
    }

    public BehaviorSubject<RestErrorInfo> getError() {
        return error;
    }


    public void clearError() {
        this.error.onNext(null);
    }

    public <T> void submitRequest(Observable<T> observable, final Action1<? super T> onNext, final Action1<Throwable> onError, final Action0 onComplete) {
        if (null != baseActivity && baseActivity instanceof AppCompatActivity) {
            subscription.add(observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError, onComplete));
        } else if (null != baseActivity && baseActivity instanceof BaseFragment) {
            subscription.add(observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError, onComplete));

        } else {
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError, onComplete);
        }
    }

    public <T> void submitRequest(Observable<T> observable, final Action1<? super T> onNext, final Action1<Throwable> onError) {
        if (null != baseActivity && baseActivity instanceof BaseActivity) {
            subscription.add(observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError));
        } else if (null != baseActivity && baseActivity instanceof BaseFragment) {
            subscription.add(observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError));

        } else {
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError);
        }
    }

    public <T> void submitRequestThrowError(Observable<T> observable, final Action1<? super T> onNext) {
        if (null != baseActivity && baseActivity instanceof BaseActivity) {
            subscription.add(observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, throwable -> error.onNext(getError(throwable))));
        } else if (null != baseActivity && baseActivity instanceof BaseFragment) {
            subscription.add(observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, throwable -> error.onNext(getError(throwable))));

        } else {
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, throwable -> error.onNext(getError(throwable)));
        }
    }

    public <T> void submitRequestThrowError(Observable<T> observable, final Action1<? super T> onNext, final Action0 onComplete) {
        if (null != baseActivity && baseActivity instanceof BaseActivity) {
            subscription.add(observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, throwable -> error.onNext(getError(throwable)), onComplete));
        } else if (null != baseActivity && baseActivity instanceof BaseFragment) {
            subscription.add(observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, throwable -> error.onNext(getError(throwable)), onComplete));

        } else {
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, throwable -> error.onNext(getError(throwable)), onComplete);
        }
    }

    public <T> void submitRequest(Observable<T> observable, final Action1<? super T> onNext) {
        if (null != baseActivity && baseActivity instanceof BaseActivity) {
            subscription.add(observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext));
        } else if (null != baseActivity && baseActivity instanceof BaseFragment) {
            subscription.add(observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext));

        } else {
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext);
        }
    }

    public <T> void bindUi(Observable<T> observable, Action1<? super T> onNext, Action1<Throwable> onError) {
        subscription.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError));
    }

    public <T> void bindUi(Observable<T> observable, Action1<? super T> onNext) {
        subscription.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, throwable -> {
        }));
    }

    public void onDestroy() {
        subscription.clear();
        baseActivity = null;
    }

    public void cancelTask() {
        subscription.clear();
    }

    public RestErrorInfo getError(Throwable throwable) {
        if (throwable instanceof HttpErrorException) {
            return new RestErrorInfo(((HttpErrorException) throwable).getResponseJson());
        }
        if (throwable != null) return new RestErrorInfo(throwable.getMessage());
        return new RestErrorInfo("");
    }

    public String getString(@StringRes int r) {
        if (getActivity() != null) {
            return getActivity().getString(r);
        }
        return "";
    }

    public RestErrorInfo getErrorString(@StringRes int r) {
        if (getActivity() != null) {
            return new RestErrorInfo(getActivity().getString(r));
        }
        return new RestErrorInfo("");
    }

    public RestErrorInfo getErrorString(String r) {
        return new RestErrorInfo(r);
    }

    public String getString(@StringRes int r, Object... formatArgs) {
        if (getActivity() != null) {
            return getActivity().getString(r, formatArgs);
        }
        return "";
    }

    public static String getStringValue(String s) {
        return s == null ? "" : s;
    }
}

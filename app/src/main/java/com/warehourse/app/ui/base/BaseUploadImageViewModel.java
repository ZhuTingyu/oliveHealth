package com.warehourse.app.ui.base;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.image.upload.UploadImageUtil;
import com.biz.util.LogUtil;
import com.warehourse.app.application.WareApplication;
import com.warehourse.app.model.SystemModel;

import rx.Observable;
import rx.functions.Action1;

public class BaseUploadImageViewModel extends BaseViewModel {


    public BaseUploadImageViewModel(Object activity) {
        super(activity);
    }

    protected boolean getOssPrivateBucketName()
    {
        return false;
    }

    protected void upload(String bucketName, String src, Action1<String> onNext) {
        if (WareApplication.getApplication().isOssTokenEffective()) {
            submitRequest(UploadImageUtil.upload(src, bucketName,WareApplication.getApplication().getOssTokenEntity(),getOssPrivateBucketName()), onNext, throwable -> {
                LogUtil.print(throwable);
                error.onNext(getError(throwable));
            });
        } else {
            submitRequest(SystemModel.getOssToken(), r -> {
                if (r.isOk()) {
                    WareApplication.getApplication().setOssTokenEntity(r.data);
                    submitRequest(UploadImageUtil.upload(src, bucketName,WareApplication.getApplication().getOssTokenEntity(),getOssPrivateBucketName()), onNext, throwable -> {
                        LogUtil.print(throwable);
                        error.onNext(getError(throwable));
                    });
                } else throw new HttpErrorException(r);
            },throwable -> {
                LogUtil.print(throwable);
                error.onNext(getError(throwable));
            });
        }

    }

    public void checkToken(Action1<Boolean> onNext,Action1<Throwable> onError) {
        if (WareApplication.getApplication().isOssTokenEffective()) {
            Observable.just(true).subscribe(onNext);
        } else {
            submitRequest(SystemModel.getOssToken().map(r -> {
                if (r.isOk()) {
                    WareApplication.getApplication().setOssTokenEntity(r.data);
                    return true;
                } else throw new HttpErrorException(r);
            }),onNext,onError);
        }

    }

}
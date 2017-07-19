package com.warehourse.app.ui.register;

import com.biz.base.BaseViewModel;
import com.warehourse.app.model.ShopModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.ShopDetailEntity;
import com.warehourse.app.model.entity.ShopPhotoEntity;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by wangwei on 2016/3/30.
 */
class RegisterStatusViewModel extends BaseViewModel {
    public RegisterStatusViewModel(Object activity) {
        super(activity);
    }

    public void requestDetail(Action1<ShopDetailEntity> onNext) {
        submitRequest(ShopModel.shopDetail(), r -> {
            if (r.isOk()) {
                Observable.just(r.data).subscribe(onNext);
            } else {
                error.onNext(getErrorString(r.msg));
            }
        }, throwable -> {
            error.onNext(getError(throwable));
        });
    }

    public void requestPhoto(Action1<ShopPhotoEntity> onNext) {
        submitRequest(ShopModel.shopPhoto(), r -> {
            if (r.isOk()) {
                Observable.just(r.data).subscribe(onNext);
            } else {
                error.onNext(getErrorString(r.msg));
            }
        }, throwable -> {
            error.onNext(getError(throwable));
        });
    }
}

package com.warehourse.app.ui.my.receiver;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.warehourse.app.model.UserModel;

import rx.functions.Action1;

public class ReceiverChangeViewModel extends BaseViewModel {
    private String deliveryName;

    public ReceiverChangeViewModel(Object activity) {
        super(activity);
    }

    public void save(Action1<Boolean> onNext) {
        submitRequestThrowError(UserModel.changeDeliveryName(deliveryName).map(r->{
            if(r.isOk()){
                return true;
            }else throw new HttpErrorException(r);
        }),onNext);
    }

    public Action1<String> setDeliveryName() {
        return s -> {
            this.deliveryName = s;
        };
    }
}
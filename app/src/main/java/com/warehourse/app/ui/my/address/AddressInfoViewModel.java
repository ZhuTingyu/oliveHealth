package com.warehourse.app.ui.my.address;


import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.warehourse.app.model.ShopModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.AddressStatusEntity;

import rx.Observable;
import rx.functions.Action1;

class AddressInfoViewModel extends BaseViewModel {
    private AddressStatusEntity mAddressStatusEntity;

    public AddressInfoViewModel(Object activity) {
        super(activity);
    }

    public void request(Action1<Boolean> onNext) {
        submitRequest(ShopModel.getUpdateAddressStatus().map(r -> {
            if (r.isOk()) {
                this.mAddressStatusEntity = r.data;
                return true;
            } else throw new HttpErrorException(r);
        }), onNext);
    }

    public AddressStatusEntity getAddressStatusInfo() {
        return mAddressStatusEntity;
    }
}

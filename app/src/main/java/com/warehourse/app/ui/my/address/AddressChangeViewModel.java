package com.warehourse.app.ui.my.address;


import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.warehourse.app.R;
import com.warehourse.app.event.UserEvent;
import com.warehourse.app.model.ShopModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.AddressStatusEntity;
import com.warehourse.app.model.entity.UserEntity;
import com.warehourse.app.ui.base.BaseProvinceViewModel;

import android.text.TextUtils;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by wangwei on 2016/3/24.
 */
public class AddressChangeViewModel extends BaseProvinceViewModel {
    private String deliveryAddress;
    private String reason;
    private AddressStatusEntity addressStatusInfo;

    private final BehaviorSubject<AddressStatusEntity> currAddress = BehaviorSubject.create();

    private String cityChName;

    private final BehaviorSubject<String> cityCh = BehaviorSubject.create();
    private final BehaviorSubject<String> address = BehaviorSubject.create();

    public AddressChangeViewModel(Object activity) {
        super(activity);

        addressStatusInfo = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
        if (addressStatusInfo != null) {
            this.deliveryAddress = addressStatusInfo.deliveryAddress;
            this.cityId = addressStatusInfo.cityId;
            this.districtId = addressStatusInfo.districtId;
            this.provinceId = addressStatusInfo.provinceId;
            this.cityChName = addressStatusInfo.provinceName + " " + addressStatusInfo.cityName + " " + addressStatusInfo.districtName;
        }

        currAddress.onNext(addressStatusInfo);
    }

    public void request(Action1<Boolean> onNext) {
        if (addressStatusInfo != null) {
            cityCh.onNext(cityChName);
            address.onNext(deliveryAddress);
        }
        Observable.just(true).subscribe(onNext);
    }


    public void updateData(Action1<Boolean> action0) {
        if (deliveryAddress != null) {
            deliveryAddress = deliveryAddress.trim();
        }
        if (TextUtils.isEmpty(deliveryAddress)) {
            error.onNext(getErrorString(R.string.error_address));
            return;
        }
        if (reason != null) {
            reason = reason.trim();
        }
        if (TextUtils.isEmpty(reason)) {
            error.onNext(getErrorString(R.string.error_address_reason));
            return;
        }
        submitRequest(ShopModel.updateShopAddressDetail(provinceId, cityId, districtId, deliveryAddress, reason)
                .map(r -> {
                    if (r.isOk()) {
                        return true;
                    } else throw new HttpErrorException(r);
                }), action0);
    }


    public Action1<String> setReason() {
        return s -> {
            reason = s;
        };
    }

    public Action1<String> setAddress() {
        return address -> {
            this.deliveryAddress = address;
        };
    }

    public BehaviorSubject<String> getCityCh() {
        return cityCh;
    }

    public BehaviorSubject<String> getAddress() {
        return address;
    }


    public BehaviorSubject<AddressStatusEntity> getCurrAddress() {
        return currAddress;
    }

}

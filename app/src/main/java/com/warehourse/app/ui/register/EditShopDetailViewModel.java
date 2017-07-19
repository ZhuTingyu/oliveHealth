package com.warehourse.app.ui.register;

import android.text.TextUtils;


import com.warehourse.app.R;
import com.warehourse.app.model.ShopModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.ShopDetailEntity;
import com.warehourse.app.model.entity.ShopTypeEntity;
import com.warehourse.app.ui.base.BaseProvinceViewModel;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by wangwei on 2016/3/24.
 */
public class EditShopDetailViewModel extends BaseProvinceViewModel {
    private final BehaviorSubject<String> nameOb = BehaviorSubject.create();
    private final BehaviorSubject<String> typeOb = BehaviorSubject.create();
    private final BehaviorSubject<String> areaOb = BehaviorSubject.create();
    private final BehaviorSubject<String> addressOb = BehaviorSubject.create();
    private final BehaviorSubject<String> corporateNameOb = BehaviorSubject.create();
    private List<ShopTypeEntity> shopTypes;
    private long shopTypeId;
    private String name;
    private String deliveryAddress;
    private ShopDetailEntity shopDetailInfo;
    private String corporateName;

    public EditShopDetailViewModel(Object activity) {
        super(activity);
        shopTypes(list -> {
        });
    }

    private void sendUi() {
        if (shopDetailInfo != null) {
            nameOb.onNext(name = getStringValue(shopDetailInfo.name));
            shopTypeId = shopDetailInfo.shopTypeId;
            provinceId = shopDetailInfo.provinceId;
            cityId = shopDetailInfo.cityId;
            districtId = shopDetailInfo.districtId;
            deliveryAddress = shopDetailInfo.deliveryAddress;
            typeOb.onNext(getStringValue(shopDetailInfo.shopTypeName));
            String area=getStringValue(shopDetailInfo.provinceName) + " "
                    + getStringValue(shopDetailInfo.cityName) + " "
                    + getStringValue(shopDetailInfo.districtName);
            if(area!=null&&TextUtils.isEmpty(area.replace(" ",""))){
                area="";
            }
            areaOb.onNext(area);
            addressOb.onNext(getStringValue(shopDetailInfo.deliveryAddress));
            corporateNameOb.onNext(getStringValue(shopDetailInfo.corporateName));
        }
    }

    public void requestDetail(Action1<Boolean> onNext) {
        submitRequest(ShopModel.shopDetail(), r -> {
            if (r.isOk()) {
                shopDetailInfo = r.data;
                sendUi();
                Observable.just(true).subscribe(onNext);
            } else {
                error.onNext(getErrorString(r.msg));
            }
        }, throwable -> {
            error.onNext(getError(throwable));
        });
    }


    public void updateData(Action1<Boolean> action0) {
        if (name != null) {
            name = name.trim();
        }
        if (deliveryAddress != null) {
            deliveryAddress = deliveryAddress.trim();
        }
        if (corporateName != null) {
            corporateName = corporateName.trim();
        }
        if (TextUtils.isEmpty(name)) {
            error.onNext(getErrorString(R.string.text_register_error_empty_shop_name));
            return;
        }
        if (TextUtils.isEmpty(corporateName)) {
            error.onNext(getErrorString(R.string.text_register_error_empty_shop_corporate_name));
            return;
        }
        if (shopTypeId <= 0) {
            error.onNext(getErrorString(R.string.text_register_error_empty_shop_type));
            return;
        }
        if (shopTypeId <= 0 && provinceId <= 0 && districtId <= 0) {
            error.onNext(getErrorString(R.string.text_register_error_empty_shop_city));
            return;
        }
        if (TextUtils.isEmpty(deliveryAddress)) {
            error.onNext(getErrorString(R.string.text_register_error_empty_shop_delivery_address));
            return;
        }

        submitRequest(ShopModel.saveShopDetail(name, shopTypeId, provinceId, cityId, districtId, deliveryAddress, corporateName),
                r -> {
                    if (r.isOk()) {
                        Observable.just(true).subscribe(action0);
                    } else {
                        error.onNext(getErrorString(r.msg));
                    }
                }, throwable -> {
                    error.onNext(getError(throwable));
                });

    }

    public void shopTypes(Action1<List<ShopTypeEntity>> onNext) {
        if (shopTypes != null && shopTypes.size() > 0) {
            Observable.just(shopTypes).subscribe(onNext);
            return;
        }
        submitRequest(ShopModel.getShopTypes(), r -> {
                    if (r.isOk()) {
                        shopTypes = r.data;
                        Observable.just(shopTypes).subscribe(onNext);
                    } else {
                        error.onNext(getErrorString(r.msg));
                    }
                },
                throwable -> {
                    error.onNext(getError(throwable));
                });
    }


    public Action1<Long> setShopType() {
        return id -> {
            shopTypeId = id;
        };
    }

    public Action1<String> setName() {
        return name -> {
            this.name = name;
        };
    }

    public Action1<String> setAddress() {
        return address -> {
            this.deliveryAddress = address;
        };
    }

    public Action1<String> setIdentity() {
        return s -> {
            this.corporateName = s;
        };
    }

    public BehaviorSubject<String> getName() {
        return nameOb;
    }

    public BehaviorSubject<String> getType() {
        return typeOb;
    }

    public BehaviorSubject<String> getArea() {
        return areaOb;
    }

    public BehaviorSubject<String> getAddress() {
        return addressOb;
    }


    public BehaviorSubject<String> getCorporateName() {
        return corporateNameOb;
    }
}

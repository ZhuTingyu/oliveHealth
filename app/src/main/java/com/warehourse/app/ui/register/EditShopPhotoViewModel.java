package com.warehourse.app.ui.register;

import android.text.TextUtils;

import com.biz.util.IntentBuilder;
import com.warehourse.app.R;
import com.warehourse.app.model.ShopModel;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.entity.ShopPhotoEntity;
import com.warehourse.app.ui.base.BaseUploadImageViewModel;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by wangwei on 2016/3/25.
 */
public class EditShopPhotoViewModel extends BaseUploadImageViewModel {
    private final BehaviorSubject<String> businessLicenceOb = BehaviorSubject.create();
    private final BehaviorSubject<String> shopPhotoOb = BehaviorSubject.create();
    private final BehaviorSubject<String> liquorSellLicenceOb = BehaviorSubject.create();
    private final BehaviorSubject<String> corporateIdPhotoOb = BehaviorSubject.create();
    private String businessLicence;
    private String shopPhoto;
    private String liquorSellLicence;
    private String corporateIdPhoto;
    private ShopPhotoEntity shopPhotoInfo;

    public EditShopPhotoViewModel(Object activity) {
        super(activity);
        shopPhotoInfo = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
    }

    public void upload(String src, Action1<String> onNext) {
        super.upload(SystemModel.getInstance().getOssPrivateBucketName(), src, onNext);
    }

    @Override
    protected boolean getOssPrivateBucketName() {
        return true;
    }

    private void sendUi() {
        if (shopPhotoInfo != null) {
            businessLicence=shopPhotoInfo.businessLicence;
            shopPhoto=shopPhotoInfo.shopPhoto;
            liquorSellLicence=shopPhotoInfo.liquorSellLicence;
            corporateIdPhoto=shopPhotoInfo.corporateIdPhoto;
            businessLicenceOb.onNext(getLogo(shopPhotoInfo.businessLicence));
            shopPhotoOb.onNext(getLogo(shopPhotoInfo.shopPhoto));
            liquorSellLicenceOb.onNext(getLogo(shopPhotoInfo.liquorSellLicence));
            corporateIdPhotoOb.onNext(getLogo(shopPhotoInfo.corporateIdPhoto));
        }
    }
    public void requestPhoto(Action1<Boolean> onNext) {
        submitRequest(ShopModel.shopPhoto(), r -> {
            if (r.isOk()) {
                shopPhotoInfo=r.data;
                sendUi();
                Observable.just(true).subscribe(onNext);
            } else {
                error.onNext(getErrorString(r.msg));
            }
        }, throwable -> {
            error.onNext(getError(throwable));
        });
    }

    private String getLogo(String s) {
        if (TextUtils.isEmpty(s)) return "";
        return s;
    }

    public void submit(Action1<Boolean> onNext) {

        if (TextUtils.isEmpty(businessLicence)) {
            error.onNext(getErrorString(R.string.text_error_not_exists_business_licence));
            return;
        }
        if (TextUtils.isEmpty(shopPhoto)) {
            error.onNext(getErrorString(R.string.text_error_not_exists_shop_photo));
            return;
        }
        submitRequest(ShopModel.saveQualification(businessLicence, shopPhoto, liquorSellLicence, corporateIdPhoto),
                r -> {
                    if (r.isOk()) {
                        Observable.just(true).subscribe(onNext);
                    } else {
                        error.onNext(getErrorString(r.msg));
                    }
                }, throwable -> {
                    error.onNext(getError(throwable));
                });
    }


    public Action1<String> setBusinessLicence() {
        return businessLicence -> {
            this.businessLicence = businessLicence;
        };
    }

    public Action1<String> setShopPhoto() {
        return shopPhoto -> {
            this.shopPhoto = shopPhoto;
        };
    }

    public Action1<String> setLiquorSellLicence() {
        return liquorSellLicence -> {
            this.liquorSellLicence = liquorSellLicence;
        };
    }

    public Action1<String> setCorporateIdPhoto() {
        return corporateIdPhoto -> {
            this.corporateIdPhoto = corporateIdPhoto;
        };
    }

    public BehaviorSubject<String> getBusinessLicence() {
        return businessLicenceOb;
    }

    public BehaviorSubject<String> getShopPhoto() {
        return shopPhotoOb;
    }

    public BehaviorSubject<String> getLiquorSellLicence() {
        return liquorSellLicenceOb;
    }

    public BehaviorSubject<String> getCorporateIdPhoto() {
        return corporateIdPhotoOb;
    }
}

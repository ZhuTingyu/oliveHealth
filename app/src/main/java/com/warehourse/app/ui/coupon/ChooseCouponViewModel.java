package com.warehourse.app.ui.coupon;


import com.biz.base.BaseViewModel;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.warehourse.app.model.VoucherModel;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.model.entity.VoucherEntity;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by wangwei on 2016/3/27.
 */
public class ChooseCouponViewModel extends BaseViewModel {
    private List<ProductEntity> productInfos;
    private List<VoucherEntity> voucherInfos;
    private VoucherEntity selectedVoucherInfo;
    private VoucherEntity hisSelectedVoucherInfo;
    private final BehaviorSubject<String> price = BehaviorSubject.create();

    public ChooseCouponViewModel(Object activity) {
        super(activity);
        productInfos = getActivity().getIntent().getParcelableArrayListExtra(IntentBuilder.KEY_DATA);
        hisSelectedVoucherInfo = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_VALUE);
    }

//    public void setAdapter(BaseRecyclerViewAdapter adapter) {
//        this.adapter = adapter;
//    }

    public void request(Action1<List<VoucherEntity>> action0) {
        submitRequest(VoucherModel.getCreateOrderVoucher(productInfos), r -> {
            if (r.isOk()) {
                voucherInfos = r.data;
                if (voucherInfos == null) voucherInfos = Lists.newArrayList();
                if (hisSelectedVoucherInfo != null && voucherInfos != null) {
                    for (VoucherEntity v : voucherInfos) {
                        if (v.voucherTypeId == hisSelectedVoucherInfo.voucherTypeId) {
                            selectedVoucherInfo = v;
                            selectedVoucherInfo.quantity = hisSelectedVoucherInfo.quantity;
                            break;
                        }
                    }
                }
                Observable.just(voucherInfos).subscribe(action0);
            } else {
                error.onNext(getErrorString(r.msg));
            }
        }, throwable -> {
            error.onNext(getError(throwable));
        });
    }

    public List<VoucherEntity> getVoucherInfos() {
        if (voucherInfos == null) return Lists.newArrayList();
        return voucherInfos;
    }

    public void setVoucherCount(VoucherEntity voucherInfo) {
        long p = 0;
        if (voucherInfo != null) {
            p = voucherInfo.quantity * voucherInfo.faceValue;
        }
        price.onNext(PriceUtil.formatRMB(p));
    }

    public VoucherEntity getSelectedVoucherInfo() {
        return selectedVoucherInfo;
    }

    public void setSelectedVoucherInfo(VoucherEntity selectedVoucherInfo) {
        this.selectedVoucherInfo = selectedVoucherInfo;
        setVoucherCount(this.selectedVoucherInfo);
    }

    public BehaviorSubject<String> getPrice() {
        return price;
    }
}

package com.warehourse.app.ui.coupon;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.warehourse.app.model.VoucherModel;
import com.warehourse.app.model.entity.VoucherMainEntity;

import rx.functions.Action1;

/**
 * Created by wangwei on 2016/3/23.
 */
public class MoneyViewModel extends BaseViewModel {
    public MoneyViewModel(Object activity) {
        super(activity);
    }

    public void request(Action1<VoucherMainEntity> action0) {
        submitRequestThrowError(VoucherModel.getAll().map(r -> {
            if (r.isOk()) {
                if(r.data==null){
                    r.data=new VoucherMainEntity();
                }
                return r.data;
            } else throw new HttpErrorException(r);
        }), action0);

    }
}

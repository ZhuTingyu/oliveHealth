package com.warehourse.app.ui.order.detail;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.warehourse.app.model.OrderModel;
import com.warehourse.app.model.entity.OrderEntity;
import com.warehourse.app.ui.order.BaseOrderOperationViewModel;

import rx.functions.Action1;

/**
 * Title: OrderDetailViewModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:26/05/2017  17:33
 *
 * @author johnzheng
 * @version 1.0
 */

public class OrderDetailViewModel extends BaseOrderOperationViewModel {
    private String id = "";
    private OrderEntity mOrderEntity= null;
    public OrderDetailViewModel(Object activity) {
        super(activity);
        id = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_ID);
        mOrderEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
    }

    public void detail(Action1<OrderEntity> onNext){
        if (mOrderEntity!=null) {
            onNext.call(mOrderEntity);
        }
        submitRequestThrowError(OrderModel.detail(id).map(r->{
            if (r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }), onNext);
    }
}

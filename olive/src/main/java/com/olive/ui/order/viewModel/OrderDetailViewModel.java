package com.olive.ui.order.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.TimeUtil;
import com.olive.R;
import com.olive.model.OrderModel;
import com.olive.model.entity.OrderEntity;
import com.olive.model.entity.ProductEntity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/8.
 */

public class OrderDetailViewModel extends OrderViewModel{

    public OrderEntity orderEntity;
    public boolean isHaveLogisticsPrice = false;

    public OrderDetailViewModel(Object activity) {
        super(activity);
        orderNo = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_VALUE);
    }

    public void getOrderDetail(Action1<OrderEntity> action1){
        submitRequestThrowError(OrderModel.orderDetail(orderNo).map(r -> {
            if(r.isOk()){
                orderEntity = r.data;
                return orderEntity;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public int getTotalCount(List<ProductEntity> productEntities) {
        int count = 0;
        for (ProductEntity productEntity : productEntities) {
            count += productEntity.quantity;
        }
        return count;
    }

    public String[] getOderInfoTitle(){
        if(orderEntity.payTime != 0){
            return getActivity().getResources().getStringArray(R.array.array_order_details);
        }else {
            return getActivity().getResources().getStringArray(R.array.array_order_details_no_pay_time);
        }
    }

    public List<String> getOrderInfo() {
        List<String> list = Lists.newArrayList();
        list.add(orderEntity.expressNo == null ? "" : orderEntity.expressNo);
        list.add(orderEntity.expressInfo == null ? "" : orderEntity.expressInfo);
        list.add(orderEntity.orderNo);
        list.add(orderEntity.outTradeNo);
        list.add(TimeUtil.format(orderEntity.createTime, TimeUtil.FORMAT_YYYYMMDDHHMMSS));
        if(orderEntity.payTime != 0){
            list.add(TimeUtil.format(orderEntity.payTime, TimeUtil.FORMAT_YYYYMMDDHHMMSS));
        }
        return list;
    }

}

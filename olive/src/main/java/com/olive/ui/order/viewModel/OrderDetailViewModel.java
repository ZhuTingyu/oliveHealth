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

public class OrderDetailViewModel extends BaseViewModel{

    private String orderNo;
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
        String[] titles = getActivity().getResources().getStringArray(R.array.array_order_details);
        return titles;
    }

    public List<String> getOrderInfo() {
        List<String> list = Lists.newArrayList();
        list.add(orderEntity.outTradeNo);
        list.add(orderEntity.expressInfo);
        list.add("12");
        list.add(orderEntity.orderNo);
        list.add(orderEntity.outTradeNo);
        list.add(TimeUtil.format(orderEntity.createTime, TimeUtil.FORMAT_YYYYMMDDHHMMSS));
        list.add(TimeUtil.format(orderEntity.payTime, TimeUtil.FORMAT_YYYYMMDDHHMMSS));
        return list;
    }

}

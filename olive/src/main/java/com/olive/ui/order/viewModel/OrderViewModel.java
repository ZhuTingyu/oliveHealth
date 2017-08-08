package com.olive.ui.order.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.model.OrderModel;
import com.olive.model.UserModel;
import com.olive.model.entity.OrderEntity;
import com.olive.model.entity.ProductEntity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/8.
 */

public class OrderViewModel extends BaseViewModel {

    private String orderNo;
    private int addressId;
    private List<ProductEntity> productEntities;

    public OrderViewModel(Object activity) {
        super(activity);
    }

    public void createOrder(Action1<OrderEntity> action1){
        submitRequestThrowError(OrderModel.createOrder(productEntities, addressId, getCheckNumber()).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }), action1);
    }

    public void cancelOrder(Action1<String> action1){
        submitRequestThrowError(OrderModel.cancelOrder(orderNo).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }), action1);
    }

    public void confirmOrder(Action1<String> action1){
        submitRequestThrowError(OrderModel.confireOrder(orderNo).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }), action1);
    }



    private String getCheckNumber() {
        return UserModel.getInstance().getToken() + System.currentTimeMillis();
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}

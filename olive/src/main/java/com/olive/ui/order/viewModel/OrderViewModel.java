package com.olive.ui.order.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.R;
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

    private static final int PAY_STATUS_PAY = 1; // 支付状态 已支付
    private static final int PAY_STATUS_NOT_PAY = 0;// 支付状态 未支付

    private static final int EXPRESS_STATUS_DEFALUT = 0;//物流状态  默认 （未支付）
    private static final int EXPRESS_STATUS_WAIT_SEND    = 1;//物流状态  待发货
    private static final int EXPRESS_STATUS_WAIT_RECEIVE    = 2;//物流状态  待收货
    private static final int EXPRESS_STATUS_COMPLETE    = 3;//物流状态  已经完成（签收）

    private static final int ORDER_STATUS_NORMAL   = 1;//订单状态 正常状态
    private static final int ORDER_STATUS_CANCEL   = -1;//订单状态 已取消

    private static final int ALLOW_NOT_DEBT = 1; //有欠款
    private static final int ALLOW_DEBT = 0; //没有欠款

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
        submitRequestThrowError(OrderModel.confirmOrder(orderNo).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }), action1);
    }

    public String getOrderStatus(OrderEntity orderEntity){

        String status = "";

        if(orderEntity.payStatus == PAY_STATUS_PAY){
            if(orderEntity.expressStatus == EXPRESS_STATUS_WAIT_SEND){
                status = getActivity().getString(R.string.text_wait_send);
            }else if(orderEntity.expressStatus == EXPRESS_STATUS_WAIT_RECEIVE){
                status = getActivity().getString(R.string.text_wait_receive);
            }else if(orderEntity.expressStatus == EXPRESS_STATUS_COMPLETE) {
                status = getActivity().getString(R.string.text_order_complete);
            }
        }else {
            if(orderEntity.orderStatus == ORDER_STATUS_NORMAL){
                status = getString(R.string.text_waiting_pay);
            }else {
                status = getString(R.string.text_order_cancel);
            }
        }

        return status;
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

    public boolean isHasDebt(OrderEntity orderEntity){
        return orderEntity.allowDebt == ALLOW_NOT_DEBT;
    }
}

package com.olive.ui.order.viewModel;

import com.biz.base.BaseFragment;
import com.biz.http.HttpErrorException;
import com.olive.R;
import com.olive.model.OrderModel;
import com.olive.model.entity.OrderEntity;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.BaseLoadMoreViewModel;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class OrderListViewModel extends BaseLoadMoreViewModel {


    private static final int TYPE_ALL = 0; //全部（默认），
    private static final int TYPE_WAIT_PAY = 1;//待付款，
    private static final int TYPE_WAIT_SEND = 2;//待发货，e
    private static final int TYPE_WAIT_RECEIVE = 3;//待收货 ，
    private static final int TYPE_ORDER_COMPLETE = 4;//已完成订单，
    private static final int TYPE_ORDER_CANCEL = -1;//已取消订单

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


    public static final String KEY_TYPE = "type";
    private String typeName;
    private int typeCode;


    public OrderListViewModel(BaseFragment fragment) {
        super(fragment);
        typeName = fragment.getArguments().getString(KEY_TYPE);
        initTypeCode();
    }

    public void getOrderList(Action1<List<OrderEntity>> action1){
        submitRequestThrowError(OrderModel.orderList(page, typeCode).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }), action1);
    }

    private void initTypeCode() {
        if (getString(R.string.text_order_all).equals(typeName)) {
            //全部
            typeCode = TYPE_ALL;
        } else if (getString(R.string.text_waiting_pay).equals(typeName)) {
            //待支付
            typeCode = TYPE_WAIT_PAY;
        } else if (getString(R.string.text_wait_send).equals(typeName)) {
            //待发货
            typeCode = TYPE_WAIT_SEND;
        } else if (getString(R.string.text_wait_receive).equals(typeName)) {
            //待收货
            typeCode = TYPE_WAIT_RECEIVE;
        } else if (getString(R.string.text_order_complete).equals(typeName)) {
            //已完成
            typeCode = TYPE_ORDER_COMPLETE;
        } else if (getString(R.string.text_order_cancel).equals(typeName)) {
            //已经取消
            typeCode = TYPE_ORDER_CANCEL;
        }
    }

    @Override
    public void setLoadMore(Action1<Object> action1) {
        page++;
        getOrderList(orderListEntities-> {
            loadMore(orderListEntities,action1);
        });
    }

    public String getTypeName() {
        return typeName;
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
                status =  getString(R.string.text_order_cancel);
            }
        }

        return status;
    }

    public boolean isHasDebt(OrderEntity orderEntity){
        return orderEntity.allowDebt == ALLOW_NOT_DEBT;
    }
}

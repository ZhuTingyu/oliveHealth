package com.olive.ui.order.viewModel;

import com.biz.base.BaseFragment;
import com.biz.http.HttpErrorException;
import com.biz.util.Lists;
import com.olive.R;
import com.olive.model.OrderModel;
import com.olive.model.entity.OrderListEntity;
import com.olive.ui.BaseLoadMoreViewModel;

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

    private static final String KEY_TYPE = "type";
    private String typeName;
    private int typeCode;


    public OrderListViewModel(BaseFragment fragment) {
        super(fragment);
        typeName = fragment.getArguments().getString(KEY_TYPE);
        initTypeCode();
    }

    public void getOrderList(Action1<OrderListEntity> action1){
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

    }

    public String getTypeName() {
        return typeName;
    }
}

package com.warehourse.app.ui.order.list;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.OrderEntity;
import com.warehourse.app.ui.order.detail.*;
import com.warehourse.app.util.LoadImageUtil;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import java.util.List;

/**
 * Title: OrderListAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:15/05/2017  15:16
 *
 * @author johnzheng
 * @version 1.0
 */

public class OrderListAdapter extends BaseMultiItemQuickAdapter<OrderEntity, OrderViewHolder> {

    private OrderViewHolder.OrderListener mOrderApplySaleListener;
    private OrderViewHolder.OrderListener mOrderCancelListener;
    private OrderViewHolder.OrderListener mOrderPayListener;
    private View.OnClickListener mItemOnClickListener;

    public void setItemOnClickListener(View.OnClickListener itemOnClickListener) {
        mItemOnClickListener = itemOnClickListener;
    }

    public void setOrderPayListener(OrderViewHolder.OrderListener orderPayListener) {
        mOrderPayListener = orderPayListener;
    }

    public void setOrderApplySaleListener(OrderViewHolder.OrderListener orderApplySaleListener) {
        mOrderApplySaleListener = orderApplySaleListener;
    }

    public void setOrderCancelListener(OrderViewHolder.OrderListener orderCancelListener) {
        mOrderCancelListener = orderCancelListener;
    }
    public OrderListAdapter() {
        super(Lists.newArrayList());
        addItemType(OrderEntity.TYPE_SINGLE, R.layout.item_order_single_layout);
        addItemType(OrderEntity.TYPE_MORE, R.layout.item_order_more_layout);
    }


    @Override
    protected void convert(OrderViewHolder holder, OrderEntity entity) {
        holder.setOrderApplySaleListener(mOrderApplySaleListener);
        holder.setOrderCancelListener(mOrderCancelListener);
        holder.setOrderPayListener(mOrderPayListener);
        holder.setOrderId(entity.orderCode);
        holder.setOrderPrice(entity.getPayAmount());
        holder.setOrderSaleStatus(entity.statusMsg);

        holder.mBtnLayout.removeAllViews();
        holder.setBtn1Contact(entity);
        holder.setBtn1Sale(entity.applyRefundable, entity);
        holder.setBtn1Cancel(entity.cancelable, entity);
        holder.setBtn2Pay(entity.payable, entity);

        holder.setOrderStatus(entity.statusDesc);
        switch (holder.getItemViewType()) {
            case OrderEntity.TYPE_SINGLE:
                holder.setIconAndName(entity.getFirstItem().getLogo(), entity.getFirstItem().name);
                break;
            case OrderEntity.TYPE_MORE:
                holder.setItemList(entity.getItems());
                break;
            default:
        }
        holder.itemView.setTag(entity);
        holder.itemView.setOnClickListener(v -> {
            if(mItemOnClickListener!=null){
                mItemOnClickListener.onClick(v);
            }
        });
    }

}

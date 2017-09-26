package com.olive.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.biz.util.TimeUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.OrderEntity;
import com.olive.model.entity.ProductEntity;
import com.olive.util.Utils;
import com.olive.widget.LinearLayoutForRecyclerView;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/30.
 */

public class ConsumeDetailAdapter extends BaseQuickAdapter<OrderEntity, BaseViewHolder> {

    private static final int TYPE_ORDER_PAY = 1;
    private static final int TYPE_REFUND = 2;
    private  RefundProductsNumberInfoAdapter adapter;

    public ConsumeDetailAdapter() {
        super(R.layout.item_consume_detail_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, OrderEntity orderEntity) {

        TextView status = holder.findViewById(R.id.status);
        TextView text = holder.findViewById(R.id.text);

        if(orderEntity.type == TYPE_ORDER_PAY || orderEntity.type == 0){
            status.setText(mContext.getString(R.string.title_pay_order));
            text.setText(mContext.getString(R.string.text_order_price_1));
            status.setOnClickListener(v -> {

            });
        }else {
            status.setText(mContext.getString(R.string.title_refund_goods_and_money));
            text.setText(mContext.getString(R.string.text_refund_price));
            status.setOnClickListener(v -> {

            });
        }

        holder.setText(R.id.date, TimeUtil.format(orderEntity.orderTime, TimeUtil.FORMAT_YYYYMMDD));
        holder.setText(R.id.price, PriceUtil.formatRMB(orderEntity.amount));

        List<ProductEntity> productEntityList = orderEntity.products;
        LinearLayoutForRecyclerView linearLayout = holder.findViewById(R.id.ll_info);

       if(adapter == null){
           adapter = new RefundProductsNumberInfoAdapter(mContext, productEntityList);
       }
       adapter.setData(productEntityList);
       linearLayout.setAdapter(adapter);


    }

    @Override
    public void setNewData(@Nullable List<OrderEntity> data) {
        super.setNewData(data);
        if(data.isEmpty()){
            Utils.setEmptyView(this, mContext.getString(R.string.message_empty_consume));
        }
    }
}

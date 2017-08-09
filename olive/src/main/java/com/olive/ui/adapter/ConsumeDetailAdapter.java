package com.olive.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.biz.util.TimeUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.OrderEntity;
import com.olive.model.entity.ProductEntity;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/30.
 */

public class ConsumeDetailAdapter extends BaseQuickAdapter<OrderEntity, BaseViewHolder> {

    private static final int TYPE_ORDER_PAY = 1;
    private static final int TYPE_REFUND = 2;

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
        LinearLayout linearLayout = holder.findViewById(R.id.ll_info);
        linearLayout.removeAllViews();
        for(ProductEntity productEntity : productEntityList){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_line_text_layout, linearLayout,false);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView number = (TextView) view.findViewById(R.id.number);
            name.setText(productEntity.productName);
            number.setText("x"+ productEntity.quantity);
            linearLayout.addView(view);
        }
    }
}

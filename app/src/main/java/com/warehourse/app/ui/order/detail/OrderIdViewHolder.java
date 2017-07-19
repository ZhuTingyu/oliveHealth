package com.warehourse.app.ui.order.detail;

import com.biz.base.BaseViewHolder;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.OrderEntity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class OrderIdViewHolder extends BaseViewHolder {
    public TextView textOrderId;


    OrderIdViewHolder(View view) {
        super(view);
        textOrderId = findViewById(R.id.text);
        findViewById(R.id.text1).setVisibility(View.GONE);


    }

    public void bindData(OrderEntity entity){
        textOrderId.setText(getString(R.string.text_order_code, entity.orderCode));
    }


    public static OrderIdViewHolder createViewHolder(ViewGroup parent) {
        return new OrderIdViewHolder(inflater(R.layout.item_order_include_text_layout, parent));
    }
}
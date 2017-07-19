package com.warehourse.app.ui.order.detail;

import com.biz.base.BaseViewHolder;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.OrderEntity;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class PayInfoViewHolder extends BaseViewHolder {
    public TextView textPayTitle;
    public TextView textPayWay;
    public TextView textInvoiceTitle;
    public TextView textInvoiceDesc;
    public TextView textInvoiceNumberDesc;
    private View invoiceView;

    PayInfoViewHolder(View view) {
        super(view);
        textPayTitle =  findViewById(R.id.title_left);
        textPayWay =  findViewById(R.id.text);
        textInvoiceTitle =  findViewById(R.id.title_line_1);
        textInvoiceDesc =  findViewById(R.id.title_line_2);
        textInvoiceNumberDesc = findViewById(R.id.title_line_4);
        invoiceView = findViewById(R.id.layout);
        invoiceView.setVisibility(View.VISIBLE);
    }

    public void bindData(OrderEntity entity){
        textPayWay.setText(entity.getPayType());
        textInvoiceDesc.setText(entity.getInvoiceTitle());
        textInvoiceNumberDesc.setText(entity.getInvoiceTaxId());
    }


    public static PayInfoViewHolder createViewHolder(ViewGroup parent) {
        return new PayInfoViewHolder(inflater(R.layout.item_order_pay_way_layout, parent));
    }
}
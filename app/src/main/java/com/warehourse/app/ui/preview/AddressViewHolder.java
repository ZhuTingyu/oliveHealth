package com.warehourse.app.ui.preview;

import com.biz.base.BaseViewHolder;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.OrderEntity;
import com.warehourse.app.model.entity.OrderPreviewEntity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class AddressViewHolder extends BaseViewHolder {
    public TextView textName;
    public TextView textPhone;
    public TextView textDetail;
    View topView, bottomView;

    AddressViewHolder(View view) {
        super(view);
        textName = findViewById(R.id.title_left);
        textPhone = findViewById(R.id.text);
        textDetail = findViewById(R.id.title_line_2);
        bottomView = findViewById(R.id.image_bottom);
        topView = findViewById(R.id.image_top);

        topView.setVisibility(View.GONE);
    }


    public static AddressViewHolder createViewHolder(ViewGroup parent) {
        return new AddressViewHolder(inflater(R.layout.item_order_address_layout, parent));
    }


    public void bindData(OrderPreviewEntity entity){
        textName.setText(entity.buyerName);
        textPhone.setText(entity.buyerMobile);
        textDetail.setText(entity.buyerAddress);
    }
}
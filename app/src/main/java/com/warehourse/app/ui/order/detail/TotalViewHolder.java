package com.warehourse.app.ui.order.detail;

import com.biz.base.BaseViewHolder;
import com.biz.util.PriceUtil;
import com.biz.util.TimeUtil;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.OrderEntity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class TotalViewHolder extends BaseViewHolder {


    public
    TextView textPayPrice, mTextView;
    public
    TextView textOrderTime;

    public
    TextView textTotalTitle;
    public
    TextView textTotalPrice;
    public
    TextView textDiscountTitle;
    public
    TextView textDiscountPrice;
    public
    TextView textCouponTitle;
    public
    TextView textCouponPrice;


    TotalViewHolder(View view) {
        super(view);

        textTotalTitle = findViewById(R.id.title_line_1_left);
        textTotalPrice = findViewById(R.id.title_line_1_right);
        textDiscountTitle   = findViewById(R.id.title_line_2_left);
        textDiscountPrice   = findViewById(R.id.title_line_2_right);
        textCouponTitle  = findViewById(R.id.title_line_3_left);
        textCouponPrice  = findViewById(R.id.title_line_3_right);

        textOrderTime = findViewById(R.id.title_line_2);
        textPayPrice = findViewById(R.id.title_line_1);
        mTextView  =  findViewById(R.id.text1);


        textTotalTitle.setText(R.string.text_product_total);
        textDiscountTitle.setText(R.string.text_promote_order);
        textCouponTitle.setText(R.string.text_money_coupon);
        textCouponTitle.setTextColor(getColors(R.color.color_b2b2b2));
        textDiscountTitle.setTextColor(getColors(R.color.color_b2b2b2));

    }

    public void bindData(OrderEntity entity){
        mTextView.setText(entity.getPayAmountString());

        textTotalPrice.setText(PriceUtil.formatRMB(entity.orderAmount));
        textCouponPrice.setText("-"+PriceUtil.formatRMB(entity.voucherAmount));
        textDiscountPrice.setText("-"+PriceUtil.formatRMB(entity.reeAmount));
        textPayPrice.setText(PriceUtil.formatRMB(entity.payAmount));
        textOrderTime.setText(getString(R.string.text_order_create_time,
                TimeUtil.format(entity.createTime, TimeUtil.FORMAT_YYYYMMDDHHMMSS)));
    }


    public static TotalViewHolder createViewHolder(ViewGroup parent) {
        return new TotalViewHolder(inflater(R.layout.item_order_total_layout, parent));
    }


}
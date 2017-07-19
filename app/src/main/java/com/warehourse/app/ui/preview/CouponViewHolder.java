package com.warehourse.app.ui.preview;

import com.biz.base.BaseViewHolder;
import com.biz.util.DrawableHelper;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.OrderPreviewEntity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class CouponViewHolder extends BaseViewHolder {
    TextView text;
    TextView textCouponCount;
    TextView textCoupon;

    CouponViewHolder(View view) {
        super(view);

        text = findViewById(R.id.text);
        textCouponCount = findViewById(R.id.title_left);
        textCoupon = findViewById(R.id.title_right);

        textCouponCount.setBackgroundDrawable(
                DrawableHelper.createShapeDrawable(getColors(R.color.color_red), 3)
        );
    }


    public static CouponViewHolder createViewHolder(ViewGroup parent) {
        return new CouponViewHolder(inflater(R.layout.item_coupon_choice_layout, parent));
    }

    public void bindData(OrderPreviewEntity entity){
        textCouponCount.setText(itemView.getResources().getString(R.string.text_coupon_count_use,String.valueOf(entity.coupons)));
        textCoupon.setText(itemView.getResources().getString(R.string.text_coupon_count_used, String.valueOf(0)));
    }
}
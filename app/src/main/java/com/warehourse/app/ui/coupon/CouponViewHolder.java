package com.warehourse.app.ui.coupon;


import com.biz.base.BaseViewHolder;
import com.biz.util.Utils;
import com.biz.widget.CountEditText;
import com.biz.widget.CouponStatusTextView;
import com.warehourse.app.R;

import android.graphics.Color;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CouponViewHolder extends BaseViewHolder {


    TextView titleLine1;
    TextView titleLine2;
    TextView titleMoney;
    TextView titleFlag;
    TextView titleLine4;
    ImageView imageCircle;

    AppCompatRadioButton checkbox;
    ImageView line;
    TextView textCount;
    AppCompatImageButton btnMin;
    CountEditText editCount;
    AppCompatImageButton btnAdd;
    LinearLayout layoutEdit, layoutRight;
    CouponStatusTextView iconStatus;
    ImageView arrow;
    View addView, layout;
    TextView disableView;


    public CouponViewHolder(View view) {
        super(view);

        checkbox = findViewById(R.id.checkbox);
        layout = findViewById(R.id.layout);
        imageCircle = findViewById(R.id.image_circle);
        titleFlag = findViewById(R.id.text_flag);
        titleMoney = findViewById(R.id.title_money);
        titleLine4 = findViewById(R.id.title_line_4);
        layoutRight = findViewById(R.id.layout_right);
        titleLine1 =  findViewById(R.id.title_line_1);
        arrow = (AppCompatImageView) findViewById(R.id.arrow);
        titleLine2 = findViewById(R.id.title_line_2);
        line = findViewById(R.id.line);
        layoutEdit = findViewById(R.id.layout_edit);
        textCount = findViewById(R.id.text_count);
        disableView = findViewById(R.id.text_disable);
        addView = findViewById(R.id.layout_add);
        btnMin = findViewById(R.id.btn_min);
        editCount = findViewById(R.id.edit_count);
        btnAdd = findViewById(R.id.btn_add);
        iconStatus = findViewById(R.id.icon_status);

        arrow.setVisibility(View.GONE);
        setCheckedLayout(false);
    }

    public void setCheckedLayout(boolean isChecked) {
        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        if (isChecked) {
            int dp8 = Utils.dip2px(8);
            int dp20 = Utils.dip2px(20);
            //layoutRight.setPadding(dp8, dp20, dp8, dp20);
            line.setVisibility(View.VISIBLE);
            layoutEdit.setVisibility(View.VISIBLE);
            checkbox.setVisibility(View.VISIBLE);
            lp.height = Utils.dip2px(160);
        } else {
            line.setVisibility(View.GONE);
            layoutEdit.setVisibility(View.GONE);
            checkbox.setVisibility(View.GONE);
            lp.height = Utils.dip2px(110);
        }
    }

    public void setLayoutEditEnable(boolean enable) {
        btnAdd.setEnabled(enable);
        btnMin.setEnabled(enable);
        editCount.setEnabled(enable);
    }


    public void setExpiredView(boolean isExpired) {

        if (isExpired) {
            titleLine1.setTextColor(getColors(R.color.color_d2d2d2));
            titleLine2.setTextColor(getColors(R.color.color_d2d2d2));
            titleMoney.setTextColor(getColors(R.color.color_eeeeee));
            titleLine4.setTextColor(getColors(R.color.color_999999));
            titleFlag.setTextColor(getColors(R.color.color_eeeeee));
            imageCircle.setImageResource(R.drawable.vector_coupon_gray);
            iconStatus.setText(R.string.text_expired);
            iconStatus.setColor(getColors(R.color.color_929292), getColors(R.color.color_d2d2d2));
            //arrow.setImageResource( R.drawable.ic_arrow_light);
        } else {
            iconStatus.setText(R.string.text_expired_soon);
            iconStatus.setColor(Color.parseColor("#925d00"), Color.parseColor("#fdb72b"));
            titleLine1.setTextColor(getColors(R.color.color_money));
            titleLine2.setTextColor(getColors(R.color.color_515151));
            titleMoney.setTextColor(getColors(R.color.white));
            titleLine4.setTextColor(getColors(R.color.white));
            titleFlag.setTextColor(getColors(R.color.white));
            imageCircle.setImageResource(R.drawable.vector_coupon_red);
        }
    }
}
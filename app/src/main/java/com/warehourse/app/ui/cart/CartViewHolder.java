package com.warehourse.app.ui.cart;

import com.biz.base.BaseViewHolder;
import com.biz.util.DrawableHelper;
import com.biz.widget.CountEditText;
import com.biz.widget.CustomDraweeView;
import com.biz.widget.SwipeLayout;
import com.facebook.drawee.drawable.ScalingUtils;
import com.warehourse.app.R;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.iwgang.simplifyspan.SimplifySpanBuild;
import cn.iwgang.simplifyspan.other.SpecialGravity;
import cn.iwgang.simplifyspan.unit.SpecialClickableUnit;
import cn.iwgang.simplifyspan.unit.SpecialLabelUnit;

/**
 * Title: CartViewHolder
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:12/05/2017  14:47
 *
 * @author johnzheng
 * @version 1.0
 */

public class CartViewHolder extends BaseViewHolder {

    public SwipeLayout swipeLayout;
    public LinearLayout smMenuViewRight;
    public AppCompatImageButton btnRemove;
    public RelativeLayout smContentView;
    public RelativeLayout iconContainer;
    public AppCompatCheckBox checkBox;
    public CustomDraweeView icon;
    public ImageView icon2;
    public TextView icon1;
    public TextView title;
    public TextView tagView;
    public TextView textMinNumber;
    public TextView textMaxNumber;
    public TextView textPrice;
    public TextView textPromo;
    public TextView titleLine3Right;
    public LinearLayout layoutCount;
    public AppCompatImageButton btnMin;
    public CountEditText editCount;
    public AppCompatImageButton btnAdd;


    public CartViewHolder(View view) {
        super(view);
        swipeLayout = findViewById(R.id.swipe_layout);
        smMenuViewRight = findViewById(R.id.smMenuViewRight);
        btnRemove = findViewById(R.id.btn_remove);
        smContentView = findViewById(R.id.smContentView);
        iconContainer = findViewById(R.id.icon_container);
        checkBox = findViewById(R.id.checkbox);
        icon = findViewById(R.id.icon);
        icon2 = findViewById(R.id.icon2);
        icon1 = findViewById(R.id.icon1);
        title = findViewById(R.id.title);
        tagView = findViewById(R.id.title_line_2);
        textMinNumber = findViewById(R.id.text_min_number);
        textMaxNumber = findViewById(R.id.text_max_number);
        textPrice = findViewById(R.id.title_line_3);
        textPromo = findViewById(R.id.text_promo);
        titleLine3Right = findViewById(R.id.title_line_3_right);
        layoutCount = findViewById(R.id.layout);
        btnMin = findViewById(R.id.btn_min);
        editCount = findViewById(R.id.edit_count);
        btnAdd = findViewById(R.id.btn_add);

//        titleLine3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        if (icon != null) {
            icon.setPlaceholderScaleType(ScalingUtils.ScaleType.FIT_XY);
            icon.setOverlay(DrawableHelper.createShapeStrokeDrawable(R.color.color_transparent, R.color.color_d2d2d2, 1, 0));

        }
        if (icon1 != null) {
            icon1.setBackgroundDrawable(getTextDrawable());

            icon1.setText(icon.getResources().getString(R.string.text_out_of_stock));
            icon1.setVisibility(View.GONE);
        }
        if (checkBox != null) {
            checkBox.setVisibility(View.GONE);
        }
        if (icon2 != null) {
            icon2.setImageResource(R.color.color_white_transparent_50);
            icon2.setVisibility(View.GONE);
        }
    }


    public void createTagViews(List<String> list) {
        SimplifySpanBuild spanBuild = new SimplifySpanBuild(tagView.getContext(), tagView);
        int linkNorTextColor = getColors(R.color.color_4a4a4a);
        int linkPressBgColor = getColors(R.color.color_transparent_10);
        for (String s : list) {
            if (!TextUtils.isEmpty(s))
                spanBuild.appendMultiClickableSpecialUnit(
                        new SpecialClickableUnit(null)
                                .setNormalTextColor(linkNorTextColor).setPressBgColor(linkPressBgColor),
                        " ",
                        new SpecialLabelUnit(s.trim(), linkNorTextColor, 15, Color.TRANSPARENT)
                                .showBorder(getColors(R.color.color_divider), 2).setLabelBgRadius(8)
                                .setPadding(10)
                                .setGravity(SpecialGravity.CENTER));

        }
        tagView.setText(spanBuild.build());
    }

    public Drawable getTextDrawable() {
        return DrawableHelper.createCycleShapeWithStrokeDrawable(
                getColors(R.color.color_transparent_half),
                getColors(R.color.color_transparent_half),
                icon1.getMeasuredHeight() / 2);
    }
}

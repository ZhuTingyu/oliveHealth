package com.olive.ui.holder;

import com.biz.base.BaseViewHolder;
import com.biz.widget.CustomDraweeView;
import com.olive.R;
import com.olive.widget.LabelView;

import android.app.Activity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

/**
 * Title: ProductViewHolder
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:20/07/2017  11:34
 *
 * @author johnzheng
 * @version 1.0
 */

public class ProductViewHolder extends BaseViewHolder {

    public CustomDraweeView icon;
    public LabelView iconLabel;
    public TextView tvProductName;
    public TextView tvProductPrice;
    public TextView tvProductPriceOld;
    public AppCompatImageView btnCart;
    public AppCompatImageView btnLike;


    public ProductViewHolder(View itemView) {
        super(itemView);


        icon = (CustomDraweeView) findViewById(R.id.icon);
        iconLabel = (LabelView) findViewById(R.id.icon_label);
        tvProductName = (TextView) findViewById(R.id.tv_product_name);
        tvProductPrice = (TextView) findViewById(R.id.tv_product_price);
        tvProductPriceOld = (TextView) findViewById(R.id.tv_product_price_old);
        btnCart = (AppCompatImageView) findViewById(R.id.btn_cart);
        btnLike = (AppCompatImageView) findViewById(R.id.btn_like);

        Activity activity = (Activity) itemView.getContext();
        if (activity.getTitle().equals(getString(R.string.text_my_favor))){
            btnLike.setVisibility(View.GONE);
        }

    }
}

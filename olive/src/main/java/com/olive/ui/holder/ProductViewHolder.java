package com.olive.ui.holder;

import com.biz.base.BaseActivity;
import com.biz.base.BaseViewHolder;
import com.biz.base.FragmentParentActivity;
import com.biz.widget.CustomDraweeView;
import com.biz.widget.Toolbar;
import com.olive.R;
import com.olive.widget.LabelView;

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


        icon = findViewById(R.id.icon);
        iconLabel = findViewById(R.id.icon_label);
        tvProductName = findViewById(R.id.tv_product_name);
        tvProductPrice = findViewById(R.id.tv_product_price);
        tvProductPriceOld = findViewById(R.id.tv_product_price_old);
        btnCart = findViewById(R.id.btn_cart);
        btnLike = findViewById(R.id.btn_like);

        BaseActivity activity = (BaseActivity) itemView.getContext();
        if (FragmentParentActivity.class.isInstance(activity)) {
            Toolbar toolbar = (Toolbar) activity.getmToolbar();
            String title = toolbar.getmTitleText().getText().toString();
            if (title.equals(getString(R.string.text_my_favor))) {
                if (btnLike != null) {
                    btnLike.setVisibility(View.GONE);
                }
            }
        }
    }
}

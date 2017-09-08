package com.olive.ui.holder;

import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.PriceUtil;
import com.biz.widget.CustomDraweeView;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.util.LoadImageUtil;

/**
 * Created by TingYu Zhu on 2017/9/8.
 */

public class CartProductViewHolder extends BaseViewHolder {

    private CustomDraweeView logo;
    private TextView productName;
    private TextView standard;
    private TextView price;
    private TextView quantity;
    private RelativeLayout rl;
    private AppCompatImageView rightIcon;

    public CartProductViewHolder(View itemView) {
        super(itemView);
        logo = findViewById(R.id.icon_img);
        productName = findViewById(R.id.title);
        standard = findViewById(R.id.title_line_2);
        price = findViewById(R.id.title_line_3);
        quantity = findViewById(R.id.text_product_number);

        rl = findViewById(R.id.rl_info);
        rightIcon = findViewById(R.id.right_icon);

        findViewById(R.id.checkbox).setVisibility(View.GONE);
        findViewById(R.id.number_layout).setVisibility(View.GONE);

    }

    public void bindData(ProductEntity productEntity, boolean isLook){
        LoadImageUtil.Builder()
                .load(productEntity.imgLogo == null ? productEntity.imageLogo : productEntity.imgLogo).http().build()
                .displayImage(logo);
        productName.setText(productEntity.productName == null || productEntity.productName.isEmpty()
                ? productEntity.name : productEntity.productName);
        standard.setText(getString(R.string.text_product_specification_1, productEntity.standard));
        long priceLong = 0;
        if (productEntity.price == 0) {
            if (productEntity.originalPrice != 0) {
                priceLong = productEntity.originalPrice;
            } else {
                priceLong = productEntity.salePrice;
            }
        } else {
            priceLong = productEntity.price;
        }
        price.setText(PriceUtil.formatRMB(priceLong));
        quantity.setVisibility(View.VISIBLE);
        quantity.setText("x" + productEntity.quantity);

        if (!isLook) {
            rl.setPadding(0, 0, com.biz.util.Utils.dip2px(24), 0);
            rightIcon.setVisibility(View.VISIBLE);
        }
    }

}

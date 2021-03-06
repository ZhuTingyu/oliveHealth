package com.olive.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.util.LoadImageUtil;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/26.
 */

public class CheckOrderAdapter extends BaseQuickAdapter<ProductEntity, BaseViewHolder> {
    public CheckOrderAdapter() {
        super(R.layout.item_cart_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, ProductEntity productEntity) {

        String imgUrl;
        if (productEntity.imgLogo != null && !productEntity.imgLogo.isEmpty()) {
            imgUrl = productEntity.imgLogo;
        } else {
            imgUrl = productEntity.imageLogo;
        }

        long price = 0;

        if(productEntity.price == 0){
            if(productEntity.salePrice == 0){
                price = productEntity.originalPrice;
            }else {
                price = productEntity.salePrice;
            }
        }else {
            price = productEntity.price;
        }

        LoadImageUtil.Builder()
                .load(imgUrl).http().build()
                .displayImage(holder.getView(R.id.icon_img));
        holder.setText(R.id.title, productEntity.name == null ? productEntity.productName : productEntity.name);
        holder.setText(R.id.title_line_2, mContext.getString(R.string.text_product_specification, productEntity.standard));
        holder.setText(R.id.title_line_3, PriceUtil.formatRMB(price));
        holder.getView(R.id.checkbox).setVisibility(View.GONE);
        holder.getView(R.id.number_layout).setVisibility(View.GONE);
        holder.getView(R.id.text_product_number).setVisibility(View.VISIBLE);
        holder.setText(R.id.text_product_number, "x" + productEntity.quantity);
    }
}

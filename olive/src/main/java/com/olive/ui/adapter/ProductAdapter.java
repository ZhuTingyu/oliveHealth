package com.olive.ui.adapter;

import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.drawable.ScalingUtils;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.holder.ProductViewHolder;
import com.olive.util.LoadImageUtil;

import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Title: ProductAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:20/07/2017  11:47
 *
 * @author johnzheng
 * @version 1.0
 */

public class ProductAdapter extends BaseQuickAdapter<ProductEntity, ProductViewHolder> {
    public ProductAdapter(@LayoutRes int id) {
        super(id, Lists.newArrayList());
    }

    @Override
    protected void convert(ProductViewHolder holder, ProductEntity item) {
        LoadImageUtil.Builder()
                .load(item.imgLogo).http().build()
                .displayImage(holder.icon);
        holder.tvProductName.setText(item.name);

        if(item.salePrice == 0){
            holder.tvProductPrice.setText(PriceUtil.formatRMB(item.originalPrice));
            holder.tvProductPriceOld.setVisibility(View.GONE);
        }else {
            holder.tvProductPrice.setText(PriceUtil.formatRMB(item.salePrice));
            holder.tvProductPriceOld.setText(PriceUtil.formatRMB(item.originalPrice));
            holder.tvProductPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        }
        if(holder.btnLike != null){
            holder.btnLike.setOnClickListener(v -> {
                v.setSelected(!v.isSelected());
            });
        }
        holder.btnCart.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
        });

    }
}

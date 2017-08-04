package com.olive.ui.adapter;

import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.biz.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.holder.ProductViewHolder;
import com.olive.ui.main.home.ProductsViewModel;
import com.olive.util.LoadImageUtil;

import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.view.View;

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

    private ProductsViewModel viewHolder;

    public ProductAdapter(@LayoutRes int id) {
        super(id, Lists.newArrayList());
        viewHolder = new ProductsViewModel(mContext);
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
                if(v.isSelected()){
                    viewHolder.setProductNo(mData.get(holder.getAdapterPosition()).productNo);
                    viewHolder.addProductFavorites(s -> {
                        ToastUtils.showLong(mContext, s);
                    });
                }
            });
        }
        holder.btnCart.setOnClickListener(v -> {
            viewHolder.setAddProductList(mData.get(holder.getAdapterPosition()));
            viewHolder.addCart(s -> {
                ToastUtils.showLong(mContext, s);
            });
        });

    }
}

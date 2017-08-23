package com.olive.ui.adapter;

import com.biz.util.Lists;
import com.biz.util.LogUtil;
import com.biz.util.PriceUtil;
import com.biz.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.app.OliveApplication;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.holder.ProductViewHolder;
import com.olive.ui.main.home.ProductsViewModel;
import com.olive.util.LoadImageUtil;

import android.app.Application;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.util.Log;
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

    private ProductsViewModel viewModel;

    public ProductAdapter(@LayoutRes int id) {
        super(id, Lists.newArrayList());
    }

    @Override
    protected void convert(ProductViewHolder holder, ProductEntity item) {
        LoadImageUtil.Builder()
                .load(item.imgLogo).http().build()
                .displayImage(holder.icon);
        holder.tvProductName.setText(item.name);

        if (item.salePrice == 0) {
            holder.tvProductPrice.setText(PriceUtil.formatRMB(item.originalPrice));
            holder.tvProductPriceOld.setVisibility(View.GONE);
            if (holder.iconLabel != null) {
                holder.iconLabel.setVisibility(View.GONE);
            }
        } else {
            if (holder.iconLabel != null) {
                holder.iconLabel.setVisibility(View.VISIBLE);
            }
            holder.tvProductPriceOld.setVisibility(View.VISIBLE);
            holder.tvProductPrice.setText(PriceUtil.formatRMB(item.salePrice));
            holder.tvProductPriceOld.setText(PriceUtil.formatRMB(item.originalPrice));
            holder.tvProductPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        }
        if (holder.btnLike != null) {
            holder.btnLike.setOnClickListener(v -> {
                v.setSelected(!v.isSelected());
                viewModel.setProductNo(mData.get(holder.getAdapterPosition() - getHeaderLayoutCount()).productNo);
                if (v.isSelected()) {
                    viewModel.addProductFavorites(s -> {
                        ToastUtils.showLong(mContext, mContext.getString(R.string.text_add_favorites));
                    });
                } else {
                    viewModel.cancelProductFavorites(s -> {
                        ToastUtils.showLong(mContext, mContext.getString(R.string.text_cancel_favorites));
                    });
                }
            });
        }
        holder.btnCart.setOnClickListener(v -> {
            viewModel.setAddProductList(mData.get(holder.getAdapterPosition() - getHeaderLayoutCount()));
            viewModel.addCart(s -> {
                ToastUtils.showLong(mContext, mContext.getString(R.string.text_join_cart_success));
            });
        });

    }

    public void setViewModel(ProductsViewModel viewHolder) {
        this.viewModel = viewHolder;
    }
}

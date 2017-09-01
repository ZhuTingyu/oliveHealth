package com.olive.ui.adapter;

import com.biz.base.BaseActivity;
import com.biz.util.IntentBuilder;
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
import com.olive.ui.order.ProductDetailsFragment;
import com.olive.util.LoadImageUtil;
import com.olive.util.Utils;

import android.app.Application;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

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
                viewModel.setProductNo(item.productNo);
                if (v.isSelected()) {
                    viewModel.addProductFavorites(s -> {
                        item.favorited = ProductsViewModel.IS_FAVORITE;
                        notifyItemChanged(holder.getAdapterPosition() - getHeaderLayoutCount());
                    });
                } else {
                    viewModel.cancelProductFavorites(s -> {
                        item.favorited = ProductsViewModel.NOT_FAVORITE;
                        notifyItemChanged(holder.getAdapterPosition() - getHeaderLayoutCount());
                    });
                }
            });

            holder.btnLike.setSelected(item.favorited == ProductsViewModel.IS_FAVORITE);

        }
        holder.btnCart.setOnClickListener(v -> {
            viewModel.setAddProductList(item);
            viewModel.addCart(s -> {
                ToastUtils.showLong(mContext, mContext.getString(R.string.text_join_cart_success));
            });
        });

        holder.itemView.setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_VALUE, item.productNo)
                    .startParentActivity((BaseActivity)mContext, ProductDetailsFragment.class, true);
        });

    }

    public void setViewModel(ProductsViewModel viewHolder) {
        this.viewModel = viewHolder;
    }

    @Override
    public void setNewData(@Nullable List<ProductEntity> data) {
        super.setNewData(data);
        if(data.isEmpty()){
            Utils.setEmptyView(this,mContext.getString(R.string.message_empty_product));
        }
    }
}

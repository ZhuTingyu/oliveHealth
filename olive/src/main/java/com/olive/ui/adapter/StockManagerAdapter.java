package com.olive.ui.adapter;

import android.support.annotation.Nullable;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.util.LoadImageUtil;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/30.
 */

public class StockManagerAdapter extends BaseQuickAdapter<ProductEntity, BaseViewHolder>{
    public StockManagerAdapter() {
        super(R.layout.item_stock_info_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, ProductEntity productEntity) {
        LoadImageUtil.Builder()
                .load(productEntity.imageLogo).http().build()
                .displayImage(holder.findViewById(R.id.icon_img));
        holder.setText(R.id.title, productEntity.productName);
        holder.setText(R.id.amount_in, mContext.getString(R.string.text_number_in_goods, productEntity.stockQuantity+""));
        holder.setText(R.id.amount_out,mContext.getString(R.string.text_number_out_goods, productEntity.saleQuantity+""));
        holder.setText(R.id.number, productEntity.quantity + "");
    }
}

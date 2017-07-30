package com.olive.ui.adapter;

import android.support.annotation.Nullable;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.util.LoadImageUtil;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/30.
 */

public class StockManagerAdapter extends BaseQuickAdapter<Object, BaseViewHolder>{
    public StockManagerAdapter() {
        super(R.layout.item_stock_info_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, Object o) {
        LoadImageUtil.Builder()
                .load("http://img.taopic.com/uploads/allimg/140326/235113-1403260G01561.jpg").http().build()
                .displayImage(holder.findViewById(R.id.icon_img));
        holder.setText(R.id.title,"52C 五粮液 500ML52C 52C 五粮液 500ML");
        holder.setText(R.id.amount_in,"进货量：500");
        holder.setText(R.id.amount_out,"销售量：500");
        holder.setText(R.id.number, PriceUtil.formatRMB(121231));
    }
}

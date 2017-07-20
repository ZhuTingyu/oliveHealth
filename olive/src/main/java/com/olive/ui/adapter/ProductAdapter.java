package com.olive.ui.adapter;

import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.drawable.ScalingUtils;
import com.olive.R;
import com.olive.ui.holder.ProductViewHolder;
import com.olive.util.LoadImageUtil;

import android.graphics.Paint;
import android.support.annotation.Nullable;

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

public class ProductAdapter extends BaseQuickAdapter<Object, ProductViewHolder> {
    public ProductAdapter() {
        super(R.layout.item_product_grid_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(ProductViewHolder holder, Object item) {
        LoadImageUtil.Builder()
                .load("http://img13.360buyimg.com/imgzone/jfs/t6517/304/1921907774/343777/df918f69/595a01f6Ne19fc737.jpg").http().build()
                .displayImage(holder.icon);
        holder.tvProductName.setText("美国GNC健安喜三倍效力鱼油软胶囊120");
        holder.tvProductPrice.setText(PriceUtil.formatRMB(500));
        holder.tvProductPriceOld.setText(PriceUtil.formatRMB(400));
        holder.tvProductPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

    }
}

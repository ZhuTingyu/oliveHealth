package com.olive.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.util.LoadImageUtil;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/26.
 */

public class CheckOrderAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public CheckOrderAdapter() {
        super(R.layout.item_cart_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        LoadImageUtil.Builder()
                .load("http://img13.360buyimg.com/imgzone/jfs/t6517/304/1921907774/343777/df918f69/595a01f6Ne19fc737.jpg").http().build()
                .displayImage(holder.getView(R.id.icon_img));
        holder.setText(R.id.title, "产品名称");
        holder.setText(R.id.title_line_2, "规格：1000mg*100粒");
        holder.setText(R.id.title_line_3, "¥ 7908.00");
        holder.getView(R.id.checkbox).setVisibility(View.GONE);
        holder.getView(R.id.number_layout).setVisibility(View.GONE);
        holder.getView(R.id.text_product_number).setVisibility(View.VISIBLE);
        holder.setText(R.id.text_product_number, "x3");
    }
}

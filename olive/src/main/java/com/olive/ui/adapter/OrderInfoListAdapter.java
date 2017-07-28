package com.olive.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.olive.R;
import com.olive.util.LoadImageUtil;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class OrderInfoListAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    private Context context;

    public OrderInfoListAdapter(Context context) {
        super(R.layout.item_order_list_info_layout, Lists.newArrayList());
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, Object o) {
        holder.setText(R.id.order_status, "待付款");
        LoadImageUtil.Builder()
                .load("http://img13.360buyimg.com/imgzone/jfs/t6517/304/1921907774/343777/df918f69/595a01f6Ne19fc737.jpg").http().build()
                .displayImage(holder.getView(R.id.icon_img));
        holder.setText(R.id.title, "产品名称");
        holder.setText(R.id.title_line_2, "规格：1000mg*100粒");
        holder.setText(R.id.title_line_3, "¥ 7908.00");
        holder.setText(R.id.text_product_number, "x2");
        holder.setText(R.id.number, context.getString(R.string.text_order_list_info_number, 1+""));
        holder.setText(R.id.price, PriceUtil.formatRMB(9800));
    }
}

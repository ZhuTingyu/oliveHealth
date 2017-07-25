package com.olive.ui.adapter;


import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.olive.R;
import com.olive.util.LoadImageUtil;


/**
 * Created by TingYu Zhu on 2017/7/25.
 */

public class CartAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public CartAdapter() {
        super(R.layout.item_cart_layout, Lists.newArrayList());
    }
    @Override
    protected void convert(BaseViewHolder holder, String o) {
        LoadImageUtil.Builder()
                .load("http://img13.360buyimg.com/imgzone/jfs/t6517/304/1921907774/343777/df918f69/595a01f6Ne19fc737.jpg").http().build()
                .displayImage(holder.getView(R.id.icon_img));
        holder.setText(R.id.title, "产品名称");
        holder.setText(R.id.title_line_2, "规格：1000mg*100粒");
        holder.setText(R.id.title_line_3, "¥ 7908.00");
        holder.setText(R.id.edit_count, "1");

        holder.getView(R.id.btn_min).setOnClickListener(v -> {

        });

        holder.getView(R.id.btn_add).setOnClickListener(v -> {

        });

        holder.getView(R.id.edit_count).setOnClickListener(v -> {

        });

        holder.getView(R.id.checkbox).setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
        });
    }
}

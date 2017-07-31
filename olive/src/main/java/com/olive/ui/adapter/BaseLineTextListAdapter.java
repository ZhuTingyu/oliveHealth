package com.olive.ui.adapter;

import android.support.annotation.Nullable;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/31.
 */

public class BaseLineTextListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    protected String[] title;

    public BaseLineTextListAdapter(String[] title) {
        super(R.layout.item_line_text_layout, Lists.newArrayList());
        this.title = title;
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        holder.setText(R.id.name, title[holder.getAdapterPosition()]);
        holder.setText(R.id.number, s);
    }
}

package com.olive.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/9/7.
 */

public class SystemMessageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SystemMessageAdapter() {
        super(R.layout.item_icon_text_layout
                ,Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        holder.findViewById(R.id.icon).setVisibility(View.GONE);
        holder.setText(R.id.title, s);
    }
}

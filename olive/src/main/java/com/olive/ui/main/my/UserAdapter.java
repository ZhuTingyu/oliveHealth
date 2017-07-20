package com.olive.ui.main.my;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Title: UserAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:20/07/2017  15:35
 *
 * @author johnzheng
 * @version 1.0
 */

public class UserAdapter extends BaseQuickAdapter<String, BaseViewHolder>
        implements FlexibleDividerDecoration.SizeProvider,
        FlexibleDividerDecoration.ColorProvider,HorizontalDividerItemDecoration.MarginProvider{

    public UserAdapter() {
        super(R.layout.item_icon_text_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        holder.setTextView(R.id.title, item);
    }

    @Override
    public int dividerColor(int position, RecyclerView parent) {
        if (position==3||position==5||position==8){
            return parent.getResources().getColor(R.color.color_background);
        }
        return parent.getResources().getColor(R.color.color_divider);

    }

    @Override
    public int dividerSize(int position, RecyclerView parent) {
        if (position==3||position==5||position==8){
            return Utils.dip2px(10);
        }
        return 1;
    }


    @Override
    public int dividerLeftMargin(int position, RecyclerView parent) {
        if (position==3||position==5||position==8){
            return 0;
        }
        return Utils.dip2px(16);
    }

    @Override
    public int dividerRightMargin(int position, RecyclerView parent) {
        return 0;
    }
}

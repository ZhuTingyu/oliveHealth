package com.olive.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.widget.CustomDraweeView;
import com.olive.R;

/**
 * Created by TingYu Zhu on 2017/7/24.
 */

public class NoticeListViewHolder extends BaseViewHolder{
    public TextView time;
    public TextView title;
    public TextView describe;
    public CustomDraweeView icon;

    public NoticeListViewHolder(View itemView) {
        super(itemView);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);
        describe = findViewById(R.id.describe);
        icon = findViewById(R.id.icon);
    }

}

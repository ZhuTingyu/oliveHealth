package com.olive.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.olive.R;

/**
 * Created by TingYu Zhu on 2017/7/24.
 */

public class HomeNoticeViewHolder extends BaseViewHolder {
    public TextView noticeTitle;
    public HomeNoticeViewHolder(View itemView) {
        super(itemView);
        noticeTitle = findViewById(R.id.title);
    }
}

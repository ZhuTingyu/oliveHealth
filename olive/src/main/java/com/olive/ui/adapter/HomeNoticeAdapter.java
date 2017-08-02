package com.olive.ui.adapter;

import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.NoticeEntity;
import com.olive.ui.holder.HomeNoticeViewHolder;

/**
 * Created by TingYu Zhu on 2017/7/24.
 */

public class HomeNoticeAdapter extends BaseQuickAdapter<NoticeEntity, HomeNoticeViewHolder> {
    public HomeNoticeAdapter() {
        super(R.layout.item_notice_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(HomeNoticeViewHolder homeNoticeViewHolder, NoticeEntity entity) {
        homeNoticeViewHolder.noticeTitle.setText(entity.title);
    }
}

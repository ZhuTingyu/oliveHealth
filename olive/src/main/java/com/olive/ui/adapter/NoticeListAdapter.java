package com.olive.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.TimeUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.NoticeInfoEntity;
import com.olive.ui.holder.NoticeListViewHolder;
import com.olive.ui.notice.NoticeDetailFragment;
import com.olive.util.LoadImageUtil;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/24.
 * entity: NoticeInfoEntity
 */

public class NoticeListAdapter extends BaseQuickAdapter<String, NoticeListViewHolder>{

    private Activity activity;

    public NoticeListAdapter(Activity activity) {
        super(R.layout.item_notice_list_layout, Lists.newArrayList());
        this.activity = activity;
    }

    @Override
    protected void convert(NoticeListViewHolder holder, String noticeInfoEntity) {
        holder.time.setText(TimeUtil.format(1500881465, TimeUtil.FORMAT_YYYYMMDDHHMM));
        holder.title.setText("标题");
        holder.describe.setText("五一五一五一五一五一五一五一五一五一五一五一五一五一五一五一五一五一");
        LoadImageUtil.Builder()
                .load("http://img13.360buyimg.com/imgzone/jfs/t6517/304/1921907774/343777/df918f69/595a01f6Ne19fc737.jpg").http().build()
                .displayImage(holder.icon);
        holder.findViewById(R.id.go_detail).setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(activity , NoticeDetailFragment.class, true);
        });
    }
}
package com.olive.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.TimeUtil;
import com.biz.widget.CustomDraweeView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.NoticeEntity;
import com.olive.model.entity.NoticeInfoEntity;
import com.olive.ui.holder.NoticeListViewHolder;
import com.olive.ui.notice.NoticeDetailFragment;
import com.olive.util.LoadImageUtil;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/24.
 * entity: NoticeInfoEntity
 */

public class NoticeListAdapter extends BaseQuickAdapter<NoticeEntity, NoticeListViewHolder>{

    private Activity activity;

    public NoticeListAdapter(Activity activity) {
        super(R.layout.item_notice_list_layout, Lists.newArrayList());
        this.activity = activity;
    }

    @Override
    protected void convert(NoticeListViewHolder holder, NoticeEntity entity) {
        holder.time.setText(TimeUtil.format(entity.publishDate, TimeUtil.FORMAT_YYYYMMDDHHMM));
        holder.title.setText(entity.title);
        holder.describe.setText(entity.intro);
        CustomDraweeView img = holder.icon;
        if(entity.image == null || entity.image.isEmpty()){
            img.setVisibility(View.GONE);
        }else {
            LoadImageUtil.Builder()
                    .load(entity.image).http().build()
                    .displayImage(holder.icon);
        }
        holder.findViewById(R.id.go_detail).setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_VALUE, entity.id)
                    .startParentActivity(activity , NoticeDetailFragment.class, true);
        });
    }
}

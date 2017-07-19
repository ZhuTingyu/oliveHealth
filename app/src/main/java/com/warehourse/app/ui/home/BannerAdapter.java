package com.warehourse.app.ui.home;

import com.biz.base.BaseRecyclerViewAdapter;
import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.util.Utils;
import com.biz.widget.banner.LoopRecyclerViewPager;
import com.biz.widget.banner.PageIndicator;
import com.facebook.drawee.drawable.ScalingUtils;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.HomeEntity;
import com.warehourse.app.model.entity.HomeEntity.HomeItemEntity;
import com.warehourse.app.util.LoadImageUtil;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

import rx.Observable;

/**
 * Title: BannerAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:15/05/2017  17:43
 *
 * @author johnzheng
 * @version 1.0
 */

public class BannerAdapter extends BaseRecyclerViewAdapter<HomeItemEntity> {

    public static View createViewPager(ViewGroup parent, HomeEntity entity) {
        if (HomeEntity.TYPE_SLIDE_BANNER.equals(entity.type)) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_home_viewpager_layout, parent, false);
            LoopRecyclerViewPager viewpager = (LoopRecyclerViewPager) view.findViewById(R.id.viewpager);
            LinearLayoutManager layout = new LinearLayoutManager(parent.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false);
            viewpager.setLayoutManager(layout);
            viewpager.setSinglePageFling(true);
            BannerAdapter bannerAdapter = new BannerAdapter();
            bannerAdapter.setList(entity.items);
            viewpager.setAdapter(bannerAdapter);
            viewpager.setPageIndicator((PageIndicator) view.findViewById(R.id.indicator));
            //parent.addView(view);
            return view;
        }
        return null;
    }

    public void setStringList(List<String> list){
        Observable.from(list)
                .map(s -> {
                    HomeItemEntity entity = new HomeItemEntity();
                    entity.imgUrl = s;
                    return entity;
                }).toList().subscribe(this::setList);

    }

    private int height = 240;

    public BannerAdapter(int height) {
        this.height = height;
    }

    public BannerAdapter() {
        this.height = 240;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomeViewHolder holder = new HomeViewHolder(inflater(R.layout.view_icon_layout, parent));
        holder.itemView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(height))
        );
        holder.icon.setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, int position) {
        HomeViewHolder holder = (HomeViewHolder) viewHolder;
        HomeItemEntity entity = getItem(position);
        holder.bindData(entity);
        holder.setListener(holder.itemView, entity.link);

    }

}

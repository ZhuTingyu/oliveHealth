package com.warehourse.app.ui.home;

import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.widget.CustomDraweeView;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.HomeEntity;
import com.warehourse.app.model.entity.HomeEntity.HomeItemEntity;
import com.warehourse.app.ui.main.MainActivity;
import com.warehourse.app.util.LoadImageUtil;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Title: PromoViewHolder
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:22/05/2017  12:33
 *
 * @author johnzheng
 * @version 1.0
 */

class PromoViewHolder extends BaseViewHolder {

    public CustomDraweeView icon3, icon2, icon1;

    public PromoViewHolder(View itemView) {
        super(itemView);
        icon1 = findViewById(R.id.icon1);
        icon2 = findViewById(R.id.icon2);
        icon3 = findViewById(R.id.icon3);
        icon1.setPressedStateOverlayId(R.color.color_transparent_half);
        icon2.setPressedStateOverlayId(R.color.color_transparent_half);
        icon3.setPressedStateOverlayId(R.color.color_transparent_half);

    }

    public static PromoViewHolder createViewHolder(ViewGroup parent) {
        return new PromoViewHolder(inflater(R.layout.item_home_promo_stage_layout, parent));
    }

    public  static PromoViewHolder setImageShowcase(ViewGroup parent, HomeEntity entity) {
        if (HomeEntity.TYPE_IMAGE_SHOWCASE.equals(entity.type)&&entity.items!=null&&entity.items.size()>0) {
            PromoViewHolder holder = createViewHolder(parent);
            //parent.addView(holder.itemView);
            List<HomeItemEntity> list = entity.items;
            if (list == null || list.size() == 0) return null;
            for (int i = 0; i < list.size(); i++) {
                HomeItemEntity homeItemEntity = list.get(i);
                if (i == 0) {
                    LoadImageUtil.Builder().load(homeItemEntity.imgUrl).build().displayImage(holder.icon1);
                    holder.setListener(holder.icon1, homeItemEntity.link);
                }
                if (i == 1) {
                    LoadImageUtil.Builder().load(homeItemEntity.imgUrl).build().displayImage(holder.icon2);
                    holder.setListener(holder.icon2, homeItemEntity.link);

                }
                if (i == 2) {
                    LoadImageUtil.Builder().load(homeItemEntity.imgUrl).build().displayImage(holder.icon3);
                    holder.setListener(holder.icon3, homeItemEntity.link);

                }
            }
            return holder;
        }
        return null;
    }



    public void setListener(View view, String url){
        if (!TextUtils.isEmpty(url)){
            view.setOnClickListener(v -> {
                MainActivity.startUri(v.getContext(), url);});
        }
    }
}

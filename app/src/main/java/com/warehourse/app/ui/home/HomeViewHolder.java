package com.warehourse.app.ui.home;

import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.widget.CustomDraweeView;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.HomeEntity.HomeItemEntity;
import com.warehourse.app.ui.main.MainActivity;
import com.warehourse.app.util.LoadImageUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * Title: HomeViewHolder
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:22/05/2017  18:37
 *
 * @author johnzheng
 * @version 1.0
 */

public class HomeViewHolder extends BaseViewHolder {

    public CustomDraweeView icon;
    public TextView textTitle;

    public HomeViewHolder(View itemView) {
        super(itemView);
        icon = findViewById(R.id.icon);
        textTitle = findViewById(R.id.title);
    }

    public void setIconView( String url) {
        if (!TextUtils.isEmpty(url)){
            LoadImageUtil.Builder().load(url).build()
                    .imageOptions(R.color.white)
                    .displayImage(icon);
        }
    }

    public void setTitle(String text){
        if (!TextUtils.isEmpty(text) && textTitle!=null){
            textTitle.setText(text);
        }
        if (textTitle!=null){
            textTitle.setVisibility(!TextUtils.isEmpty(text)?View.VISIBLE:View.GONE);
        }
    }


    public void setListener(View view, String url){
        if (!TextUtils.isEmpty(url)){
            view.setOnClickListener(v -> {
                MainActivity.startUri(v.getContext(), url);
            });
        }
    }
    public void bindData(HomeItemEntity entity){
        setIconView(entity.imgUrl);
        setTitle(entity.text);
    }
}

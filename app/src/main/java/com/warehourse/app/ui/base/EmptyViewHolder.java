package com.warehourse.app.ui.base;

import com.biz.base.BaseViewHolder;
import com.warehourse.app.R;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

/**
 * Title: EmptyViewHolder
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:12/05/2017  15:33
 *
 * @author johnzheng
 * @version 1.0
 */

public class EmptyViewHolder extends BaseViewHolder {

    public AppCompatImageView icon;
    public TextView title;
    public TextView empty;
    public AppCompatButton btnEmpty;


    public EmptyViewHolder(View itemView) {
        super(itemView);

        icon = findViewById(R.id.icon);
        title = findViewById(R.id.title);
        empty = findViewById(android.R.id.empty);
        btnEmpty = findViewById(R.id.btn_empty);

    }

    public static EmptyViewHolder createHolder(Context context) {
        View view = View.inflate(context, R.layout.view_empty_layout, null);
        return new EmptyViewHolder(view);
    }

    public EmptyViewHolder setIcon(@DrawableRes int resId) {
        icon.setImageResource(resId);
        return this;
    }

    public EmptyViewHolder setTitle(@StringRes int resId) {
        this.title.setText(resId);
        return this;
    }
    public EmptyViewHolder setTitle( String resId) {
        this.title.setText(resId);
        return this;
    }

}

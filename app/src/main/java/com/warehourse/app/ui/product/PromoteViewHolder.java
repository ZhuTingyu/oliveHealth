package com.warehourse.app.ui.product;


import com.biz.util.DrawableHelper;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.SalePromotionEntity;

import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


@SuppressWarnings("deprecation")
 class PromoteViewHolder {
    public
    TextView name;
    public
    TextView title;
    public
    ImageView icon;
    public View itemView;

    public PromoteViewHolder(ViewGroup parent, SalePromotionEntity info) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promote_layout, parent, false);
        name = (TextView) view.findViewById(R.id.name);
        title = (TextView) view.findViewById(R.id.title);
        icon = (AppCompatImageView) view.findViewById(R.id.icon);

        itemView = view;

        name.setText(info.name);
        name.setBackgroundDrawable(DrawableHelper
                .createShapeStrokeDrawable(R.color.color_transparent, R.color.color_money, 0));
        title.setText(info.description);


        parent.addView(view);
    }



}
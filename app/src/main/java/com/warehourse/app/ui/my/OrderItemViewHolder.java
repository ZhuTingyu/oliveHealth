package com.warehourse.app.ui.my;

import com.biz.base.BaseViewHolder;
import com.biz.util.DrawableHelper;
import com.biz.widget.BadgeView;
import com.warehourse.app.R;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

class OrderItemViewHolder extends BaseViewHolder {
    private RelativeLayout layout;
    private TextView title;
    private BadgeView badgeUnread;


    public OrderItemViewHolder(View itemView) {
        super(itemView);
        layout = findViewById(R.id.layout);
        title = findViewById(R.id.title);
        badgeUnread = findViewById(R.id.badge_unread);
    }

    public static OrderItemViewHolder createHolder(ViewGroup viewGroup, @StringRes int title,
                                                   @DrawableRes int resId, View.OnClickListener onClickListener) {
        View view = inflater(R.layout.item_my_order_item_layout, viewGroup);
        view.setOnClickListener(onClickListener);

        viewGroup.addView(view);
        OrderItemViewHolder holder = new OrderItemViewHolder(view);
        holder.title.setCompoundDrawables(null, DrawableHelper
                .getDrawableWithBounds(holder.title.getContext(), resId), null, null);
        holder.title.setText(title);
        return holder;
    }

    public static OrderItemViewHolder createHolder(ViewGroup viewGroup) {
        View view = inflater(R.layout.item_my_order_item_layout, viewGroup);

        viewGroup.addView(view);
        OrderItemViewHolder holder = new OrderItemViewHolder(view);
        return holder;
    }

    public OrderItemViewHolder setTitle(@StringRes int title) {

        this.title.setText(title);
        return this;
    }

    public OrderItemViewHolder setIcon(@DrawableRes int resId) {

        this.title.setCompoundDrawables(null, DrawableHelper
                .getDrawableWithBounds(this.title.getContext(), resId), null, null);
        return this;
    }

    public OrderItemViewHolder setListener(View.OnClickListener onClickListener) {

        this.itemView.setOnClickListener(onClickListener);

        return this;
    }
}

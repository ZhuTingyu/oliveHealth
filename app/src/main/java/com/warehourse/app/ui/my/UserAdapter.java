package com.warehourse.app.ui.my;

import com.biz.base.BaseRecyclerViewAdapter;
import com.biz.base.BaseViewHolder;
import com.biz.util.GsonUtil;
import com.biz.util.LogUtil;
import com.biz.util.Utils;
import com.biz.widget.BadgeView;
import com.warehourse.app.R;
import com.warehourse.app.event.MainPointEvent;
import com.warehourse.app.event.OrderCountEvent;
import com.warehourse.app.model.entity.OrderCountEntity;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Title: UserAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:11/05/2017  11:39
 *
 * @author johnzheng
 * @version 1.0
 */

 class UserAdapter extends BaseRecyclerViewAdapter<String> implements
        HorizontalDividerItemDecoration.MarginProvider,
        FlexibleDividerDecoration.ColorProvider{

    private View.OnClickListener mOnClickListener;
    private BadgeView waitPayView, waitReceiveView, waitSendView, doneView;
    private OrderCountEvent mCountEvent;
    private MainPointEvent mPointEvent;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public static final int TYPE_ORDER = 10;


    public void notifyEvent(OrderCountEvent event){
        mCountEvent = event;
        notifyDataSetChanged();
    }

    public void notifyEvent(MainPointEvent event){
        mPointEvent = event;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return TYPE_HEADER;
        }else if (position==1){
            return TYPE_ORDER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_ORDER){
            return createOrderHolder(parent);
        }else if (viewType==TYPE_HEADER){
            UserHeaderViewHolder holder = new UserHeaderViewHolder(inflater(R.layout.item_my_header_layout, parent));
            holder.itemView.setPadding(0, Utils.getStatusBarHeight(getActivity()), 0,0);
            return holder;
        }
        return new BaseViewHolder(inflater(R.layout.item_settings_layout, parent));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (getItemViewType(position)==TYPE_ORDER){
            LogUtil.print("aaaa"+mCountEvent);
            if (mCountEvent!=null) {
                LogUtil.print("aaaa"+GsonUtil.toJson(mCountEvent));
                waitPayView.setText(mCountEvent.getWaitingPay());
                waitSendView.setText(mCountEvent.getWaitingDelivery());
                waitReceiveView.setText(mCountEvent.getWaitingReceived());
                //doneView.setText(mCountEvent.getFinished());
            }
        }else if (getItemViewType(position)==TYPE_HEADER){
            UserHeaderViewHolder viewHolder = (UserHeaderViewHolder) holder;
            viewHolder.bindData();
            if (mPointEvent!=null) {
                viewHolder.setDotView(mPointEvent.isPoint());
            }
        }else {
            holder.setTextView(R.id.title, getItem(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }
    }

    private BaseViewHolder createOrderHolder(ViewGroup parent){
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        waitPayView = OrderItemViewHolder.createHolder(layout)
                .setTitle(R.string.tab_wait_pay_order)
                .setIcon(R.drawable.vector_order_wait_pay)
                .setListener(mOnClickListener).findViewById(R.id.badge_unread);

        waitSendView=OrderItemViewHolder.createHolder(layout)
                .setTitle(R.string.tab_wait_send_order)
                .setIcon(R.drawable.vector_order_wait_send)
                .setListener(mOnClickListener).findViewById(R.id.badge_unread);

        waitReceiveView = OrderItemViewHolder.createHolder(layout)
                .setTitle(R.string.tab_wait_receive_order)
                .setIcon(R.drawable.vector_order_wait_receive)
                .setListener(mOnClickListener).findViewById(R.id.badge_unread);

        doneView =OrderItemViewHolder.createHolder(layout)
                .setTitle(R.string.tab_order_done)
                .setIcon(R.drawable.vector_order_done)
                .setListener(mOnClickListener).findViewById(R.id.badge_unread);

        View view = new View(parent.getContext());
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.topMargin = Utils.dip2px(24);
        lp.bottomMargin = lp.topMargin;
        view.setLayoutParams(lp);
        view.setBackgroundResource(R.color.color_divider);
        layout.addView(view);
        OrderItemViewHolder.createHolder(layout)
                .setTitle(R.string.title_order)
                .setIcon(R.drawable.vector_my_order)
                .setListener(mOnClickListener);
        layout.setPadding(0, Utils.dip2px(16), 0, Utils.dip2px(16));
        layout.setBackgroundResource(R.color.color_fbfbfb);
        return new BaseViewHolder(layout);
    }


    @Override
    public int dividerColor(int position, RecyclerView parent) {
        if (position>1)
        return getColor(R.color.color_divider);
        else return 0;
    }

    @Override
    public int dividerLeftMargin(int position, RecyclerView parent) {
        return Utils.dip2px(16);
    }

    @Override
    public int dividerRightMargin(int position, RecyclerView parent) {
        return Utils.dip2px(16);
    }
}

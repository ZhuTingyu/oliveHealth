
package com.biz.share;


import com.biz.base.BaseRecyclerViewAdapter;
import com.biz.base.BaseViewHolder;
import com.biz.http.R;
import com.biz.util.DrawableHelper;
import com.biz.util.Lists;
import com.biz.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Title: ShareAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:16/9/12  20:09
 *
 * @author zhengyao
 * @version 1.0
 */

public class ShareAdapter extends BaseQuickAdapter<String, BaseViewHolder>  {


    private int numCount = 4;
    protected List<Integer> icons = Lists.newArrayList(R.drawable.ic_wechat_friend, R.drawable.ic_wechat_circle,
            R.drawable.ic_qq, R.drawable.ic_qq_zone);

    protected  List<String> list;


    public void setNumCount(int numCount) {
        this.numCount = numCount;
    }

    public ShareAdapter(Context context) {
        super(R.layout.item_share_layout);
        list = Lists.newArrayList( context.getString(R.string.text_wechat_friend),
                context.getString(R.string.text_wechat_cycle),
                context.getString(R.string.text_qq),
                context.getString(R.string.text_qzone));
        setNewData(list);
    }




    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.itemView.setBackgroundDrawable(DrawableHelper
                .createShapeStrokeDrawable(R.color.color_transparent, R.color.color_eeeeee,0));
        DisplayMetrics dm = parent.getResources().getDisplayMetrics();
        int size = (dm.widthPixels - Utils.dip2px(0)) / numCount;
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        lp.width = size;
        lp.height = size;
        holder.itemView.setLayoutParams(lp);

        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        TextView title = (TextView) holder.itemView.findViewById(R.id.title);
        ImageView icon = (ImageView) holder.itemView.findViewById(R.id.icon);
        icon.setImageResource(icons.get(position));
        title.setText(getItem(position));
        holder.itemView.setTag(getItem(position));
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
    }



}
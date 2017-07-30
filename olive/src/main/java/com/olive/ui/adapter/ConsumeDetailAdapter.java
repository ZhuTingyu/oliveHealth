package com.olive.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.biz.util.TimeUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/30.
 */

public class ConsumeDetailAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    private Context context;

    public ConsumeDetailAdapter(Context context) {
        super(R.layout.item_consume_detail_layout, Lists.newArrayList());
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, Object o) {
        holder.setText(R.id.date, TimeUtil.format(1501296465, TimeUtil.FORMAT_YYYYMMDD));
        holder.setText(R.id.status,"订单支付");
        holder.setText(R.id.price, PriceUtil.formatRMB(12312343));

        LinearLayout linearLayout = holder.findViewById(R.id.ll_info);
        for(int i = 0; i < 3; i++){
            View view = LayoutInflater.from(context).inflate(R.layout.item_refund_products_info_layout, linearLayout,false);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView number = (TextView) view.findViewById(R.id.number);
            name.setText("汤臣倍健 鱼油软胶囊");
            number.setText("x2");
            linearLayout.addView(view);
        }
    }
}

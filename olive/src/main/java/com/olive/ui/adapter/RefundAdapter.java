package com.olive.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.TimeUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.ui.refund.LookRefundCheckResult;
import com.olive.ui.refund.RefundActivity;
import com.olive.ui.refund.RefundBaseFragment;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/29.
 */

public class RefundAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    private Activity context;
    private String type;

    public RefundAdapter(Activity context, String type) {
        super(R.layout.item_refund_info_layout, Lists.newArrayList());
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, Object o) {
        holder.setText(R.id.status,"待退款");

        LinearLayout linearLayout = holder.findViewById(R.id.ll_info);
        for(int i = 0; i < 3; i++){
            View view = LayoutInflater.from(context).inflate(R.layout.item_refund_products_info_layout, linearLayout,false);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView number = (TextView) view.findViewById(R.id.number);
            name.setText("汤臣倍健 鱼油软胶囊");
            number.setText("x2");
            linearLayout.addView(view);
        }

        TextView service = holder.findViewById(R.id.contact_service);
        TextView look = holder.findViewById(R.id.look_result);

        service.setOnClickListener(v -> {

        });

        look.setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(context, LookRefundCheckResult.class, true);
        });


        if(context.getString(R.string.text_refund_list).equals(type)){
            TextView date = holder.findViewById(R.id.date);
            TextView totalNumber = holder.findViewById(R.id.total_number);
            TextView remark = holder.findViewById(R.id.remark);

            remark.setVisibility(View.VISIBLE);
            totalNumber.setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
            service.setVisibility(View.GONE);
            look.setVisibility(View.GONE);

            date.setText(TimeUtil.format(1501296465, TimeUtil.FORMAT_YYYYMMDD));
            totalNumber.setText(context.getString(R.string.text_products_total_number,3+""));
            remark.setText("【订单审核备注】方法方法方法方法方法方法\n" +
                    "方法方法方法方法方法方法方法方法方法\n" +
                    "方法方法方法方法方法方法方法方法方法方法方法方法方法方法方法方法方法");
        }

    }
}

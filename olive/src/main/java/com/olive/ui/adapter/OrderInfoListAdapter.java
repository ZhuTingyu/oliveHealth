package com.olive.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.util.LoadImageUtil;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class OrderInfoListAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    private Context context;
    private String type;

    public OrderInfoListAdapter(Context context, String type) {
        super(R.layout.item_order_list_info_layout, Lists.newArrayList());
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, Object o) {
        holder.setText(R.id.order_status, "待付款");
        LoadImageUtil.Builder()
                .load("http://img13.360buyimg.com/imgzone/jfs/t6517/304/1921907774/343777/df918f69/595a01f6Ne19fc737.jpg").http().build()
                .displayImage(holder.getView(R.id.icon_img));
        holder.setText(R.id.title, "产品名称");
        holder.setText(R.id.title_line_2, "规格：1000mg*100粒");
        holder.setText(R.id.title_line_3, "¥ 7908.00");
        holder.setText(R.id.text_product_number, "x2");
        holder.setText(R.id.number, context.getString(R.string.text_order_list_info_number, 1+""));
        holder.setText(R.id.price, PriceUtil.formatRMB(9800));

        TextView leftBtn = holder.findViewById(R.id.btn_left);
        TextView rightBtn = holder.findViewById(R.id.btn_right);

        if(context.getString(R.string.text_waiting_pay).equals(type)){
            //待支付
        }else if(context.getString(R.string.text_wait_send).equals(type)){
            //待发货
        }else if(context.getString(R.string.text_wait_receive).equals(type)){
            //待收货
        }else if(context.getString(R.string.text_order_complete).equals(type)){
            //已完成
        }else if(context.getString(R.string.text_order_cancel).equals(type)){
            //已经取消
        }

    }

}

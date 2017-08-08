package com.olive.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.biz.widget.recyclerview.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.OrderEntity;
import com.olive.model.entity.OrderListEntity;
import com.olive.model.entity.ProductEntity;
import com.olive.util.LoadImageUtil;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class OrderInfoListAdapter extends BaseQuickAdapter<OrderListEntity, BaseViewHolder> {
    private String type;

    public OrderInfoListAdapter(String type) {
        super(R.layout.item_order_list_info_layout, Lists.newArrayList());
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, OrderListEntity orderListEntity) {
        holder.setText(R.id.order_status, "待付款");

        LinearLayout linearLayout = holder.findViewById(R.id.list);
        /*recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        CheckOrderAdapter adapter = new CheckOrderAdapter();
        adapter.setNewData(Lists.newArrayList("","",""));
        if(recyclerView.getAdapter() == null){
            recyclerView.setAdapter(adapter);
        }*/

        List<ProductEntity> products = orderListEntity.products;

        for(ProductEntity productEntity : products){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart_layout, linearLayout ,false);
            BaseViewHolder holder1 = new BaseViewHolder(view);
            LoadImageUtil.Builder()
                    .load(productEntity.imgLogo).http().build()
                    .displayImage(holder1.getView(R.id.icon_img));
            holder1.setText(R.id.title, productEntity.productName);
            holder1.setText(R.id.title_line_2, mContext.getString(R.string.text_product_specification, productEntity.standard));
            holder1.setText(R.id.title_line_3, PriceUtil.formatRMB(productEntity.price));
            holder1.getView(R.id.checkbox).setVisibility(View.GONE);
            holder1.getView(R.id.number_layout).setVisibility(View.GONE);
            holder1.getView(R.id.text_product_number).setVisibility(View.VISIBLE);
            holder1.setText(R.id.text_product_number, "x"+productEntity.quantity);
            linearLayout.addView(view);
        }




        TextView leftBtn = holder.findViewById(R.id.btn_left);
        TextView rightBtn = holder.findViewById(R.id.btn_right);

        leftBtn.setOnClickListener(v -> {

        });

        rightBtn.setOnClickListener(v -> {

        });

        if(mContext.getString(R.string.text_waiting_pay).equals(type)){
            //待支付
        }else if(mContext.getString(R.string.text_wait_send).equals(type)){
            //待发货
        }else if(mContext.getString(R.string.text_wait_receive).equals(type)){
            //待收货
        }else if(mContext.getString(R.string.text_order_complete).equals(type)){
            //已完成
        }else if(mContext.getString(R.string.text_order_cancel).equals(type)){
            //已经取消
        }
    }
}

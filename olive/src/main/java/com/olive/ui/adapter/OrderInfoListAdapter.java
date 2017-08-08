package com.olive.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.OrderEntity;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.order.PayDebtFragment;
import com.olive.ui.order.BasePayFragment;
import com.olive.ui.order.PayOrderFragment;
import com.olive.ui.order.viewModel.OrderListViewModel;
import com.olive.ui.order.viewModel.OrderViewModel;
import com.olive.util.LoadImageUtil;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class OrderInfoListAdapter extends BaseQuickAdapter<OrderEntity, BaseViewHolder> {

    private OrderListViewModel viewModel;
    private OrderViewModel orderViewModel;
    private BaseFragment fragment;

    public OrderInfoListAdapter(BaseFragment fragment) {
        super(R.layout.item_order_list_info_layout, Lists.newArrayList());
        this.fragment = fragment;
        orderViewModel = new OrderViewModel(fragment);
    }

    @Override
    protected void convert(BaseViewHolder holder, OrderEntity orderEntity) {

        LinearLayout linearLayout = holder.findViewById(R.id.list);
        /*recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        CheckOrderAdapter adapter = new CheckOrderAdapter();
        adapter.setNewData(Lists.newArrayList("","",""));
        if(recyclerView.getAdapter() == null){
            recyclerView.setAdapter(adapter);
        }*/


        List<ProductEntity> products = orderEntity.products;

        for (ProductEntity productEntity : products) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart_layout, linearLayout, false);
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
            holder1.setText(R.id.text_product_number, "x" + productEntity.quantity);
            linearLayout.addView(view);
        }

        holder.setText(R.id.price, PriceUtil.formatRMB(orderEntity.amount));
        holder.setText(R.id.number, mContext.getString(R.string.text_order_list_info_number, getTotalCount(products) + ""));


        TextView status = holder.findViewById(R.id.order_status);
        TextView leftBtn = holder.findViewById(R.id.btn_left);
        TextView rightBtn = holder.findViewById(R.id.btn_right);

        String statusString = orderViewModel.getOrderStatus(orderEntity);
        status.setText(statusString);

        if (mContext.getString(R.string.text_waiting_pay).equals(statusString)) {
            //待支付
            initWaitPay(holder, leftBtn, rightBtn, orderEntity);
        } else if (mContext.getString(R.string.text_wait_send).equals(statusString)) {
            //待发货
            initWaitSend(leftBtn, rightBtn);
        } else if (mContext.getString(R.string.text_wait_receive).equals(statusString)) {
            //待收货
            initWaitReceive(leftBtn, rightBtn, orderEntity);
        } else if (mContext.getString(R.string.text_order_complete).equals(statusString)) {
            //已完成
            initComplete(leftBtn, rightBtn);
        } else if (mContext.getString(R.string.text_order_cancel).equals(statusString)) {
            //已经取消
            initCancel(leftBtn, rightBtn, orderEntity);
        }
    }

    private void initCancel(TextView leftBtn, TextView rightBtn, OrderEntity orderEntity) {
        leftBtn.setVisibility(View.GONE);
        rightBtn.setText(R.string.text_buy_again);
        rightBtn.setOnClickListener(v -> {
            //TODO 再次购买
        });
    }

    private void initWaitReceive(TextView leftBtn, TextView rightBtn, OrderEntity orderEntity) {
        leftBtn.setVisibility(View.GONE);
        rightBtn.setText(mContext.getString(R.string.text_make_sure_receive));
        rightBtn.setOnClickListener(v -> {
            fragment.setProgressVisible(true);
            orderViewModel.setOrderNo(orderEntity.orderNo);
            orderViewModel.confirmOrder(s -> {
                fragment.setProgressVisible(false);
            });
        });
    }

    private void initComplete(TextView leftBtn, TextView rightBtn) {
        leftBtn.setVisibility(View.GONE);
        rightBtn.setVisibility(View.GONE);
    }

    private void initWaitSend(TextView leftBtn, TextView rightBtn) {
        leftBtn.setVisibility(View.GONE);
        rightBtn.setVisibility(View.GONE);
    }

    private void initWaitPay(BaseViewHolder holder, TextView leftBtn, TextView rightBtn, OrderEntity orderEntity) {
        leftBtn.setOnClickListener(v -> {
            fragment.setProgressVisible(true);
            orderViewModel.setOrderNo(orderEntity.orderNo);
            orderViewModel.cancelOrder(s -> {
                fragment.setProgressVisible(false);
                remove(holder.getAdapterPosition());
                //TODO 刷新 已经取消列表的数据
            });
        });

        rightBtn.setOnClickListener(v -> {
            payOrder(orderEntity);
        });

    }

    public void setViewModel(OrderListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public int getTotalCount(List<ProductEntity> productEntities) {
        int count = 0;
        for (ProductEntity productEntity : productEntities) {
            count += productEntity.quantity;
        }
        return count;
    }

    private void payOrder(OrderEntity orderEntity) {
        if (orderViewModel.isHasDebt(orderEntity)) {
            IntentBuilder.Builder()
                    .startParentActivity((Activity) mContext, PayDebtFragment.class, true);
        } else {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_DATA, orderEntity)
                    .startParentActivity((Activity) mContext, PayOrderFragment.class, true);

        }
    }

}

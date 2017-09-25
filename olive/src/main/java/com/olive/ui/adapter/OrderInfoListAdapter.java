package com.olive.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.DialogUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.event.OrderListUpdateEvent;
import com.olive.model.entity.OrderEntity;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.main.cart.CartFragment;
import com.olive.ui.main.home.ProductsViewModel;
import com.olive.ui.main.my.account.viewModel.AccountViewModel;
import com.olive.ui.order.PayDebtFragment;
import com.olive.ui.order.PayOrderFragment;
import com.olive.ui.order.viewModel.OrderListViewModel;
import com.olive.ui.order.viewModel.OrderViewModel;
import com.olive.util.LoadImageUtil;
import com.olive.util.Utils;
import com.olive.widget.LinearLayoutForRecyclerView;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class OrderInfoListAdapter extends BaseQuickAdapter<OrderEntity, BaseViewHolder> {

    private OrderViewModel orderViewModel;
    private AccountViewModel accountViewModel;
    private BaseFragment fragment;
    private ProductsViewModel productsViewModel;
    private ProductInfoWithNumberAdapter adapter;

    private static final int DMS_CODE = 1;

    private static final String ORDER_SOURCE_DMS = "DMS";
    private static final String ORDER_SOURCE_ERP = "ERP";

    public OrderInfoListAdapter(BaseFragment fragment) {
        super(R.layout.item_order_list_info_layout, Lists.newArrayList());
        this.fragment = fragment;
        orderViewModel = new OrderViewModel(fragment);
        accountViewModel = new AccountViewModel(fragment);
        productsViewModel = new ProductsViewModel(fragment);
    }

    @Override
    protected void convert(BaseViewHolder holder, OrderEntity orderEntity) {

        LinearLayoutForRecyclerView linearLayout = holder.findViewById(R.id.list);
        List<ProductEntity> products = orderEntity.products;

        if(adapter == null) {
            adapter = new ProductInfoWithNumberAdapter(mContext, products, true);
        }
        adapter.setData(orderEntity.products);
        linearLayout.setAdapter(adapter);


        holder.setText(R.id.price, PriceUtil.formatRMB(orderEntity.amount));
        holder.setText(R.id.number, mContext.getString(R.string.text_order_list_info_number, getTotalCount(orderEntity.products) + ""));
        holder.setText(R.id.order_source, orderEntity.source == DMS_CODE ?
                mContext.getString(R.string.text_order_source, ORDER_SOURCE_DMS)
                : mContext.getString(R.string.text_order_source, ORDER_SOURCE_ERP) );


        TextView status = holder.findViewById(R.id.order_status);
        TextView leftBtn = holder.findViewById(R.id.btn_left);
        TextView rightBtn = holder.findViewById(R.id.btn_right);

        String statusString = orderViewModel.getOrderStatus(orderEntity);
        status.setText(statusString);

        if (mContext.getString(R.string.text_waiting_pay).equals(statusString)) {
            //待支付
            initWaitPay(holder, leftBtn, rightBtn, orderEntity);
            status.setTextColor(mContext.getResources().getColor(R.color.red_light));
        } else if (mContext.getString(R.string.text_wait_send).equals(statusString)) {
            //待发货
            initWaitSend(leftBtn, rightBtn);
            status.setTextColor(mContext.getResources().getColor(R.color.orange_light));
        } else if (mContext.getString(R.string.text_wait_receive).equals(statusString)) {
            //待收货
            initWaitReceive(holder, leftBtn, rightBtn, orderEntity);
            status.setTextColor(mContext.getResources().getColor(R.color.blue_light));
        } else if (mContext.getString(R.string.text_order_complete).equals(statusString)) {
            //已完成
            initComplete(leftBtn, rightBtn);
            status.setTextColor(mContext.getResources().getColor(R.color.green_light));
        } else if (mContext.getString(R.string.text_order_cancel).equals(statusString)) {
            //已经取消
            initCancel(leftBtn, rightBtn, orderEntity);
            status.setTextColor(mContext.getResources().getColor(R.color.red_light));
        }
    }

    private void initCancel(TextView leftBtn, TextView rightBtn, OrderEntity orderEntity) {
        leftBtn.setVisibility(View.GONE);
        rightBtn.setText(R.string.text_buy_again);
        rightBtn.setOnClickListener(v -> {
            productsViewModel.setAddProductList(orderEntity.products);
            productsViewModel.addCart(s -> {
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_BOOLEAN_KUAIHE, true)
                        .putExtra(IntentBuilder.KEY_VALUE, orderEntity.products.size())
                        .putExtra(IntentBuilder.KEY_BOOLEAN, true)
                        .startParentActivity(fragment.getActivity(), CartFragment.class, false);
            });
        });
    }

    private void initWaitReceive(BaseViewHolder holder, TextView leftBtn, TextView rightBtn, OrderEntity orderEntity) {
        leftBtn.setVisibility(View.GONE);
        rightBtn.setText(mContext.getString(R.string.text_make_sure_receive));
        rightBtn.setOnClickListener(v -> {


            DialogUtil.createDialogView(mContext, R.string.message_make_sure_goods_receipt, null, R.string.btn_cancel,
                    (dialog, which) -> {

                        fragment.setProgressVisible(true);
                        orderViewModel.setOrderNo(orderEntity.orderNo);
                        orderViewModel.confirmOrder(s -> {
                            fragment.setProgressVisible(false);
                            mData.remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                            EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_ORDER_COMPLETE));
                            EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_ALL));
                        });

                    }, R.string.btn_confirm);

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

            DialogUtil.createDialogView(mContext, R.string.message_is_cancel_order, null, R.string.btn_cancel,
                    (dialog, which) -> {
                        fragment.setProgressVisible(true);
                        orderViewModel.setOrderNo(orderEntity.orderNo);
                        orderViewModel.cancelOrder(s -> {
                            fragment.setProgressVisible(false);
                            remove(holder.getAdapterPosition());
                            EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_ORDER_CANCEL));
                            EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_ALL));
                        });
                    }, R.string.btn_confirm);

        });

        rightBtn.setOnClickListener(v -> {
            payOrder(orderEntity);
        });

    }

    public int getTotalCount(List<ProductEntity> productEntities) {
        int count = 0;
        for (ProductEntity productEntity : productEntities) {
            count += productEntity.quantity;
        }
        return count;
    }

    private void payOrder(OrderEntity orderEntity) {

        accountViewModel.getAccountInfo(accountEntity -> {
            if (orderViewModel.isHasDebt(orderEntity)) {
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_VALUE, accountEntity)
                        .putExtra(IntentBuilder.KEY_DATA, orderEntity)
                        .startParentActivity((Activity) mContext, PayDebtFragment.class, true);
            } else {
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_VALUE, accountEntity)
                        .putExtra(IntentBuilder.KEY_DATA, orderEntity)
                        .startParentActivity((Activity) mContext, PayOrderFragment.class, true);

            }
        });
    }

    @Override
    public void setNewData(@Nullable List<OrderEntity> data) {
        super.setNewData(data);
        if (data.isEmpty()) {
            Utils.setEmptyView(this, mContext.getString(R.string.message_empty_order_list));
        }
    }
}

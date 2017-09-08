package com.olive.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.DialogUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.PriceUtil;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.event.OrderListUpdateEvent;
import com.olive.model.UserModel;
import com.olive.model.entity.OrderEntity;
import com.olive.ui.BaseErrorFragment;
import com.olive.ui.adapter.BaseLineTextListAdapter;
import com.olive.ui.adapter.CheckOrderAdapter;
import com.olive.ui.main.cart.CartFragment;
import com.olive.ui.order.viewModel.OrderDetailViewModel;
import com.olive.ui.order.viewModel.OrderListViewModel;
import com.olive.widget.LinearLayoutForRecyclerView;


import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by TingYu Zhu on 2017/7/31.
 */

public class OrderDetailsFragment extends BaseErrorFragment {
    private XRecyclerView recyclerView;
    private CheckOrderAdapter adapter;
    private String status;
    private List<Object> data;
    private OrderEntity orderEntity;

    private OrderDetailViewModel viewModel;

    private View head;
    private View footView;
    private LinearLayoutForRecyclerView footList;
    private LinearLayout btns;
    private TextView btnLeft;
    private TextView btnRight;
    private TextView btnOk;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new OrderDetailViewModel(context);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_details_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.title_order_detail));
        initView();
        initData();
    }


    private void initData() {
        viewModel.getOrderDetail(orderEntity -> {
            this.orderEntity = viewModel.orderEntity;
            status = viewModel.getOrderStatus(orderEntity);
            viewModel.setOrderNo(orderEntity.orderNo);
            adapter.setNewData(orderEntity.products);
            initButton();
            adapter.removeAllFooterView();
            adapter.removeAllHeaderView();
            bindHeadView();
            footList.removeAllViews();
            bindFoodView();
            recyclerView.setRefreshing(false);
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setFocusable(false);
        adapter = new CheckOrderAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
        recyclerView.setRefreshListener(() -> {
            initData();
        });

        head = View.inflate(getContext(), R.layout.item_order_details_head_layout, null);

        footView = LayoutInflater.from(getContext()).inflate(R.layout.item_order_details_foot_layout, null);
        footList = (LinearLayoutForRecyclerView) footView.findViewById(R.id.list);

        btns = findViewById(R.id.ll_btn);
        btnLeft = findViewById(R.id.btn_left);
        btnRight = findViewById(R.id.btn_right);
        btnOk = findViewById(R.id.btn_sure);
    }

    private void initButton() {

        if (getString(R.string.text_waiting_pay).equals(status)) {

            btns.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.GONE);

            //待支付
            btnLeft.setOnClickListener(v -> {
                DialogUtil.createDialogView(getContext(), R.string.message_is_cancel_order,null,R.string.btn_cancel,
                        (dialog, which) -> {
                            setProgressVisible(true);
                            viewModel.cancelOrder(s -> {
                                setProgressVisible(false);
                                EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_ORDER_CANCEL));
                                EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_WAIT_PAY));
                                EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_ALL));
                                getActivity().finish();
                            });
                        },R.string.btn_confirm);
            });



            btnRight.setOnClickListener(v -> {
                if (viewModel.isHasDebt(orderEntity)) {
                    IntentBuilder.Builder()
                            .putExtra(IntentBuilder.KEY_DATA, orderEntity)
                            .startParentActivity(getBaseActivity(), PayDebtFragment.class, true);
                } else {
                    IntentBuilder.Builder()
                            .putExtra(IntentBuilder.KEY_DATA, orderEntity)
                            .startParentActivity(getBaseActivity(), PayOrderFragment.class, true);
                }
            });

        } else if (getString(R.string.text_wait_send).equals(status)) {
            //待发货
            btns.setVisibility(View.GONE);
            btnOk.setVisibility(View.GONE);
        } else if (getString(R.string.text_wait_receive).equals(status)) {
            //待收货
            btns.setVisibility(View.GONE);
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setText(getString(R.string.text_make_sure_receive));
            btnOk.setOnClickListener(v -> {
                DialogUtil.createDialogView(getContext(), R.string.message_make_sure_goods_receipt, null, R.string.btn_cancel,
                        (dialog, which) -> {
                            setProgressVisible(true);
                            viewModel.setOrderNo(orderEntity.orderNo);
                            viewModel.confirmOrder(s -> {
                                setProgressVisible(false);
                                EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_ORDER_COMPLETE));
                                EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_WAIT_RECEIVE));
                                EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_ALL));
                                getActivity().finish();
                            });

                        }, R.string.btn_confirm);
            });
        } else if (getString(R.string.text_order_complete).equals(status)) {
            //已完成
            btns.setVisibility(View.GONE);
            btnOk.setVisibility(View.GONE);

        } else if (getString(R.string.text_order_cancel).equals(status)) {
            btns.setVisibility(View.GONE);
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setText(getString(R.string.text_buy_again));
            btnOk.setOnClickListener(v -> {
                viewModel.setAddProductList(orderEntity.products);
                viewModel.addCart(s -> {
                    IntentBuilder.Builder()
                            .putExtra(IntentBuilder.KEY_BOOLEAN_KUAIHE, true)
                            .putExtra(IntentBuilder.KEY_VALUE, orderEntity.products.size())
                            .putExtra(IntentBuilder.KEY_BOOLEAN, true)
                            .startParentActivity(getActivity(), CartFragment.class, false);
                });
            });
        }
    }

    private void bindFoodView() {
        String[] title = viewModel.getOderInfoTitle();
        List<String> content = viewModel.getOrderInfo();

        /*for(int i = 0, len = title.length; i < len; i++){
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_line_text_layout, footList, false);
            TextView tvTitle = (TextView) view.findViewById(R.id.name);
            TextView tvContent = (TextView) view.findViewById(R.id.number);

            tvTitle.setText(title[i]);
            tvContent.setText(content.get(i));

            footList.addView(view);
        }*/

        BaseLineTextListAdapter baseLineTextListAdapter =
                new BaseLineTextListAdapter(getContext(),title, content);
        footList.setAdapter(baseLineTextListAdapter);

        TextView number = (TextView) footView.findViewById(R.id.number);
        TextView price = (TextView) footView.findViewById(R.id.price);

        number.setText(getContext().getString(R.string.text_products_total_money, viewModel.getTotalCount(orderEntity.products) + ""));
        price.setText(PriceUtil.formatRMB(orderEntity.amount));

        adapter.addFooterView(footView);
    }

    private void bindHeadView() {
        BaseViewHolder holder = new BaseViewHolder(head);
        TextView tvStatus = holder.findViewById(R.id.status);
        tvStatus.setText(status);
        if (getString(R.string.text_waiting_pay).equals(status)) {
            //待支付
            tvStatus.setTextColor(getResources().getColor(R.color.red_light));
        } else if (getString(R.string.text_wait_send).equals(status)) {
            //待发货
            tvStatus.setTextColor(getResources().getColor(R.color.orange_light));
        } else if (getString(R.string.text_wait_receive).equals(status)) {
            //待收货
            tvStatus.setTextColor(getResources().getColor(R.color.blue_light));
        } else if (getString(R.string.text_order_complete).equals(status)) {
            //已完成
            tvStatus.setTextColor(getResources().getColor(R.color.green_light));
        } else if (getString(R.string.text_order_cancel).equals(status)) {
            //已经取消
            tvStatus.setTextColor(getResources().getColor(R.color.red_light));
        }
        holder.setText(R.id.address, UserModel.getInstance().getNickName());
        holder.setText(R.id.name, orderEntity.consigneeName);
        holder.setText(R.id.phone, orderEntity.mobile);
        holder.setText(R.id.address_1, orderEntity.address);
        adapter.addHeaderView(head);
    }


}

package com.olive.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.biz.util.TimeUtil;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.event.OrderListUpdateEvent;
import com.olive.model.UserModel;
import com.olive.model.entity.OrderEntity;
import com.olive.ui.adapter.CheckOrderAdapter;
import com.olive.ui.adapter.OrderFootAdapter;
import com.olive.ui.order.viewModel.OrderDetailViewModel;
import com.olive.ui.order.viewModel.OrderListViewModel;
import com.olive.ui.order.viewModel.OrderViewModel;

import org.w3c.dom.Text;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by TingYu Zhu on 2017/7/31.
 */

public class OrderDetailsFragment extends BaseFragment {
    private XRecyclerView recyclerView;
    private CheckOrderAdapter adapter;
    private String status;
    private List<Object> data;
    private OrderEntity orderEntity;

    private OrderDetailViewModel viewModel;
    private OrderViewModel orderViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new OrderDetailViewModel(context);
        orderViewModel = new OrderViewModel(context);
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
        initData();
    }


    private void initData() {
        viewModel.getOrderDetail(orderEntity -> {
            this.orderEntity = viewModel.orderEntity;
            status = orderViewModel.getOrderStatus(orderEntity);
            orderViewModel.setOrderNo(orderEntity.orderNo);
            initView();
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setFocusable(false);
        adapter = new CheckOrderAdapter();
        adapter.setNewData(orderEntity.products);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);

        initButton();
        initHeadView();
        initFoodView();

    }

    private void initButton() {

        LinearLayout btns = findViewById(R.id.ll_btn);
        TextView btnLeft = findViewById(R.id.btn_left);
        TextView btnRight = findViewById(R.id.btn_right);
        TextView btnOk = findViewById(R.id.btn_sure);

        if (getString(R.string.text_waiting_pay).equals(status)) {
            //待支付
            btnLeft.setOnClickListener(v -> {
                setProgressVisible(true);
                orderViewModel.cancelOrder(s -> {
                    setProgressVisible(false);
                    EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_ORDER_CANCEL));
                    EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_WAIT_PAY));
                    EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_ALL));
                    getActivity().finish();
                });
            });

            btnRight.setOnClickListener(v -> {
                if (orderViewModel.isHasDebt(orderEntity)) {
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
        } else if (getString(R.string.text_wait_receive).equals(status)) {
            //待收货
            btns.setVisibility(View.GONE);
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setText(getString(R.string.text_make_sure_receive));
            btnOk.setOnClickListener(v -> {
                setProgressVisible(true);
                orderViewModel.confirmOrder(s -> {
                    setProgressVisible(false);
                });
            });
        } else if (getString(R.string.text_order_complete).equals(status)) {
            //已完成
            btns.setVisibility(View.GONE);
            btnOk.setVisibility(View.GONE);

        } else if (getString(R.string.text_order_cancel).equals(status)) {
            btns.setVisibility(View.GONE);
            btnOk.setText(getString(R.string.text_buy_again));
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setOnClickListener(v -> {
                IntentBuilder.Builder().putExtra(IntentBuilder.KEY_DATA, orderEntity)
                        .startParentActivity(getBaseActivity(), CheckOrderInfoFragment.class, true);
            });
        }
    }

    private void initFoodView() {
        View footView = LayoutInflater.from(getContext()).inflate(R.layout.item_order_details_foot_layout, null);
        XRecyclerView footList = (XRecyclerView) footView.findViewById(R.id.list);
        footList.setLayoutManager(new LinearLayoutManager(getContext()));
        OrderFootAdapter footAdapter = new OrderFootAdapter(viewModel.getOderInfoTitle());
        footAdapter.setNewData(viewModel.getOrderInfo());
        footList.setAdapter(footAdapter);

        TextView number = (TextView) footView.findViewById(R.id.number);
        TextView price = (TextView) footView.findViewById(R.id.price);

        number.setText(getContext().getString(R.string.text_products_total_money, viewModel.getTotalCount(orderEntity.products) + ""));
        price.setText(PriceUtil.formatRMB(orderEntity.amount));

        adapter.addFooterView(footView);
    }

    private void initHeadView() {
        View head = View.inflate(getContext(), R.layout.item_order_details_head_layout, null);
        BaseViewHolder holder = new BaseViewHolder(head);
        holder.setText(R.id.status, status);
        holder.setText(R.id.address, UserModel.getInstance().getNickName());
        holder.setText(R.id.name, orderEntity.consigneeName);
        holder.setText(R.id.phone, orderEntity.mobile);
        holder.setText(R.id.address_1, orderEntity.address);
        adapter.addHeaderView(head);
    }


}

package com.olive.ui.order;

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
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.adapter.CheckOrderAdapter;
import com.olive.ui.adapter.BaseLineTextListAdapter;
import com.olive.ui.adapter.OrderFootAdapter;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/31.
 */

public class OrderDetailsFragment extends BaseFragment {
    private XRecyclerView recyclerView;
    private CheckOrderAdapter adapter;
    private String type;
    private List<Object> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_details_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_TYPE);
        setTitle(getString(R.string.title_order_detail));
        initData();
        initView();
    }


    private void initData() {
        if(getString(R.string.text_waiting_pay).equals(type)){

        }else if(getString(R.string.text_wait_send).equals(type)){
            //待发货
        }else if(getString(R.string.text_wait_receive).equals(type)){
            //待收货
        }else if(getString(R.string.text_order_complete).equals(type)){
            //已完成
        }else if(getString(R.string.text_order_cancel).equals(type)){

        }
        data = Lists.newArrayList("","","","");
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CheckOrderAdapter();
        adapter.setNewData(data);
        recyclerView.setAdapter(adapter);

        initButton();
        initHeadView();
        initFoodView();

    }

    private void initButton() {
        if(getString(R.string.text_waiting_pay).equals(type)){
            //待支付
            findViewById(R.id.btn_sure).setOnClickListener(v -> {

            });

            findViewById(R.id.btn_cancel).setOnClickListener(v -> {

            });

        }else if(getString(R.string.text_wait_send).equals(type)){
            //待发货
        }else if(getString(R.string.text_wait_receive).equals(type)){
            //待收货
        }else if(getString(R.string.text_order_complete).equals(type)){
            //已完成
        }else if(getString(R.string.text_order_cancel).equals(type)){
            findViewById(R.id.ll_btn).setVisibility(View.GONE);
            TextView btnOk = findViewById(R.id.btn_sure);
            btnOk.setText(getString(R.string.text_buy_again));
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setOnClickListener(v -> {

            });
        }
    }

    private void initFoodView() {
        View footView = LayoutInflater.from(getContext()).inflate(R.layout.item_order_details_foot_layout, null);
        XRecyclerView footList  = (XRecyclerView) footView.findViewById(R.id.list);
        footList.setLayoutManager(new LinearLayoutManager(getContext()));
        OrderFootAdapter footAdapter = new OrderFootAdapter(getContext().getResources().getStringArray(R.array.array_order_details));
        footAdapter.setNewData(Lists.newArrayList("","","","","","",""));
        footList.setAdapter(footAdapter);

        TextView number = (TextView) footView.findViewById(R.id.number);
        TextView price = (TextView) footView.findViewById(R.id.price);

        number.setText(getContext().getString(R.string.text_products_total_money,1+""));
        price.setText(PriceUtil.formatRMB(123123));

        adapter.addFooterView(footView);
    }

    private void initHeadView() {
        View head = View.inflate(getContext(), R.layout.item_order_details_head_layout, null);
        BaseViewHolder holder = new BaseViewHolder(head);
        holder.setText(R.id.status, type);
        holder.setText(R.id.address, "成都武侯区“生活馆");
        holder.setText(R.id.name, "刘德华");
        holder.setText(R.id.phone, "1231231231");
        holder.setText(R.id.address_1,"成都市天府大道北段环球中心 E1-1-1513");
        adapter.addHeaderView(head);
    }
}

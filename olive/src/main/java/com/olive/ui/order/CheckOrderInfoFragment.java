package com.olive.ui.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.adapter.CheckOrderAdapter;
import com.olive.ui.main.my.address.AddressManageFragment;

/**
 * Created by TingYu Zhu on 2017/7/26.
 */

public class CheckOrderInfoFragment extends BaseFragment {

    private XRecyclerView recyclerView;
    private CheckOrderAdapter adapter;
    private TextView price;
    private TextView number;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_with_button_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.text_order_sure));
        initView(view);
    }

    private void initView(View view) {
        recyclerView = findViewById(view, R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CheckOrderAdapter();
        adapter.setNewData(Lists.newArrayList("","","","","",""));
        recyclerView.setAdapter(adapter);

        findViewById(view, R.id.btn_sure).setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), PayOrderFragment.class, true);
        });

        initHeadView();
        initFoodView();
    }

    private void initFoodView() {
        View footer = View.inflate(getContext(), R.layout.item_check_order_footer_layout,null);
        BaseViewHolder holder = new BaseViewHolder(footer);
        holder.setText(R.id.text_price,"￥652.00");
        holder.setText(R.id.text_product_number,"共3件商品，合计：");
        adapter.addFooterView(footer);
    }

    private void initHeadView() {
        View head = View.inflate(getContext(), R.layout.item_check_order_head_layout, null);
        BaseViewHolder holder = new BaseViewHolder(head);
        holder.setText(R.id.tv_shop_name,"成都市武侯区生活馆(门店)");
        holder.setText(R.id.tv_consignee_name,"收货人：胡萝卜");
        holder.setText(R.id.tv_consignee_tel,"手机号：15899768766");
        holder.setText(R.id.tv_address,"地址：成都市环球中心E1-1801");
        head.setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), AddressManageFragment.class, true);
        });
        adapter.addHeaderView(head);
    }
}

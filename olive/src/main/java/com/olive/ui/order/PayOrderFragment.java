package com.olive.ui.order;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.adapter.PayOrderAdapter;

/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public class PayOrderFragment extends BaseFragment {

    private XRecyclerView recyclerView;
    private PayOrderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_with_button_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PayOrderAdapter(recyclerView.getRecyclerView());
        adapter.setNewData(Lists.newArrayList("","","",""));
        recyclerView.setAdapter(adapter);

        initHeadView();
    }

    private void initHeadView() {
        View head = View.inflate(getContext(), R.layout.item_pay_order_head_layout, null);
        BaseViewHolder holder = new BaseViewHolder(head);
        holder.setText(R.id.order_number,"订单编号：2323 323 3");
        holder.setText(R.id.order_price,"￥231.123");
        holder.setText(R.id.need_pay,"￥231.123");
        EditText editText = holder.findViewById(R.id.input_account_vacancies);
        editText.clearFocus();
        adapter.addHeaderView(head);
    }
}

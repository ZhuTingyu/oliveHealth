package com.olive.ui.main.my.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.adapter.PayOrderAdapter;

/**
 * Created by TingYu Zhu on 2017/7/31.
 */

public class RefundMoneyFragment extends BaseFragment {

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
        setTitle(getString(R.string.title_pay_order));
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PayOrderAdapter(recyclerView.getRecyclerView());
        //adapter.setNewData(Lists.newArrayList("","","",""));
        recyclerView.setAdapter(adapter);

        initHeadView();
    }

    private void initHeadView() {
        View head = View.inflate(getContext(), R.layout.item_pay_order_head_layout, null);
        BaseViewHolder holder = new BaseViewHolder(head);
        holder.findViewById(R.id.text1).setVisibility(View.GONE);
        holder.findViewById(R.id.order_number).setVisibility(View.GONE);
        holder.findViewById(R.id.divider1).setVisibility(View.GONE);

        TextView text2 = holder.findViewById(R.id.text2);
        text2.setText(getString(R.string.text_need_refund_money));

        holder.setText(R.id.price,"￥231.123");
        holder.setText(R.id.need_pay,"￥231.123");
        EditText editText = holder.findViewById(R.id.input_account_vacancies);
        editText.clearFocus();
        adapter.addHeaderView(head);

        TextView btn = findViewById(R.id.btn_sure);
        btn.setText(getString(R.string.text_pay));
        btn.setOnClickListener(v -> {

        });
    }
}

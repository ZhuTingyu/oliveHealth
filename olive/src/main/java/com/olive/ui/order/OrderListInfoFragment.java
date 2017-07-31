package com.olive.ui.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.adapter.OrderInfoListAdapter;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

@SuppressLint("ValidFragment")
public class OrderListInfoFragment extends BaseFragment {
    String type;
    private XRecyclerView recyclerView;
    private OrderInfoListAdapter adapter;
    private List<Object> data;

    public OrderListInfoFragment(String type){
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        if(getString(R.string.text_order_all).equals(type)){
            //全部
        }else if(getString(R.string.text_waiting_pay).equals(type)){
            //待支付
        }else if(getString(R.string.text_wait_send).equals(type)){
            //待发货
        }else if(getString(R.string.text_wait_receive).equals(type)){
            //待收货
        }else if(getString(R.string.text_order_complete).equals(type)){
            //已完成
        }else if(getString(R.string.text_order_cancel).equals(type)){
            //已经取消
        }
        data = Lists.newArrayList("","","","","");
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderInfoListAdapter(getContext(),type);
        adapter.setNewData(data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            //TODO data里面的type
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_TYPE, "")
                    .startParentActivity(getActivity(), OrderDetailsFragment.class, true);
        });
    }
}

package com.olive.ui.order;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.event.OrderListUpdateEvent;
import com.olive.ui.BaseErrorFragment;
import com.olive.ui.adapter.OrderInfoListAdapter;
import com.olive.ui.order.viewModel.OrderListViewModel;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class BaseOrderListInfoFragment extends BaseErrorFragment {


    private XRecyclerView recyclerView;
    private OrderInfoListAdapter adapter;

    private OrderListViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new OrderListViewModel(this);
        initViewModel(viewModel);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderInfoListAdapter(this);
        recyclerView.setAdapter(adapter);

        recyclerView.setRefreshListener(() -> {
            viewModel.cleanPage();
            viewModel.getOrderList(orderEntities -> {
                adapter.setNewData(orderEntities);
                recyclerView.setRefreshing(false);
            });
        });

        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_VALUE, adapter.getItem(i).orderNo)
                    .startParentActivity(getActivity(), OrderDetailsFragment.class, true);
        });

        viewModel.setRecyclerView(recyclerView);

        adapter.setOnLoadMoreListener(() -> {
            viewModel.setLoadMore(o -> {

            });
        }, recyclerView.getRecyclerView());

       initData();

    }

    public void onEvent(OrderListUpdateEvent event){
        if(event.typeCode == viewModel.typeCode){
            viewModel.cleanPage();
            initData();
        }
    }

    private void initData(){
        viewModel.getOrderList(orderEntities -> {
            adapter.setNewData(orderEntities);
        });
    }

}

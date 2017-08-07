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
import com.olive.R;
import com.olive.ui.adapter.OrderInfoListAdapter;
import com.olive.ui.order.viewModel.OrderListViewModel;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

@SuppressLint("ValidFragment")
public class OrderListInfoFragment extends BaseFragment {



    private XRecyclerView recyclerView;
    private OrderInfoListAdapter adapter;
    private List<Object> data;

    private OrderListViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new OrderListViewModel(this);
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



    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderInfoListAdapter(viewModel.getTypeName());
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

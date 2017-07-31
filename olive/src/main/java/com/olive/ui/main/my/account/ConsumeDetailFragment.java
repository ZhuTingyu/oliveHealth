package com.olive.ui.main.my.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.adapter.ConsumeDetailAdapter;

/**
 * Created by TingYu Zhu on 2017/7/30.
 */

public class ConsumeDetailFragment extends BaseFragment {

    public static final String TYPE_CONSUME = "con";
    public static final String TYPE_REFUND = "ref";


    private XRecyclerView recyclerView;
    private ConsumeDetailAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String type = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_TYPE);
        if(type != null && TYPE_CONSUME.equals(type)){
            setTitle(getString(R.string.title_consume_detail));
        }else {
            setTitle(getString(R.string.title_refund_money_detail));
        }
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ConsumeDetailAdapter();
        adapter.setNewData(Lists.newArrayList("","","",""));
        recyclerView.setAdapter(adapter);
    }
}

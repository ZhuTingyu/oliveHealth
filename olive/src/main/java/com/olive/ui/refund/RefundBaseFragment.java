package com.olive.ui.refund;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.adapter.RefundAdapter;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

@SuppressLint("ValidFragment")
public class RefundBaseFragment extends BaseFragment {

    private String type;

    private XRecyclerView recyclerView;
    private RefundAdapter adapter;

    public RefundBaseFragment(String type){
        this.type = type;
    }


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
        adapter = new RefundAdapter(getActivity(), type);
        adapter.setNewData(Lists.newArrayList("","","",""));
        recyclerView.setAdapter(adapter);
        TextView btn = findViewById(R.id.btn_sure);

        if(getString(R.string.text_refund_apply).equals(type)){
            btn.setOnClickListener(v -> {
                IntentBuilder.Builder().startParentActivity(getActivity(), ApplyRefundFragment.class, true);
            });
        }else {
            btn.setVisibility(View.GONE);
        }
    }
}

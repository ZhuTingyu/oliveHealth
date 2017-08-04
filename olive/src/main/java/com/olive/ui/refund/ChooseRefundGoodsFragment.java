package com.olive.ui.refund;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.base.BaseFragment;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.adapter.CartAdapter;

/**
 * Created by TingYu Zhu on 2017/7/30.
 */

public class ChooseRefundGoodsFragment extends BaseFragment {
    private XRecyclerView recyclerView;
    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_with_button_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.title_apply_refund));
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter();
        //adapter.setNewData(Lists.newArrayList("","","",""));
        recyclerView.setAdapter(adapter);


        findViewById(R.id.btn_sure).setOnClickListener(v -> {
            //String o = adapter.getItem(1);
            Intent intent = new Intent();
            //intent.putExtra("info",o);
            getActivity().setIntent(intent);
            getActivity().finish();
        });

    }
}

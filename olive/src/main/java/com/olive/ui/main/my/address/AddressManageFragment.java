package com.olive.ui.main.my.address;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.DialogUtil;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.adapter.AddressManageAdapter;

/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public class AddressManageFragment extends BaseFragment {

    private XRecyclerView recyclerView;
    private AddressManageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_with_button_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar.setTitle(getString(R.string.title_manager_address));
        initView(view);
    }

    private void initView(View view) {
        recyclerView = findViewById(view, R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AddressManageAdapter(this);
        adapter.setNewData(Lists.newArrayList("","","","",""));
        recyclerView.setAdapter(adapter);

        TextView btn = findViewById(view, R.id.btn_sure);
        btn.setText(getString(R.string.text_add_new_address));
    }

}

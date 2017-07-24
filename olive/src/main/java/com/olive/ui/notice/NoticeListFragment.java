package com.olive.ui.notice;

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
import com.olive.ui.adapter.NoticeListAdapter;

/**
 * Created by TingYu Zhu on 2017/7/24.
 */

public class NoticeListFragment extends BaseFragment {

    private XRecyclerView recyclerView;
    private NoticeListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.text_notice_list));
        recyclerView = findViewById(view, R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NoticeListAdapter();
        adapter.setNewData(Lists.newArrayList("","","",""));
        recyclerView.setAdapter(adapter);
    }
}

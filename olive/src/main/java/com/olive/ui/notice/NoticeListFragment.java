package com.olive.ui.notice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.base.BaseFragment;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.ui.adapter.NoticeListAdapter;

/**
 * Created by TingYu Zhu on 2017/7/24.
 */

public class NoticeListFragment extends BaseFragment {

    private XRecyclerView recyclerView;
    private NoticeListAdapter adapter;
    private NoticeViewModel viewModel;
    private int page = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel= new NoticeViewModel(context);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.text_notice_list));
        initView();
        initData();
    }

    private void initData() {
        viewModel.setPage(page);
        viewModel.getNoticeList(noticeEntities -> {
            setProgressVisible(false);
            if(noticeEntities.isEmpty()){
                error(getString(R.string.message_no_more));
                adapter.loadMoreEnd();
            }else {
                adapter.addData(noticeEntities);
                adapter.loadMoreComplete();
            }
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NoticeListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(()-> {
            page++;
            initData();
        },recyclerView.getRecyclerView());
    }
}

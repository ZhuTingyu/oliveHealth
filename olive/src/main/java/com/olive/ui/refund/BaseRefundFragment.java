package com.olive.ui.refund;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.BaseErrorFragment;
import com.olive.ui.adapter.RefundAdapter;
import com.olive.ui.refund.viewModel.RefundListViewModel;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class BaseRefundFragment extends BaseErrorFragment {

    public static final String KEY_TYPE = "type";

    private String type;

    private XRecyclerView recyclerView;
    private RefundAdapter adapter;

    private RefundListViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new RefundListViewModel(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_with_button_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getString(KEY_TYPE);
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RefundAdapter(getActivity(), type);
        recyclerView.setAdapter(adapter);
        TextView btn = findViewById(R.id.btn_sure);
        btn.setText(getString(R.string.title_apply_refund));

        if(getString(R.string.text_refund_apply).equals(type)){

            viewModel.getRefundApplyList(orderEntities -> {
                adapter.setNewData(orderEntities);
            });

            btn.setOnClickListener(v -> {
                IntentBuilder.Builder().startParentActivity(getActivity(), ApplyRefundFragment.class, true);
            });

            adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_VALUE, adapter.getItem(i).orderNo)
                        .startParentActivity(getActivity(), LookApplyDetailFragment.class, true);

            });

        }else {

            viewModel.getRefundList(orderEntities -> {
                adapter.setNewData(orderEntities);
            });

            btn.setVisibility(View.GONE);
        }
    }
}

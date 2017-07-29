package com.olive.ui.refund;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.olive.R;

/**
 * Created by TingYu Zhu on 2017/7/29.
 */

public class LookRefundCheckResult extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_look_refund_result_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.title_check_result));
        initView(view);
    }

    private void initView(View view) {
        BaseViewHolder holder = new BaseViewHolder(view);
        holder.setText(R.id.result,"审核成功");
        holder.setText(R.id.remark,"审核成功审核成功审核成功审核成功审核成功审核成功审核成功审核成功审核成功审核成功审核成功审核成功审核成功");
        holder.setText(R.id.remark2,"审核成功审核成功审核成功审核成功审核成功审核成功审核成功审核成功审核成功审核成功审核成功审核成功审核成功");
        holder.findViewById(R.id.btn_ok).setOnClickListener(v -> {

        });
    }
}

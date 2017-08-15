package com.olive.ui.refund;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.olive.R;
import com.olive.ui.refund.viewModel.LookApplyRefundDetailViewModel;

/**
 * Created by TingYu Zhu on 2017/7/29.
 */

public class LookRefundCheckResultFragment extends BaseFragment {

    LookApplyRefundDetailViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new LookApplyRefundDetailViewModel(context);
    }

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
        viewModel.getApplyRefundDetail(orderEntity -> {
            String status = "";
            AppCompatImageView imageView = findViewById(R.id.icon_img);
            if(orderEntity.status == LookApplyRefundDetailViewModel.STATUS_WAIT_CHECK){
                status = getContext().getString(R.string.text_refund_wait_check);
            }else if(orderEntity.status == LookApplyRefundDetailViewModel.STATUS_PASS_CHECK){
                status = getContext().getString(R.string.text_refund_pass_check);
            }else if(orderEntity.status == LookApplyRefundDetailViewModel.STATUS_NOT_PASS_CHECK){
                status = getContext().getString(R.string.text_refund_not_pass_check);
                imageView.setImageResource(R.drawable.vector_pay_failed);
            }
            holder.setText(R.id.result,status);
            holder.setText(R.id.remark,orderEntity.description);
            holder.setText(R.id.remark2,"审核成功");
            holder.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                getActivity().finish();
            });
        });

    }
}

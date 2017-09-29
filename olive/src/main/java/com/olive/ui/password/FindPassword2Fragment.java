package com.olive.ui.password;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.util.IntentBuilder;
import com.biz.util.RxUtil;
import com.biz.util.ToastUtils;
import com.olive.R;
import com.olive.ui.password.viewmodel.BasePasswordViewModel;
import com.olive.ui.password.viewmodel.FindPasswordViewModel;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class FindPassword2Fragment extends BasePasswordFragment {

    @Override
    public void onAttach(Context context) {
        viewModel = new FindPasswordViewModel(context);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_password_2_layout, container, false);
    }

    @Override
    protected void initView() {
        viewModel.setMobile(getBaseActivity().getIntent().getStringExtra(IntentBuilder.KEY_VALUE));
        viewModel.setAuthCode(getBaseActivity().getIntent().getStringExtra(IntentBuilder.KEY_DATA));
        setTitle(getString(R.string.title_find_password));
        viewModel.setType(BasePasswordViewModel.TYPE_CODE_FIND_PASSWORD);
        initPasswordView();
        tvOk = findViewById(R.id.btn_ok);
        tvOk.setOnClickListener(v -> {
            viewModel.resetPassword(s1 -> {
                ToastUtils.showLong(getActivity(), getString(R.string.message_modify_success));
                getActivity().finish();
            });
        });

        bindUi(viewModel.getIsValid(), RxUtil.enabled(tvOk));

    }

    @Override
    protected void sendCodeSuccess() {

    }
}

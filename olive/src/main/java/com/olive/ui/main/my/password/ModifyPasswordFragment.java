package com.olive.ui.main.my.password;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.util.RxUtil;
import com.biz.util.ToastUtils;
import com.biz.widget.CustomCountDownTimer;
import com.olive.R;
import com.olive.ui.main.my.password.viewmodel.ChangPasswordViewModel;
import com.olive.ui.main.my.password.viewmodel.BasePasswordViewModel;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class ModifyPasswordFragment extends BasePasswordFragment {


    @Override
    public void onAttach(Context context) {
        viewModel = new ChangPasswordViewModel(context);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modify_password_layout, container, false);
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getString(R.string.title_modify_password));
        initPasswordView();
        code = findViewById(R.id.code);
        btnCode = findViewById(R.id.btn_code);
        bindData(RxUtil.textChanges(code), viewModel.setAuthCode());
        countDownTimer = new CustomCountDownTimer(getActivity(),
                btnCode, R.string.text_send_code, R.string.btn_resend_count, 60000, 1000);

        btnCode.setOnClickListener(v -> {
            viewModel.sendCode(s -> {
                countDownTimer.start();
                ToastUtils.showLong(getActivity(), getString(R.string.message_send_code_success));
                sendCodeSuccess();
            });
        });
        bindUi(RxUtil.textChanges(newPassword), viewModel.setNewPassword());
        bindUi(viewModel.getIsValid(), RxUtil.enabled(tvOk));

        viewModel.setType(BasePasswordViewModel.TYPE_CODE_MODIFY_PASSWORD);

        tvOk.setOnClickListener(v -> {
            viewModel.changPassword(s2 -> {
                ToastUtils.showLong(getActivity(), getString(R.string.message_modify_success));
                getActivity().finish();
            });
        });
    }

    @Override
    protected void sendCodeSuccess() {

    }
}

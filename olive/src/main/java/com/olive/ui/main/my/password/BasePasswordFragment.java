package com.olive.ui.main.my.password;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.RxUtil;
import com.biz.util.ToastUtils;
import com.biz.widget.CustomCountDownTimer;
import com.olive.R;
import com.olive.model.UserModel;
import com.olive.ui.BaseErrorFragment;
import com.olive.ui.main.my.password.viewmodel.BasePasswordViewModel;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public abstract class BasePasswordFragment extends BaseErrorFragment {

    protected EditText mobile;
    protected EditText code;
    protected TextView btnCode;
    protected TextView tvOk;
    protected CustomCountDownTimer countDownTimer;
    protected EditText password;
    protected EditText newPassword;
    protected BasePasswordViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initViewModel(viewModel);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    protected void initView() {
        mobile = findViewById(R.id.mobile);
        tvOk = findViewById(R.id.btn_ok);

        bindData(RxUtil.textChanges(mobile), viewModel.setMobile());
        bindUi(viewModel.getIsValid(), RxUtil.enabled(tvOk));

    }

    protected void initPasswordView() {
        password = findViewById(R.id.password);
        newPassword = findViewById(R.id.new_password);

        bindData(RxUtil.textChanges(newPassword), viewModel.setNewPassword());
        bindData(RxUtil.textChanges(password), viewModel.setPassword());
    }

    protected void initBtnCode() {
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
    }

    protected abstract void sendCodeSuccess();

}
package com.olive.ui.main.my.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.biz.util.RxUtil;
import com.biz.util.ToastUtils;
import com.biz.widget.CustomCountDownTimer;
import com.olive.R;
import com.olive.model.UserModel;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class ResetPayPasswordFragment extends BasePasswordFragment {

    TextView mTvSendCodeStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reset_pay_password_layout, container, false);
    }

    @Override
    protected void initView() {

        mTvSendCodeStatus = findViewById(R.id.send_code_status);

        viewModel.setMobile(UserModel.getInstance().getMobile());

        code = findViewById(R.id.code);
        btnCode = findViewById(R.id.btn_code);
        bindData(RxUtil.textChanges(code), viewModel.setAuthCode());
        countDownTimer = new CustomCountDownTimer(getActivity(),
                btnCode, R.string.text_send_code, R.string.btn_resend_count, 60000, 1000);

        btnCode.setOnClickListener(v -> {
            if (viewModel.isMobileValid()) {
                viewModel.setType(PasswordViewModel.TYPE_CODE_RESET_PAY_PASSWORD);
                viewModel.sendCode(s -> {
                    countDownTimer.start();
                    ToastUtils.showLong(getActivity(), getString(R.string.message_send_code_success));
                    sendCodeSuccess();
                });
            } else {
                error(getString(R.string.message_input_valid_mobile));
            }
        });

        initPasswordView();

        tvOk = findViewById(R.id.btn_ok);
        tvOk.setOnClickListener(v -> {
            viewModel.isPasswordValid(s -> {
                if (PasswordViewModel.INFO_VALID.equals(s)) {
                    viewModel.resetPayPassword(s1 -> {
                        ToastUtils.showLong(getContext(), getString(R.string.message_modify_success));
                        getActivity().finish();
                    });
                } else {
                    error(s);
                }
            });
        });
    }

    @Override
    protected void sendCodeSuccess() {
        mTvSendCodeStatus.setText(getString(R.string.message_send_code_status_with_mobile,
                UserModel.getInstance().getMobile()));
        mTvSendCodeStatus.setVisibility(View.VISIBLE);
    }
}

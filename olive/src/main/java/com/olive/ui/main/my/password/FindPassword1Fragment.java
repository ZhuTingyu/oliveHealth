package com.olive.ui.main.my.password;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.RxUtil;
import com.biz.util.ToastUtils;
import com.biz.widget.CustomCountDownTimer;
import com.olive.R;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class FindPassword1Fragment extends BasePasswordFragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_password_1_layout, container, false);
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getString(R.string.title_find_password));
        code = findViewById(R.id.code);
        btnCode = findViewById(R.id.btn_code);
        bindData(RxUtil.textChanges(code), viewModel.setAuthCode());
        countDownTimer = new CustomCountDownTimer(getActivity(),
                btnCode, R.string.text_send_code, R.string.btn_resend_count, 60000, 1000);

        btnCode.setOnClickListener(v -> {
            if(viewModel.isMobileValid()){
                viewModel.sendCode(s -> {
                    countDownTimer.start();
                    ToastUtils.showLong(getActivity(), getString(R.string.message_send_code_success));
                    sendCodeSuccess();
                });
            }else {
                error(getString(R.string.message_input_valid_mobile));
            }
        });
        viewModel.setType(PasswordViewModel.TYPE_CODE_FIND_PASSWORD);
        tvOk.setOnClickListener(v -> {
            if(viewModel.isMobileValid() && viewModel.isCodeValid()){
                viewModel.validateCode(s -> {
                    IntentBuilder.Builder()
                            .putExtra(IntentBuilder.KEY_DATA, viewModel.authCode)
                            .putExtra(IntentBuilder.KEY_VALUE, viewModel.mobile)
                            .startParentActivity(getActivity(), FindPassword2Fragment.class, true);
                    getActivity().finish();
                });
            }else {
                error(getString(R.string.message_perfect_info));
            }
        });
    }

    @Override
    protected void sendCodeSuccess() {

    }
}

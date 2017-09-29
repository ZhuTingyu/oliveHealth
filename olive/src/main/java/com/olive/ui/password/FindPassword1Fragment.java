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
import com.biz.widget.CustomCountDownTimer;
import com.olive.R;
import com.olive.ui.password.viewmodel.BasePasswordViewModel;
import com.olive.ui.password.viewmodel.FindPasswordViewModel;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class FindPassword1Fragment extends BasePasswordFragment {


    @Override
    public void onAttach(Context context) {
        viewModel = new FindPasswordViewModel(context);
        super.onAttach(context);
    }

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
        countDownTimer = new CustomCountDownTimer(getActivity(),
                btnCode, R.string.text_send_code, R.string.btn_resend_count, 60000, 1000);

        btnCode.setOnClickListener(v -> {
            viewModel.setType(BasePasswordViewModel.TYPE_CODE_FIND_PASSWORD);
            viewModel.sendCode(s -> {
                countDownTimer.start();
                ToastUtils.showLong(getActivity(), getString(R.string.message_send_code_success));
                sendCodeSuccess();
            });
        });

        bindData(RxUtil.textChanges(code), viewModel.setAuthCode());

        tvOk.setOnClickListener(v -> {
            viewModel.validateCode(s -> {
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_DATA, viewModel.authCode)
                        .putExtra(IntentBuilder.KEY_VALUE, viewModel.mobile)
                        .startParentActivity(getActivity(), FindPassword2Fragment.class, true);
                getActivity().finish();
            });
        });


    }

    @Override
    protected void sendCodeSuccess() {

    }
}

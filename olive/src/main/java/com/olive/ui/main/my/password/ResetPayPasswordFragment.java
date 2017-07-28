package com.olive.ui.main.my.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.biz.util.RxUtil;
import com.biz.widget.CustomCountDownTimer;
import com.olive.R;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class ResetPayPasswordFragment extends BasePasswordFragment {

    protected EditText code;
    protected TextView tvCode;
    protected TextView tvOk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reset_pay_password_layout, container, false);
    }

    @Override
    protected void initView() {
        initPasswordView();
        tvCode = findViewById(R.id.btn_code);
        tvOk = findViewById(R.id.btn_ok);
        countDownTimer = new CustomCountDownTimer(getActivity(),
                tvCode, R.string.text_send_code, R.string.btn_resend_count, 60000, 1000);

        bindData(RxUtil.textChanges(code), viewModel.setCode());

        tvOk.setOnClickListener(v -> {

        });
    }
}

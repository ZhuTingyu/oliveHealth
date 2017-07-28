package com.olive.ui.main.my.password;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.RxUtil;
import com.biz.widget.CustomCountDownTimer;
import com.olive.R;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class BasePasswordFragment extends BaseFragment {

    protected EditText userName;
    protected EditText code;
    protected TextView tvCode;
    protected TextView tvOk;
    protected CustomCountDownTimer countDownTimer;
    protected EditText password;
    protected EditText newPassword;
    protected PasswordViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new PasswordViewModel(context);
        initViewModel(viewModel);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    protected void initView() {
        userName = findViewById(R.id.username);
        tvCode = findViewById(R.id.btn_code);
        tvOk = findViewById(R.id.btn_ok);
        countDownTimer = new CustomCountDownTimer(getActivity(),
                tvCode, R.string.text_send_code, R.string.btn_resend_count, 60000, 1000);



        bindData(RxUtil.textChanges(userName), viewModel.setUserName());
        bindData(RxUtil.textChanges(code), viewModel.setCode());

    }

    protected void initPasswordView(){
        password = findViewById(R.id.password);
        newPassword = findViewById(R.id.new_password);

        bindData(RxUtil.textChanges(password), viewModel.setPassword());
        bindData(RxUtil.textChanges(newPassword), viewModel.setNewPassword());
    }
}

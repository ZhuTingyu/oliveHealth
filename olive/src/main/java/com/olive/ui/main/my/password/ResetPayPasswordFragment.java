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
import com.olive.model.UserModel;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class ResetPayPasswordFragment extends BasePasswordFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reset_pay_password_layout, container, false);
    }

    @Override
    protected void initView() {


        viewModel.setMobile(UserModel.getInstance().getMobile());

        initBtnCode();
        viewModel.setType(PasswordViewModel.TYPE_CODE_RESET_PAY_PASSWORD);

        initPasswordView();

        tvOk = findViewById(R.id.btn_ok);
        tvOk.setOnClickListener(v -> {
            viewModel.isOldAndNewPasswordValid(s -> {
                if(PasswordViewModel.INFO_VALID.equals(s)){
                    viewModel.changPassword(s1 -> {
                        viewModel.resetPassword(s2 -> {

                        });
                    });
                }else {
                    error(s);
                }
            });
        });
    }
}

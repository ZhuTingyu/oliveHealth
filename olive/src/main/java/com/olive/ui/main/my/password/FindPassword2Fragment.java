package com.olive.ui.main.my.password;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.RxUtil;
import com.olive.R;
import com.olive.model.UserModel;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class FindPassword2Fragment extends BasePasswordFragment {


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
        viewModel.setType(PasswordViewModel.TYPE_CODE_FIND_PASSWORD);
        initPasswordView();
        tvOk.setOnClickListener(v -> {
            viewModel.isPasswordValid(s -> {
                viewModel.setPassword(UserModel.getInstance().getPassword());
                if(PasswordViewModel.INFO_VALID.equals(s)){
                    viewModel.changPassword(s1 -> {

                    });
                }else {
                    error(s);
                }
            });
        });
    }

    @Override
    protected void sendCodeSuccess() {

    }
}

package com.olive.ui.main.my.password;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
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
        initBtnCode();
        viewModel.setType(PasswordViewModel.TYPE_CODE_FIND_PASSWORD);
        tvOk.setOnClickListener(v -> {
            if(viewModel.isMobileValid() && viewModel.isCodeValid()){
                viewModel.validateCode(s -> {
                    IntentBuilder.Builder()
                            .putExtra(IntentBuilder.KEY_DATA, viewModel.authCode)
                            .putExtra(IntentBuilder.KEY_VALUE, viewModel.mobile)
                            .startParentActivity(getActivity(), FindPassword2Fragment.class, true);
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

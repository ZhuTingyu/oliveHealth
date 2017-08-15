package com.olive.ui.main.my.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.util.RxUtil;
import com.biz.util.ToastUtils;
import com.olive.R;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class ModifyPasswordFragment extends BasePasswordFragment {
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
        initBtnCode();
        bindUi(RxUtil.textChanges(password), viewModel.setPassword());
        bindUi(RxUtil.textChanges(newPassword), viewModel.setNewPassword());

        viewModel.setType(PasswordViewModel.TYPE_CODE_MODIFY_PASSWORD);

        tvOk.setOnClickListener(v -> {

            viewModel.isInfoValid(s -> {
                if (PasswordViewModel.INFO_VALID.equals(s)) {
                    viewModel.isPasswordValid(s1 -> {
                        if (PasswordViewModel.INFO_VALID.equals(s1)){
                            viewModel.changPassword(s2 -> {
                                ToastUtils.showLong(getActivity(), getString(R.string.message_modify_success));
                                getActivity().finish();
                            });
                        }else {
                            error(s);
                        }
                    });
                } else {
                    error(s);
                }
            });
        });
    }

    @Override
    protected void sendCodeSuccess() {

    }
}

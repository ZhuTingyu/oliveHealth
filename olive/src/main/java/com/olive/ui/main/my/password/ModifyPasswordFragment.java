package com.olive.ui.main.my.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.util.RxUtil;
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
        tvOk.setOnClickListener(v -> {

        });
    }
}

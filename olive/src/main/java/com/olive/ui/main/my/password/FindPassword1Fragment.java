package com.olive.ui.main.my.password;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.base.BaseFragment;
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
        tvOk.setOnClickListener(v -> {

        });
    }
}

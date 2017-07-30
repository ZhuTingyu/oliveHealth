package com.olive.ui.main.my.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.PriceUtil;
import com.olive.R;

/**
 * Created by TingYu Zhu on 2017/7/30.
 */

public class MyAccoutFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_accout_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        TextView balance = findViewById(R.id.balance);
        TextView consume = findViewById(R.id.consume);
        TextView debt = findViewById(R.id.debt);

        balance.setText(PriceUtil.formatRMB(123123));
        consume.setText(PriceUtil.formatRMB(123123));
        debt.setText(PriceUtil.formatRMB(123123));

        findViewById(R.id.rl_consume).setOnClickListener(v -> {

        });

        findViewById(R.id.rl_debt).setOnClickListener(v -> {

        });
    }
}

package com.olive.ui.order;

import android.view.View;

import com.olive.R;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class PayDebtFragment extends PayFragment {

    @Override
    protected void initHeadView() {

        super.initHeadView();
        findViewById(R.id.text1).setVisibility(View.GONE);
        findViewById(R.id.order_number).setVisibility(View.GONE);
        findViewById(R.id.divider1).setVisibility(View.GONE);

    }
}

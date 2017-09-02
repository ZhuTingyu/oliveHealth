package com.olive.ui.order;

import android.view.View;

import com.olive.R;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class PayDebtFragment extends BasePayFragment {

    @Override
    protected void initHeadView() {

        super.initHeadView();
        findViewById(head, R.id.text1).setVisibility(View.GONE);
        findViewById(head, R.id.order_number).setVisibility(View.GONE);
        findViewById(head, R.id.divider1).setVisibility(View.GONE);

    }

}

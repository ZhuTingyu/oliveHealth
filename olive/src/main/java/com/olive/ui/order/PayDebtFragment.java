package com.olive.ui.order;

import android.view.View;
import android.widget.TextView;

import com.biz.util.PriceUtil;
import com.olive.R;
import com.olive.model.entity.OrderEntity;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class PayDebtFragment extends BasePayFragment {

    @Override
    protected void initView() {
        super.initView();
        setTitle(R.string.text_refund_money);
        btnOk.setText(R.string.text_pay);
    }

    @Override
    protected void initHeadView() {

        super.initHeadView();
        findViewById(head, R.id.text1).setVisibility(View.GONE);
        findViewById(head, R.id.order_number).setVisibility(View.GONE);
        findViewById(head, R.id.divider1).setVisibility(View.GONE);

        TextView text = findViewById(head, R.id.text2);
        text.setText(R.string.text_need_refund_price);

        tvVacancies.setText(getString(R.string.text_pay_by_account_vacancies, PriceUtil.formatRMB(viewModel.accountEntity.balance))+"");

        viewModel.getDebtDetails(orderEntities -> {
            OrderEntity orderEntity = orderEntities.get(0);
            viewModel.setOrderEntity(orderEntity);
            tvPayPrice.setText(PriceUtil.formatRMB(viewModel.orderEntity.amount));
            tvNeedPayPrice.setText(PriceUtil.formatRMB(viewModel.orderEntity.amount));
        });

    }

}

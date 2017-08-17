package com.olive.ui.order;

import com.biz.util.PriceUtil;
import com.olive.R;

/**
 * Created by TingYu Zhu on 2017/8/8.
 */

public class PayOrderFragment extends BasePayFragment {

    @Override
    protected void initHeadView() {
        super.initHeadView();
        tvOrderNumber.setText(getString(R.string.text_order_number, orderEntity.orderNo));
        tvPayPrice.setText(PriceUtil.formatRMB(orderEntity.amount));
        tvVacancies.setText(getString(R.string.text_pay_by_account_vacancies, PriceUtil.formatRMB(accountEntity.balance))+"");
        tvNeedPayPrice.setText(PriceUtil.formatRMB(orderEntity.amount));
    }

}

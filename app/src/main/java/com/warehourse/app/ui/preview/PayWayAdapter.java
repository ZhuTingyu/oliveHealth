package com.warehourse.app.ui.preview;


import com.biz.base.BaseViewHolder;
import com.biz.util.DrawableHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.PaymentTypeEntity;

import android.widget.TextView;

/**
 * Created by johnzheng on 3/17/16.
 */
public class PayWayAdapter extends BaseQuickAdapter<PaymentTypeEntity, BaseViewHolder> {


    public PayWayAdapter() {
        super(R.layout.item_pay_way_layout);
    }


    @Override
    protected void convert(BaseViewHolder holder, PaymentTypeEntity s) {
        TextView textView = holder.findViewById(R.id.title_left);
        if (s.isAlipay()) {
            textView.setCompoundDrawables(
                    DrawableHelper.getDrawableWithBounds(textView.getContext(), R.drawable.ic_alipay),
                    null, null, null);
        } else if (s.isWechaty()) {
            textView.setCompoundDrawables(
                    DrawableHelper.getDrawableWithBounds(textView.getContext(), R.drawable.ic_wechat_pay),
                    null, null, null);
        } else if (s.isDeliver()) {
            textView.setCompoundDrawables(
                    DrawableHelper.getDrawableWithBounds(textView.getContext(), R.drawable.ic_cash),
                    null, null, null);
        }
        textView.setText(s.getPayName());
    }


}

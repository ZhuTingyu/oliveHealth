package com.warehourse.app.ui.coupon;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import com.biz.util.PriceUtil;
import com.biz.util.TimeUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.warehourse.app.R;
import com.warehourse.app.model.entity.VoucherEntity;
import com.warehourse.app.model.entity.VoucherMainEntity;


class CouponAdapter extends BaseQuickAdapter<VoucherEntity, CouponViewHolder> {


    public CouponAdapter() {
        super(R.layout.item_coupon_layout);
    }



    @Override
    protected void convert(CouponViewHolder holder, VoucherEntity o) {
            onBindHolder(holder, o);
    }

    public void onBindHolder(CouponViewHolder holder, VoucherEntity voucherInfo) {
        holder.setCheckedLayout(false);
        holder.titleMoney.setText(PriceUtil.formatInteger(voucherInfo.faceValue));
        if (holder.itemView.getResources().getDisplayMetrics().density<3)
            holder.titleMoney.setTextSize(TypedValue.COMPLEX_UNIT_DIP, voucherInfo.faceValue>99?22:33);
        holder.titleLine1.setText(voucherInfo.name == null ? "" : voucherInfo.name);
        holder.titleLine2.setText(TimeUtil.formatValidityDate(voucherInfo.startTime, voucherInfo.expireTime));
        holder.titleLine4.setVisibility(View.VISIBLE);
        holder.titleLine4.setText(voucherInfo.categoryInfo == null ? "" : voucherInfo.categoryInfo);
        if (TextUtils.isEmpty(voucherInfo.freeMsg)) {
            holder.iconStatus.setVisibility(View.GONE);
        } else {
            holder.iconStatus.setVisibility(View.VISIBLE);
            holder.iconStatus.setText(voucherInfo.freeMsg);
        }
    }


}
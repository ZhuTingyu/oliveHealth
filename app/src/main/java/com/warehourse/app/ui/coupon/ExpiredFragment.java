package com.warehourse.app.ui.coupon;


import com.biz.util.Lists;
import com.warehourse.app.model.entity.VoucherEntity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


public class ExpiredFragment extends BaseCouponListFragment {


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setAdapter(adapter = new ExpiredAdapter());
        addAdapterList();
    }


    class ExpiredAdapter extends CouponAdapter {


        public void onBindHolder(CouponViewHolder holder, VoucherEntity o) {
            holder.setExpiredView(true);
            super.onBindHolder(holder, o);
        }

    }


}

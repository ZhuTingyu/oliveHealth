package com.warehourse.app.ui.coupon;

import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.warehourse.app.model.entity.VoucherEntity;
import com.warehourse.app.model.entity.VoucherMainEntity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;


public class NoUseFragment extends BaseCouponListFragment {


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setAdapter(adapter = new NoUseAdapter());
        addAdapterList();
    }

    class NoUseAdapter extends CouponAdapter {
        public void onBindHolder(CouponViewHolder holder, VoucherEntity o) {
            super.onBindHolder(holder, o);
            holder.setExpiredView(false);
        }

    }


}

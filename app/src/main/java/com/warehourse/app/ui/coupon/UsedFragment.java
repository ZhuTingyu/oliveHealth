package com.warehourse.app.ui.coupon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.biz.util.IntentBuilder;
import com.warehourse.app.model.entity.VoucherEntity;

import java.util.ArrayList;


public class UsedFragment extends BaseCouponListFragment {


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setAdapter(adapter = new UsedAdapter());
        addAdapterList();
    }

    class UsedAdapter extends CouponAdapter {



        public void onBindHolder(CouponViewHolder holder, VoucherEntity o) {

            holder.setExpiredView(true);
            super.onBindHolder(holder,o);
            holder.iconStatus.setVisibility(View.GONE);

        }

    }


}

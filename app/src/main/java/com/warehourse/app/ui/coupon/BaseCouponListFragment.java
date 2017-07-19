package com.warehourse.app.ui.coupon;


import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.VoucherEntity;
import com.warehourse.app.ui.base.EmptyViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class BaseCouponListFragment extends BaseFragment {

    XRecyclerView recyclerView;

    protected CouponAdapter adapter = null;


    private void emptyView(BaseQuickAdapter adapter) {
        EmptyViewHolder holder = EmptyViewHolder.createHolder(getContext())
                .setIcon(R.color.color_transparent)
                .setTitle(R.string.text_money_none);
        adapter.setEmptyView(holder.itemView);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_recyclerview, container, false);
    }

    protected void addAdapterList()
    {
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<VoucherEntity> voucherMainEntities = bundle.getParcelableArrayList(IntentBuilder.KEY_DATA);
            adapter.setNewData(voucherMainEntities);
            emptyView(adapter);
        }
    }
    public static void putListData(BaseFragment baseFragment,ArrayList<VoucherEntity> list)
    {
        Bundle bundle=new Bundle();
        bundle.putParcelableArrayList(IntentBuilder.KEY_DATA,list);
        baseFragment.setArguments(bundle);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView(view, R.id.list);
        LinearLayoutManager layoutManager =
                (LinearLayoutManager) recyclerView.getRecyclerView().getLayoutManager();

        layoutManager.setRecycleChildrenOnDetach(true);
        if (mPool != null) {
            recyclerView.getRecyclerView().setRecycledViewPool(mPool);
        }
    }
}

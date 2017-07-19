package com.warehourse.app.ui.order.list;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.widget.recyclerview.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.OrderApplyReturnEntity;
import com.warehourse.app.model.entity.OrderEntity;
import com.warehourse.app.ui.base.EmptyViewHolder;
import com.warehourse.app.ui.order.detail.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Title: BaseOrderListFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:15/05/2017  15:25
 *
 * @author johnzheng
 * @version 1.0
 */

public class BaseOrderListFragment extends BaseFragment {

    protected XRecyclerView mRecyclerView;
    protected OrderListAdapter mAdapter;
    protected OrderListViewModel mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void error(String error) {
        emptyView(mAdapter);
        mRecyclerView.setEnabled(true);
    }

    protected void emptyView(BaseQuickAdapter adapter) {
        EmptyViewHolder holder = EmptyViewHolder.createHolder(getContext())
                .setIcon(R.color.color_transparent)
                .setTitle(R.string.text_no_order);
        holder.setIcon(R.drawable.vector_no_order);
        adapter.setEmptyView(holder.itemView);
    }

    private void refresh() {
        mViewModel.refresh(list -> {
            mAdapter.setNewData(list);
            emptyView(mAdapter);
            mRecyclerView.setEnabled(true);
        }, mRecyclerView::setLoadMore);
    }

    protected void loadMore() {
        mViewModel.loadMore(mAdapter::addData, mRecyclerView::setLoadMore);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = getView(R.id.list);
        mAdapter = new OrderListAdapter();
        mAdapter.setOrderApplySaleListener(orderEntity -> {
            IntentBuilder.Builder().putExtra(IntentBuilder.KEY_ID, orderEntity.getOrderId())
                    .startParentActivity(this, OrderOfterSaleFragment.class, 101);
        });
        mAdapter.setItemOnClickListener(v->{
            try {
                OrderEntity orderEntity= (OrderEntity) v.getTag();
                mViewModel.clearPayStatus();
                IntentBuilder.Builder().putExtra(IntentBuilder.KEY_ID,orderEntity.getOrderId())
                        .startParentActivity(this, OrderDetailFragment.class,101);
            }catch (Exception e){}

        });
        mAdapter.setOrderCancelListener(orderEntity -> {
            setProgressVisible(true);
            mViewModel.setOperationOrderId(orderEntity.getOrderId());
            mViewModel.cancelOrder(b -> {
                setProgressVisible(false);
                refresh();
            });
        });
        mAdapter.setOrderPayListener(orderEntity -> {
            setProgressVisible(true);
            mViewModel.setOperationOrderId(orderEntity.getOrderId());
            mViewModel.rePay(getBaseActivity(), orderEntity);
        });
        mViewModel.setPayComplete(payCompleteEntity -> {
            setProgressVisible(false);
            if (payCompleteEntity.isComplete) {
                setProgressVisible(true);
                mRecyclerView.postDelayed(()->{
                    setProgressVisible(false);
                    IntentBuilder.Builder().putExtra(IntentBuilder.KEY_ID, payCompleteEntity.orderId)
                            .startParentActivity(getBaseActivity(), OrderPayStatusFragment.class);
                    finish();
                },800);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getContext().getString(R.string.dialog_title_notice));
                builder.setMessage(payCompleteEntity.message);
                builder.setPositiveButton(R.string.btn_confirm, (dialogInterface, i) -> {
                });
                builder.setOnDismissListener(dialogInterface -> {
                    dialogInterface.dismiss();
                    refresh();
                });
                builder.show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEnabled(false);
        refresh();

        mAdapter.setOnLoadMoreListener(this::loadMore, mRecyclerView.getRecyclerView());

        mRecyclerView.setRefreshListener(this::refresh);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            refresh();
        }
    }
}

package com.warehourse.app.ui.preview;

import com.biz.base.BaseFragment;
import com.biz.base.FragmentBackHelper;
import com.biz.util.DialogUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.OrderPreviewEntity;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.ui.coupon.ChooseCouponFragment;
import com.warehourse.app.ui.order.detail.OrderDetailFragment;
import com.warehourse.app.ui.order.detail.OrderPayStatusFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Title: OrderPreviewFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:19/05/2017  10:04
 *
 * @author johnzheng
 * @version 1.0
 */

public class OrderPreviewFragment extends BaseFragment implements FragmentBackHelper{
    public static final int REQUEST_COUPON=1002;
    private RecyclerView mRecyclerView;
    private AppCompatTextView mTextTotal;
    private TextView mBtn;
    private PreviewAdapter mAdapter;
    private AddressViewHolder mAddressViewHolder;
    private CouponViewHolder mCouponViewHolder;
    private InvoiceViewHolder mInvoiceViewHolder;
    private TotalViewHolder mTotalViewHolder;
    private OrderPreviewViewModel mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mViewModel = new OrderPreviewViewModel(context);
        initViewModel(mViewModel);
    }
    @Override
    public boolean onBackPressed() {
        if (true) {
            DialogUtil.createDialogViewWithCancel(getActivity(), R.string.dialog_title_notice,
                    R.string.dialog_msg_cancel_order,
                    (dialog, which) -> {
                        getActivity().finish();
                    }, R.string.btn_confirm);
            return true;
        }
        return false;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_order_preview_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setBackgroundDrawableResource(R.color.white);
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.title_fill_order);
        mRecyclerView = findViewById(android.R.id.list);
        mTextTotal = findViewById(R.id.text_total);
        mBtn = findViewById(R.id.btn);

        mAdapter = new PreviewAdapter();
        mAddressViewHolder = AddressViewHolder.createViewHolder(mRecyclerView);
        mAdapter.addHeaderView(mAddressViewHolder.itemView);

        mAdapter.addFooterView(View.inflate(getContext(), R.layout.view_line_10, null));
        mInvoiceViewHolder = InvoiceViewHolder.createViewHolder(mRecyclerView);
        mAdapter.addFooterView(mInvoiceViewHolder.itemView);
        mAdapter.addFooterView(View.inflate(getContext(), R.layout.view_line_10, null));

        mCouponViewHolder = CouponViewHolder.createViewHolder(mRecyclerView);
        mAdapter.addFooterView(mCouponViewHolder.itemView);
        mAdapter.addFooterView(View.inflate(getContext(), R.layout.view_line_10, null));
        mTotalViewHolder = TotalViewHolder.createViewHolder(mRecyclerView);
        mAdapter.addFooterView(mTotalViewHolder.itemView);

        mRecyclerView.setAdapter(mAdapter);
        addItemDecorationLine(mRecyclerView);

        mBtn.setOnClickListener(v -> {
            setProgressVisible(true);
            mViewModel.setInvoice(mInvoiceViewHolder.isChecked(),
                    mInvoiceViewHolder.getInvoiceTitle(),
                    mInvoiceViewHolder.getInvoiceNUmber());
            mViewModel.submitOrder(getBaseActivity());
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
                    IntentBuilder.Builder().putExtra(IntentBuilder.KEY_ID, payCompleteEntity.orderId)
                            .startParentActivity(getActivity(), OrderDetailFragment.class);
                    finish();
                });
                builder.show();
            }
        });

        setProgressVisible(true);
        mViewModel.requestPreview(this::bindData);

    }

    private void bindData(OrderPreviewEntity entity) {
        setProgressVisible(false);
        mAdapter.setNewData(entity.items);
        mAddressViewHolder.bindData(entity);
        mCouponViewHolder.bindData(entity);
        bindData(mViewModel.getVoucherCount(),count->{
            mCouponViewHolder.textCoupon.setText(mCouponViewHolder.itemView.getResources().getString(R.string.text_coupon_count_used, String.valueOf(count)));
        });
        mCouponViewHolder.itemView.setOnClickListener(v -> {
            IntentBuilder.Builder().putExtra(IntentBuilder.KEY_DATA,mViewModel.getProductList())
                    .putExtra(IntentBuilder.KEY_VALUE,mViewModel.getVoucherEntity())
                    .startParentActivity(this,ChooseCouponFragment.class,REQUEST_COUPON);
        });
        mTotalViewHolder.bindData(entity);
        mTotalViewHolder.setPayData(entity.paymentTypes, mViewModel.setPayType());
        mBtn.setEnabled(entity.valid);

        mTextTotal.setText(getString(R.string.text_total) + PriceUtil.formatRMB(entity.payAmount));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_COUPON&&resultCode== Activity.RESULT_OK){
            mViewModel.setVoucherData(data);
            setProgressVisible(true);
            mViewModel.requestPreview(this::bindData);
        }
    }

    public class PreviewAdapter extends BaseQuickAdapter<ProductEntity, ProductViewHolder> {

        public PreviewAdapter() {
            super(R.layout.item_product_order_layout);
        }

        @Override
        protected void convert(ProductViewHolder holder, ProductEntity entity) {
            holder.bindData(entity);
        }
    }


}

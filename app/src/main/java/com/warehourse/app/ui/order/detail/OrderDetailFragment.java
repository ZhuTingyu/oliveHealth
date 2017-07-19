package com.warehourse.app.ui.order.detail;

import com.biz.base.BaseFragment;
import com.biz.util.DialogUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.biz.widget.TimeTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.OrderEntity;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.ui.order.list.OrderViewHolder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Title: OrderDetailFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:19/05/2017  10:04
 *
 * @author johnzheng
 * @version 1.0
 */
public class OrderDetailFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private TextView mTextStatus, mTextRemind;
    //private TextView mBtn, btn1, btn2;
    private TimeTextView mRemindTime;
    private View mRemindView ;
    private ViewGroup mBtnLayout;

    private DetailAdapter mAdapter;
    private AddressViewHolder mAddressViewHolder;
    private OrderIdViewHolder mOrderIdViewHolder;
    private TotalViewHolder mTotalViewHolder;
    private PayInfoViewHolder mPayInfoViewHolder;

    protected OrderDetailViewModel mViewModel;
    private OrderViewHolder.OrderListener mOrderApplySaleListener;
    private OrderViewHolder.OrderListener mOrderCancelListener;
    private OrderViewHolder.OrderListener mOrderPayListener;

    public void setOrderPayListener(OrderViewHolder.OrderListener orderPayListener) {
        mOrderPayListener = orderPayListener;
    }

    public void setOrderApplySaleListener(OrderViewHolder.OrderListener orderApplySaleListener) {
        mOrderApplySaleListener = orderApplySaleListener;
    }

    public void setOrderCancelListener(OrderViewHolder.OrderListener orderCancelListener) {
        mOrderCancelListener = orderCancelListener;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mViewModel = new OrderDetailViewModel(getActivity());
        initViewModel(mViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_order_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setBackgroundDrawableResource(R.color.white);
        super.onViewCreated(view, savedInstanceState);

        mTextRemind = getView(R.id.title_line_1);
        mRemindTime = getView(R.id.title_line_2);
        mRemindView = (View)mRemindTime.getParent();
        mTextStatus = findViewById(R.id.text_status);

        mBtnLayout = findViewById(R.id.btn_layout);

        mRecyclerView = findViewById(android.R.id.list);
        mAdapter = new DetailAdapter();
        mOrderIdViewHolder = OrderIdViewHolder.createViewHolder(mRecyclerView);
        mAdapter.addHeaderView(mOrderIdViewHolder.itemView);

        mAdapter.addFooterView(View.inflate(getContext(), R.layout.view_line_10, null));


        mAddressViewHolder = AddressViewHolder.createViewHolder(mRecyclerView);
        mAdapter.addHeaderView(mAddressViewHolder.itemView);



        mPayInfoViewHolder = PayInfoViewHolder.createViewHolder(mRecyclerView);
        mAdapter.addFooterView(mPayInfoViewHolder.itemView);
        mAdapter.addFooterView(View.inflate(getContext(), R.layout.view_line_10, null));
        mTotalViewHolder = TotalViewHolder.createViewHolder(mRecyclerView);
        mAdapter.addFooterView(mTotalViewHolder.itemView);

        mRecyclerView.setAdapter(mAdapter);
        addItemDecorationLine(mRecyclerView);

        setOrderApplySaleListener(orderEntity -> {
            IntentBuilder.Builder().putExtra(IntentBuilder.KEY_ID, orderEntity.getOrderId())
                    .startParentActivity(this, OrderOfterSaleFragment.class, 101);
        });
        setOrderCancelListener(orderEntity -> {
            setProgressVisible(true);
            mViewModel.setOperationOrderId(orderEntity.getOrderId());
            mViewModel.cancelOrder(b -> {
                setProgressVisible(false);
                refresh();
            });
        });
        setOrderPayListener(orderEntity -> {
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
        refresh();
    }

    private void refresh() {
        setProgressVisible(true);
        mViewModel.detail(this::bindData);
    }

    private void bindData(OrderEntity entity) {
        setProgressVisible(false);
        setTitle(entity.statusDesc);
        mAdapter.setNewData(entity.getItems());
        mAddressViewHolder.bindData(entity);
        mOrderIdViewHolder.bindData(entity);
        mPayInfoViewHolder.bindData(entity);
        mTotalViewHolder.bindData(entity);

       mBtnLayout.removeAllViews();
        setBtn1Contact(entity);
        setBtn1Sale(entity.applyRefundable, entity);
        setBtn1Cancel(entity.cancelable, entity);
        setBtn2Pay(entity.payable, entity);

        mRemindView.setVisibility(View.INVISIBLE);
        if (entity.payable && entity.payLimitTime > 0) {
            mTextStatus.setVisibility(View.INVISIBLE);
            mTextRemind.setVisibility(View.VISIBLE);
            mRemindView.setVisibility(View.VISIBLE);
            mTextRemind.setText(R.string.text_surplus_pay_time);
            ((View) mTextRemind.getParent()).setVisibility(View.VISIBLE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis() + entity.payLimitTime + 5000);
            mRemindTime.setEndTimes(Calendar.getInstance().getTimeInMillis(),
                    calendar.getTimeInMillis(), this::refresh);
            if (!mRemindTime.isRun()) {
                mRemindTime.run();
            }
        } else {
            mTextStatus.setVisibility(View.INVISIBLE);
            mTextStatus.setText(entity.statusDesc);

        }
    }



    public void setBtn1Sale(boolean isRefund, OrderEntity entity) {
        if (isRefund) {
            TextView btn1 = createBtn();

            btn1.setVisibility(View.VISIBLE);
            btn1.setText(R.string.title_apply_sale);
            btn1.setBackgroundResource(R.drawable.btn_gray_selector);
            RxUtil.click(btn1).subscribe(s -> {
                if (mOrderApplySaleListener != null) {
                    mOrderApplySaleListener.call(entity);
                }
            });
        }
    }

    public void setBtn1Cancel(boolean cancelable, OrderEntity entity) {
        if (cancelable) {
            TextView btn1 = createBtn();

            btn1.setVisibility(View.VISIBLE);
            btn1.setText(R.string.text_order_list_cancel);
            btn1.setBackgroundResource(R.drawable.btn_gray_selector);
            RxUtil.click(btn1).subscribe(s -> {
                DialogUtil.createDialogViewWithCancel(btn1.getContext(), R.string.dialog_title_notice,
                        R.string.dialog_msg_cancel_order,
                        (dialog, which) -> {
                            if (mOrderCancelListener != null) {
                                mOrderCancelListener.call(entity);
                            }
//                        setProgressVisible(true);
//                        orderListItemViewModel.orderCancel(d -> {
//                            getFragment().setProgressVisible(false);
//                        });
                        }, R.string.btn_confirm);
            });
        }
    }

    public void setBtn1Contact(OrderEntity entity) {
        if (entity.contactable) {
            TextView btn1 = createBtn();

            btn1.setVisibility(View.VISIBLE);
            btn1.setText(R.string.text_order_list_tell);
            btn1.setBackgroundResource(R.drawable.btn_gray_selector);
            RxUtil.click(btn1).subscribe(s -> {
                UserModel.getInstance().createContactDialog(btn1.getContext());
            });
        }
    }

    public void setBtn2Contact(boolean isContact) {
        if (isContact) {
            TextView btn2 = createBtn();
            btn2.setVisibility(View.VISIBLE);
            btn2.setText(R.string.text_order_list_tell);
            btn2.setTextColor(getColors(R.color.color_4a4a4a));
            btn2.setBackgroundResource(R.drawable.btn_gray_selector);
            RxUtil.click(btn2).subscribe(s -> {
                UserModel.getInstance().createContactDialog(btn2.getContext());
            });
        }
    }

    public void setBtn2Pay(boolean payable, OrderEntity entity) {
        if (payable) {
            TextView btn2 = createBtn();
            btn2.setVisibility(View.VISIBLE);
            btn2.setText(R.string.text_order_list_pay);
            btn2.setTextColor(getColors(R.color.color_red));
            btn2.setBackgroundResource(R.drawable.btn_red_selector);
            RxUtil.click(btn2).subscribe(s -> {
                if (mOrderPayListener != null) {
                    mOrderPayListener.call(entity);
                }
            });
        }
    }

    private TextView createBtn(){
        TextView btn1 = new TextView(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(Utils.dip2px(80), Utils.dip2px(32));
        btn1.setGravity(Gravity.CENTER);
        btn1.setTextSize(TypedValue.COMPLEX_UNIT_PX,Utils.dip2px(16));
        btn1.setTextColor(getColors(R.color.color_4a4a4a));
        btn1.setLayoutParams(lp);
        mBtnLayout.addView(btn1);
        return btn1;
    }


    private class DetailAdapter extends BaseQuickAdapter<ProductEntity, ProductViewHolder> {

        public DetailAdapter() {
            super(R.layout.item_product_order_layout);
        }

        @Override
        protected void convert(ProductViewHolder holder, ProductEntity entity) {
            holder.bindData(entity);
        }
    }


}

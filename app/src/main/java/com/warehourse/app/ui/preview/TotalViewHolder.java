package com.warehourse.app.ui.preview;

import com.biz.base.BaseViewHolder;
import com.biz.util.DrawableHelper;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.OrderPreviewEntity;
import com.warehourse.app.model.entity.PaymentTypeEntity;
import com.warehourse.app.ui.bottomsheet.BottomSheetBuilder;

import android.support.design.widget.BottomSheetDialog;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

class TotalViewHolder extends BaseViewHolder {


    public
    TextView textPayTitle;
    public
    TextView textPayWay;
    public
    TextView textTotalTitle;
    public
    TextView textTotalPrice;
    public
    TextView textDiscountTitle;
    public
    TextView textDiscountPrice;
    public
    TextView textCouponTitle;
    public
    TextView textCouponPrice;

    PayWayAdapter mPayWayAdapter;

    BottomSheetDialog mDialog;

    TotalViewHolder(View view) {
        super(view);

        textTotalTitle = findViewById(R.id.title_line_1_left);
        textTotalPrice = findViewById(R.id.title_line_1_right);
        textDiscountTitle = findViewById(R.id.title_line_2_left);
        textDiscountPrice = findViewById(R.id.title_line_2_right);
        textCouponTitle = findViewById(R.id.title_line_3_left);
        textCouponPrice = findViewById(R.id.title_line_3_right);

        textPayTitle = findViewById(R.id.title_left);
        textPayWay = findViewById(R.id.text);

        textPayWay.setCompoundDrawables(null, null,
                DrawableHelper.getDrawableWithBounds(textPayWay.getContext(), R.drawable.ic_arrow_right_gray),
                null);
        textPayWay.setText(Html.fromHtml(
                "<font color='#999999'>" + getString(R.string.dialog_title_choose_pay_way) + "</font>"));
        textTotalTitle.setText(R.string.text_product_total);
        textDiscountTitle.setText(R.string.text_promote_order);
        textCouponTitle.setText(R.string.text_money_coupon);
        textCouponTitle.setTextColor(getColors(R.color.color_b2b2b2));
        textDiscountTitle.setTextColor(getColors(R.color.color_b2b2b2));



    }

    public void setPayData(List<PaymentTypeEntity> list, Action1<Integer> paymentId) {

        View payView = (View) textPayTitle.getParent();
        payView.setOnClickListener(e -> {
            createSheet(list, paymentId);
        });

    }


    public void bindData(OrderPreviewEntity entity) {
        textTotalPrice.setText(PriceUtil.formatRMB(entity.orderAmount));
        textCouponPrice.setText("-" + PriceUtil.formatRMB(entity.voucherAmount));
        textDiscountPrice.setText("-" + PriceUtil.formatRMB(entity.freeAmount));
    }


    public static TotalViewHolder createViewHolder(ViewGroup parent) {
        return new TotalViewHolder(inflater(R.layout.item_order_pay_total_layout, parent));
    }

    private void createSheet(List<PaymentTypeEntity> list, Action1<Integer> paymentId) {
        mPayWayAdapter = new PayWayAdapter();
        View mTitleView = View.inflate(itemView.getContext(), R.layout.item_single_text_layout, null);
        TextView textView = (TextView) mTitleView.findViewById(R.id.title);
        textView.setText(R.string.dialog_title_choose_pay_way);
        mPayWayAdapter.addHeaderView(mTitleView);
        mPayWayAdapter.setNewData(list);
        mDialog = BottomSheetBuilder.createBottomSheet(itemView.getContext(), mPayWayAdapter);
        mPayWayAdapter.setOnItemClickListener((BaseQuickAdapter baseQuickAdapter, View v, int i) -> {
            PaymentTypeEntity s = mPayWayAdapter.getItem(i);
            textPayWay.setText(s.getPayName());
            Observable.just(s.paymentId).subscribe(paymentId);
            if (mDialog != null) {
                mDialog.dismiss();
            }
        });
    }
}
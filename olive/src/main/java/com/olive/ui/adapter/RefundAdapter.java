package com.olive.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.TimeUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.OrderEntity;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.refund.LookRefundCheckResultFragment;
import com.olive.ui.refund.viewModel.LookApplyRefundDetailViewModel;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/29.
 */

public class RefundAdapter extends BaseQuickAdapter<OrderEntity, BaseViewHolder> {



    private Activity context;
    private String type;

    public RefundAdapter(Activity context, String type) {
        super(R.layout.item_refund_info_layout, Lists.newArrayList());
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, OrderEntity orderEntity) {

        String status = "";
        TextView tvStatus = holder.findViewById(R.id.status);

        if(orderEntity.status == LookApplyRefundDetailViewModel.STATUS_WAIT_CHECK){
            status = mContext.getString(R.string.text_refund_wait_check);
            tvStatus.setTextColor(mContext.getResources().getColor(R.color.red_light));
        }else if(orderEntity.status == LookApplyRefundDetailViewModel.STATUS_PASS_CHECK){
            status = mContext.getString(R.string.text_refund_pass_check);
            tvStatus.setTextColor(mContext.getResources().getColor(R.color.green_light));
        }else if(orderEntity.status == LookApplyRefundDetailViewModel.STATUS_NOT_PASS_CHECK){
            status = mContext.getString(R.string.text_refund_not_pass_check);
            tvStatus.setTextColor(mContext.getResources().getColor(R.color.red_light));
        }

        tvStatus.setText(status);

        List<ProductEntity> productEntities = orderEntity.products;
        LinearLayout linearLayout = holder.findViewById(R.id.ll_info);
        linearLayout.removeAllViews();

        for(ProductEntity productEntity : productEntities){
            View view = LayoutInflater.from(context).inflate(R.layout.item_line_text_layout, linearLayout,false);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView number = (TextView) view.findViewById(R.id.number);
            name.setText(productEntity.name);
            number.setText("x"+productEntity.quantity);
            linearLayout.addView(view);
        }

        TextView service = holder.findViewById(R.id.contact_service);
        TextView look = holder.findViewById(R.id.look_result);

        service.setOnClickListener(v -> {

        });

        look.setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_VALUE, orderEntity.orderNo)
                    .startParentActivity(context, LookRefundCheckResultFragment.class, true);
        });


        if(context.getString(R.string.text_refund_list).equals(type)){
            TextView date = holder.findViewById(R.id.date);
            TextView totalNumber = holder.findViewById(R.id.total_number);
            TextView remark = holder.findViewById(R.id.remark);

            remark.setVisibility(View.VISIBLE);
            totalNumber.setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
            service.setVisibility(View.GONE);
            look.setVisibility(View.GONE);

            date.setText(TimeUtil.format(orderEntity.orderDate, TimeUtil.FORMAT_YYYYMMDD));
            totalNumber.setText(context.getString(R.string.text_products_total_number,getTotalCount(productEntities)+""));
            remark.setText(mContext.getString(R.string.text_refund_check_note, orderEntity.note));
        }

    }

    public int getTotalCount(List<ProductEntity> productEntities) {
        int count = 0;
        for (ProductEntity productEntity : productEntities) {
            count += productEntity.quantity;
        }
        return count;
    }
}

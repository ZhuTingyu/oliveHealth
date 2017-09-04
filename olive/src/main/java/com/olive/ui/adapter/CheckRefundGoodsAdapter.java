package com.olive.ui.adapter;

import android.view.View;
import android.widget.RelativeLayout;

import com.biz.base.BaseActivity;
import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.biz.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.refund.ApplyRefundFragment;
import com.olive.ui.refund.ChooseRefundGoodsFragment;
import com.olive.util.LoadImageUtil;


/**
 * Created by TingYu Zhu on 2017/9/4.
 */

public class CheckRefundGoodsAdapter extends BaseQuickAdapter<ProductEntity, BaseViewHolder> {

    private boolean isLook;

    public CheckRefundGoodsAdapter() {
        super(R.layout.item_cart_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, ProductEntity productEntity) {
        LoadImageUtil.Builder()
                .load(productEntity.imgLogo).http().build()
                .displayImage(holder.getView(R.id.icon_img));
        holder.setText(R.id.title, productEntity.name);
        holder.setText(R.id.title_line_2, mContext.getString(R.string.text_product_specification, productEntity.standard));
        holder.setText(R.id.title_line_3, PriceUtil.formatRMB(productEntity.price));
        holder.getView(R.id.checkbox).setVisibility(View.GONE);
        holder.getView(R.id.number_layout).setVisibility(View.GONE);
        holder.getView(R.id.text_product_number).setVisibility(View.VISIBLE);
        holder.setText(R.id.text_product_number, "x" + productEntity.quantity);
        if (!isLook) {
            RelativeLayout rl = holder.findViewById(R.id.rl_info);
            rl.setPadding(0, 0, Utils.dip2px(24), 0);
            holder.findViewById(R.id.right_icon).setVisibility(View.VISIBLE);

            holder.itemView.setOnClickListener(v -> {
                IntentBuilder.Builder()
                        .startParentActivity((BaseActivity) mContext, ChooseRefundGoodsFragment.class, ApplyRefundFragment.CHOOSE_GOODS__SUCCESS_REQUEST);
            });
        }
    }


/*    @Override
    public void onBindViewHolder(BaseViewHolder holder, int positions) {
        super.onBindViewHolder(holder, positions);
        com.olive.util.Utils.setListHeight(getRecyclerView());
    }*/

    public void setLook(boolean look) {
        isLook = look;
    }
}

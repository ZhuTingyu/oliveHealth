package com.olive.ui.refund;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.util.IdsUtil;
import com.biz.util.Utils;
import com.biz.widget.CustomDraweeView;
import com.olive.ui.refund.viewModel.LookApplyDetailViewModel;
import com.olive.util.LoadImageUtil;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/8/11.
 */

public class LookApplyDetailFragament extends ApplyRefundFragment {

    private LookApplyDetailViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new LookApplyDetailViewModel(context);
    }

    @Override
    protected void initView() {
        viewModel.getApplyRefundDetail(orderEntity -> {
            initGoodsInfoView(orderEntity.products);
            for (int i = 0; i < productInfoViews.size(); i++) {
                productInfoViews.get(i).setPadding(0, 0, 0, 0);
            }
            reason.setText(orderEntity.reason);
            setRight(reason);
            describe.setText(orderEntity.description);
            describe.setFocusableInTouchMode(false);
            initImageView(IdsUtil.getList(orderEntity.image, ",", false));
        });
    }

    private void initImageView(List<String> imgs) {
        for (int i = 0; i < imgs.size(); i++) {
            CustomDraweeView view = new CustomDraweeView(getContext());
            view.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(70), Utils.dip2px(70)));
            LoadImageUtil.Builder().load(imgs.get(i)).http().build().displayImage(view);
            imgsLinearLayout.addView(view);
        }
    }

    private void setRight(TextView view) {
        view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }
}

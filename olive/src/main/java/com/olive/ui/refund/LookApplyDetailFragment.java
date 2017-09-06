package com.olive.ui.refund;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.biz.util.IdsUtil;
import com.biz.util.Lists;
import com.biz.util.Utils;
import com.biz.widget.CustomDraweeView;
import com.olive.R;
import com.olive.ui.refund.viewModel.LookApplyRefundDetailViewModel;
import com.olive.util.LoadImageUtil;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/8/11.
 */

public class LookApplyDetailFragment extends ApplyRefundFragment {

    private LookApplyRefundDetailViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new LookApplyRefundDetailViewModel(context);
    }

    @Override
    protected void initView() {

        ScrollView scrollView = findViewById(R.id.scrollView);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0,0,0);
        scrollView.setLayoutParams(layoutParams);

        viewModel.getApplyRefundDetail(orderEntity -> {
            initGoodsInfoView(orderEntity.products, true);
            describe.setFocusableInTouchMode(false);
            describe.setText(orderEntity.desc == null || orderEntity.desc.isEmpty() ? getString(R.string.text_refund_no_describe) : orderEntity.desc);
            initImageView(getImagesList(orderEntity.image));
            ok.setVisibility(View.GONE);
            TextView reasonText = findViewById(R.id.reason_text);
            reasonText.setVisibility(View.VISIBLE);
            reasonText.setText(orderEntity.reason != null && !orderEntity.reason.isEmpty() ? orderEntity.reason : getString(R.string.text_refund_reason_nothing));
        });
    }

    private List<String> getImagesList(String imgString){

        List<String> list = Lists.newArrayList();
        if(!imgString.contains(",")){
            list.add(imgString);
        }else {
            list = IdsUtil.getList(imgString, ",", false);
        }

        return list;

    }

    private void initImageView(List<String> imgs) {
        imgsLinearLayout.setVisibility(View.VISIBLE);
        imgsGrid.setVisibility(View.GONE);
        if(imgs.isEmpty()){
            findViewById(R.id.text1).setVisibility(View.GONE);
        }else {
            for (int i = 0; i < imgs.size(); i++) {
                CustomDraweeView view = new CustomDraweeView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.dip2px(70), Utils.dip2px(70));
                layoutParams.setMargins(Utils.dip2px(8),0,Utils.dip2px(8),0);
                view.setLayoutParams(layoutParams);
                LoadImageUtil.Builder().load(imgs.get(i)).http().build().displayImage(view);
                imgsLinearLayout.addView(view);
                view.setTag(i);
                view.setOnClickListener(v -> {
                    showImageDialog(getContext(), imgs, (Integer) v.getTag());
                });
            }
        }

    }

    public static void showImageDialog(Context context, List<String> list, int startPosition) {
        new ImageViewer.Builder<String>(context, list)
                .setStartPosition(startPosition)
                .hideStatusBar(false)
                .allowZooming(true)
                .allowSwipeToDismiss(true)
                //.setBackgroundColorRes(colorRes)
                //.setBackgroundColor(color)
                //.setImageMargin(margin)
                //.setImageMarginPx(marginPx)
                //.setContainerPadding(this, dimen)
                //.setContainerPadding(this, dimenStart, dimenTop, dimenEnd, dimenBottom)
                //.setContainerPaddingPx(padding)
                //.setContainerPaddingPx(start, top, end, bottom)
//                        .setCustomImageRequestBuilder(imageRequestBuilder)
//                        .setCustomDraweeHierarchyBuilder(draweeHierarchyBuilder)
//                        .setImageChangeListener(imageChangeListener)
//                        .setOnDismissListener(onDismissListener)
//                        .setOverlayView(overlayView)
                .show();
    }

}

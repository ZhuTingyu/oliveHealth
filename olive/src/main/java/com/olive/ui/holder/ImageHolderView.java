package com.olive.ui.holder;

import com.bigkoo.convenientbanner.holder.Holder;
import com.biz.util.Utils;
import com.biz.widget.CustomDraweeView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.olive.R;
import com.olive.util.LoadImageUtil;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class ImageHolderView implements Holder<String> {
    CustomDraweeView icon;
    int BANNER_HEIGHT = 120;
    private ScalingUtils.ScaleType scaleType = ScalingUtils.ScaleType.CENTER_INSIDE ;

    public ImageHolderView(int height) {
        this.BANNER_HEIGHT = height;
    }

    public ImageHolderView(int height, ScalingUtils.ScaleType scaleType) {
        this.BANNER_HEIGHT = height;
        this.scaleType = scaleType;
    }

    public ImageHolderView() {
    }

    @Override
    public View createView(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        icon = new CustomDraweeView(context);
        icon.setLayoutParams(
                new ViewGroup.LayoutParams(dm.widthPixels, Utils.dip2px(BANNER_HEIGHT)));
        icon.setBackgroundResource(R.color.white);
        icon.setActualImageScaleType(scaleType);
        return icon;
    }

    @Override
    public void UpdateUI(Context context, final int position, String data) {
        icon.setFadeDuration(300);
        icon.setActualImageScaleType(scaleType);
        icon.setPressedStateOverlayId(R.color.color_transparent_half);


        LoadImageUtil.Builder()
                .load(data).http().build()
                .displayImage(icon);
    }
}

package com.warehourse.app.ui.adv;

import com.biz.base.BaseFragment;
import com.biz.base.FragmentBackHelper;
import com.biz.util.Utils;
import com.biz.widget.CustomDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.warehourse.app.R;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.entity.NoticeEntity;
import com.warehourse.app.ui.main.MainActivity;
import com.warehourse.app.util.LoadImageUtil;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Title: AdvertisingFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:22/05/2017  13:43
 *
 * @author johnzheng
 * @version 1.0
 */

public class AdvertisingFragment extends BaseFragment implements FragmentBackHelper{

    private CustomDraweeView icon;
    private AppCompatImageView btnClose;
    private NoticeEntity entity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advertising_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        icon =  findViewById(R.id.icon);
        btnClose = findViewById(R.id.btn_close);
        icon.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);

        entity = SystemModel.getInstance().getNoticeEntity();

        btnClose.setOnClickListener(v -> {
            removeFragment();
        });
        view.setOnClickListener(v -> {});
        icon.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(entity.clickUrl)) {
                MainActivity.startUri(v.getContext(), entity.clickUrl);
            }
            removeFragment();
        });

    }

    @Override
    public void onResume() {
        super.onResume();



       icon.postDelayed(()->{
           DraweeController controller = Fresco.getDraweeControllerBuilderSupplier().get()
                   .setCallerContext(icon.getContext())
                   .setControllerListener(controllerListener)
                   .setOldController(icon.getController())
                   .setUri(LoadImageUtil.Builder()
                           .load(entity.pictureUrl)
                           .http().build().displayImageUri(icon))
                   .build();
           icon.setController(controller);
       },500);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void removeFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(AdvertisingFragment.this)
                .commitAllowingStateLoss();
    }

    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(
                String id,
                @Nullable ImageInfo imageInfo,
                @Nullable Animatable anim) {
           setImageInfo(imageInfo);
        }

        @Override
        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
           setImageInfo(imageInfo);
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
        }
    };

    private void setImageInfo( @Nullable ImageInfo imageInfo){
        if (imageInfo == null) {
            return;
        }
        if (AdvertisingFragment.this.isAdded()){
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int height = (dm.widthPixels- Utils.dip2px(60)) * imageInfo.getHeight() / imageInfo.getWidth();
            int width = (icon.getMeasuredHeight()-Utils.dip2px(120)) * imageInfo.getWidth() / imageInfo.getHeight();
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) icon.getLayoutParams();
            if (imageInfo.getWidth() < imageInfo.getHeight())
                lp.width = width;
            else lp.height = height;
            icon.setLayoutParams(lp);
        }
    }

    @Override
    public boolean onBackPressed() {
        removeFragment();
        return true;
    }
}

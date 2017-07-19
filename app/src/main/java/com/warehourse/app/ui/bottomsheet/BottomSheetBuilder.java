package com.warehourse.app.ui.bottomsheet;

import com.biz.util.Lists;
import com.biz.util.Utils;
import com.biz.widget.CustomDraweeView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.application.WareApplication;
import com.warehourse.app.util.LoadImageUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import java.util.List;

/**
 * Title: BottomSheetBuilder
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:16/05/2017  10:36
 *
 * @author johnzheng
 * @version 1.0
 */

public class BottomSheetBuilder {


    public static BottomSheetDialog createBottomSheet(Context context, BaseQuickAdapter adapter) {
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setPadding(0, 0, 0, Utils.dip2px(20));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(com.biz.http.R.color.color_divider).size(1).build());
        recyclerView.setAdapter(adapter);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(recyclerView);
        bottomSheetDialog.show();
        return bottomSheetDialog;
    }



    public static BottomSheetDialog createPhotoBottomSheet(Context context
            , BaseQuickAdapter.OnItemClickListener onItemClickListener) {
        RecyclerView recyclerView = new RecyclerView(context);
        //recyclerView.setPadding(0, 0, 0, Utils.dip2px(20));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(com.biz.http.R.color.color_divider).size(1).build());
        BottomSheetAdapter adapter = new BottomSheetAdapter(BottomSheetMultipleItem.getList());
        adapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(adapter);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(recyclerView);
        bottomSheetDialog.show();
        return bottomSheetDialog;
    }

    public static BottomSheetDialog createPhotoBottomSheetWithHeader(Context context, String url
            , BaseQuickAdapter.OnItemClickListener onItemClickListener) {
        RecyclerView recyclerView = new RecyclerView(context);
        //recyclerView.setPadding(0, 0, 0, Utils.dip2px(20));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(com.biz.http.R.color.color_divider).size(1).build());
        BottomSheetAdapter adapter = new BottomSheetAdapter(BottomSheetMultipleItem.getList());
        adapter.setOnItemClickListener(onItemClickListener);
        adapter.setHeaderView(getHeaderView(context,  url));
        recyclerView.setAdapter(adapter);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(recyclerView);
        bottomSheetDialog.show();
        return bottomSheetDialog;
    }

    private static CustomDraweeView getHeaderView(Context context, String url){
        CustomDraweeView customDraweeView = new CustomDraweeView(context);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        customDraweeView.setLayoutParams(new ViewGroup.LayoutParams(dm.widthPixels, dm.widthPixels + Utils.dip2px(20)));
        int padding = Utils.dip2px(20);
        customDraweeView.setPadding(padding, padding, padding, padding);
        LoadImageUtil.Builder().loadAssets(url).build()
                .imageOptions(R.color.color_transparent)
                .displayImage(customDraweeView);
        return customDraweeView;
    }



}

package com.warehourse.app.ui.launch;


import com.biz.base.BaseViewModel;
import com.biz.util.FrescoImageUtil;
import com.biz.util.SharedPreferencesUtil;
import com.warehourse.app.model.AvgModel;
import com.warehouse.dao.AvgBean;

import android.Manifest;
import android.app.Activity;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by wangwei on 2016/3/19.
 */
public class LaunchViewModel extends BaseViewModel {

    public boolean isFirstLaunch = false;
    private static final String IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH";
    public String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA};

    public LaunchViewModel(Object activity) {
        super(activity);
        isFirstLaunch  = SharedPreferencesUtil.getBoolean(getActivity(),
                FrescoImageUtil.SharedPreferences_ConfigNAME, IS_FIRST_LAUNCH, true);

    }

    public boolean isFirstLaunch() {
        return isFirstLaunch;
    }

    public void setFirstLaunch() {
        isFirstLaunch = true;
        SharedPreferencesUtil.setBoolean(getActivity(), FrescoImageUtil.SharedPreferences_ConfigNAME, IS_FIRST_LAUNCH, false);
    }

    public void requestAvg(Action1<AvgBean> onNext) {
        submitRequestThrowError(AvgModel.getShowAvg(),onNext);
    }

}

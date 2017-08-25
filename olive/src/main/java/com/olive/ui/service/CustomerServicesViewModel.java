package com.olive.ui.service;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.google.gson.JsonObject;
import com.olive.model.ServeModel;

import org.json.JSONException;
import org.json.JSONObject;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/25.
 */

public class CustomerServicesViewModel extends BaseViewModel {

    public CustomerServicesViewModel(Object activity) {
        super(activity);
    }

    public void getServeContent(Action1<String> action1){
        submitRequestThrowError(ServeModel.serveContent().map(r -> {
            if(r.isOk()){
                return r.data.serveContent;
            }else throw new HttpErrorException(r);
        }),action1);
    }

}

package com.warehourse.app.ui.login;

import android.text.TextUtils;

import com.biz.base.BaseViewModel;
import com.biz.util.ValidUtil;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Title: LoginViewModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/17  15:00
 *
 * @author wangwei
 * @version 1.0
 */
class LoginViewModel extends BaseViewModel {
    private String account;
    private String password;
    private final BehaviorSubject<String> loginMobile=BehaviorSubject.create();
    public LoginViewModel(Object activity) {
        super(activity);
    }
    public void request()
    {
        submitRequest(UserModel.loginMobile(), s -> {
            if(!TextUtils.isEmpty(s))
                loginMobile.onNext(s);
        },throwable -> {});
    }


    public void login(Action0 action0)
    {
        if(account!=null)
        {
            account=account.trim();
        }
        if (!ValidUtil.phoneNumberValid(account)) {
            error.onNext(getErrorString(R.string.text_error_register_phone_not_valid));
            return;
        }
        if (!ValidUtil.pwdValid(password)) {
            error.onNext(getErrorString(R.string.text_error_register_pwd_not_valid));
            return;
        }
        submitRequest(UserModel.login(account,password),r->{
            if(r.isOk()){
                submitRequest(UserModel.saveLoginMobile(account),b->{});
                Observable.just("").subscribe(a->{}, throwable -> {},action0);
            }else{
                error.onNext(getErrorString(r.msg));
            }
        },throwable -> {
            error.onNext(getError(throwable));
        });

    }



    public Action1<String> setAccount() {
        return account -> {
            this.account = account;
        };
    }

    public Action1<String> setPassword() {
        return password -> {
            this.password = password;
        };
    }

    public BehaviorSubject<String> getLoginMobile() {
        return loginMobile;
    }
}

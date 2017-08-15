package com.olive.ui.login;

import android.text.TextUtils;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.R;
import com.olive.model.UserModel;
import com.olive.model.entity.UserEntity;


import rx.Observable;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/7/26.
 */

public class LoginViewModel extends BaseViewModel {

    private String userName;
    private String password;

    public LoginViewModel(Object activity) {
        super(activity);
    }

    public  void login(Action1<UserEntity> onNext){

        submitRequestThrowError(UserModel.login(userName, password + getString(R.string.string_password_suffix)).map(r ->{
            if(r.isOk()){
                submitRequest(UserModel.saveLoginMobile(userName), b -> {
                });
                return r.data;
            }else throw new HttpErrorException(r);
        }),onNext);
    }

    public Action1<String> setUserName(){
        return s -> {
            userName = s;
        };
    }

    public Action1<String> setPassword(){
        return s -> {
            password = s;
        };
    }

    public void isCanLogin(Action1<Boolean> action1) {
        Observable.just(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)).subscribe(action1);
    }

}

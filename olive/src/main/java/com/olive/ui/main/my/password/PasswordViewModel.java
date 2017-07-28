package com.olive.ui.main.my.password;

import com.biz.base.BaseViewModel;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class PasswordViewModel extends BaseViewModel {

    private String userName;
    private String code;
    private String password;
    private String newPassword;

    public PasswordViewModel(Object activity) {
        super(activity);
    }

    public Action1<String> setUserName(){
        return s -> {
            this.userName = s;
        };
    }

    public Action1<String> setCode(){
        return s -> {
            this.code = s;
        };
    }

    public Action1<String> setPassword(){
        return s -> {
            this.password = s;
        };
    }

    public Action1<String> setNewPassword(){
        return s -> {
            this.newPassword = s;
        };
    }

}

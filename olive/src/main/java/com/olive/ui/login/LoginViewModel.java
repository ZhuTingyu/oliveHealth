package com.olive.ui.login;

import com.biz.base.BaseViewModel;
import com.biz.base.RestErrorInfo;
import com.biz.util.MD5;
import com.olive.model.UserModel;
import com.olive.model.entity.UserEntity;

import java.util.Observable;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/7/26.
 */

public class LoginViewModel extends BaseViewModel {

    private String password;

    public LoginViewModel(Object activity) {
        super(activity);
    }

    public  void login(Action1<UserEntity> onNext){
        submitRequest(UserModel.login("1234567890111", "123456dms"), r -> {
            if (r.isOk()) {
                rx.Observable.just(r.data).subscribe(onNext);
                submitRequest(UserModel.saveLoginMobile("1234567890111"), b -> {
                });
            } else {
                error.onNext(new RestErrorInfo(r.msg));
            }
        }, e -> {
            error.onNext(getError(e));
        });
    }

}

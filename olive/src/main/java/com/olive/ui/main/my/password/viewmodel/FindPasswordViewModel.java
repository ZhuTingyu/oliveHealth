package com.olive.ui.main.my.password.viewmodel;


import com.biz.util.ValidUtil;
import com.olive.R;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/23.
 */

public class FindPasswordViewModel extends PasswordViewModel {

    public FindPasswordViewModel(Object activity) {
        super(activity);
    }

    @Override
    public Action1<String> setMobile() {
        return s -> {
            this.mobile = s;
            setInfoValid();
        };
    }

    @Override
    public Action1<String> setAuthCode() {
        return s -> {
            this.authCode = s;
            setInfoValid();
        };
    }

    @Override
    public Action1<String> setNewPassword() {
        return s -> {
            newPassword = s;
            setPasswordValid();
        };
    }

    @Override
    public Action1<String> setPassword() {
        return s -> {
            password = s;
            setPasswordValid();
        };
    }

    @Override
    public void resetPassword(Action1<String> action1) {

        if(!ValidUtil.pwdValid(password) || !ValidUtil.pwdValid(newPassword)){
            error.onNext(getErrorString(R.string.message_password_length_more_than_six));
            return;
        }

        if (!password.equals(newPassword)) {
            error.onNext(getErrorString(getActivity().getString(R.string.message_password_not_same)));
            return;
        }
        super.resetPassword(action1);
    }
}

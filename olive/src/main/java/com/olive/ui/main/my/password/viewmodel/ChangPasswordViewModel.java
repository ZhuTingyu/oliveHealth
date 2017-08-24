package com.olive.ui.main.my.password.viewmodel;

import com.biz.util.ValidUtil;
import com.olive.R;
import com.olive.model.UserModel;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/23.
 */

public class ChangPasswordViewModel extends PasswordViewModel {

    public ChangPasswordViewModel(Object activity) {
        super(activity);
    }

    @Override
    public Action1<String> setMobile() {
        return s -> {
            mobile = s;
            setAllInfoValid();
        };
    }

    @Override
    public Action1<String> setAuthCode() {
        return s -> {
            authCode = s;
            setAllInfoValid();
        };
    }

    @Override
    public Action1<String> setNewPassword() {
        return s -> {
            newPassword = s;
            setAllInfoValid();
        };
    }

    @Override
    public Action1<String> setPassword() {
        return s -> {
            password = s;
            setAllInfoValid();
        };
    }

    private boolean setAllInfoValid() {
        return setInfoValid() && setPasswordValid();
    }

    @Override
    public void changPassword(Action1<String> action1) {

        if(!ValidUtil.phoneNumberValid(mobile)){
            error.onNext(getErrorString(R.string.message_input_valid_mobile));
            return;
        }

        if(!ValidUtil.pwdValid(password) || !ValidUtil.pwdValid(newPassword)){
            error.onNext(getErrorString(R.string.message_password_length_more_than_six));
            return;
        }


        if (!UserModel.getInstance().getPassword().equals(password)) {
            error.onNext(getErrorString(getActivity().getString(R.string.message_old_password_error)));
            return;
        }
        super.changPassword(action1);
    }

    @Override
    public void resetPayPassword(Action1<String> action1) {

        if(!ValidUtil.pwdValid(password) || !ValidUtil.pwdValid(newPassword)){
            error.onNext(getErrorString(R.string.message_password_length_more_than_six));
        }

        if (!newPassword.equals(password)) {
            error.onNext(getErrorString(getActivity().getString(R.string.message_password_not_same)));
            return;
        }

        super.resetPayPassword(action1);
    }
}

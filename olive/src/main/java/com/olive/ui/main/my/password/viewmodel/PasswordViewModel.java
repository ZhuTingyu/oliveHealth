package com.olive.ui.main.my.password.viewmodel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.ValidUtil;
import com.olive.R;
import com.olive.model.AccountModel;
import com.olive.model.UserModel;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public abstract class PasswordViewModel extends BaseViewModel {

    public static final int TYPE_CODE_FIND_PASSWORD = 1;
    public static final int TYPE_CODE_MODIFY_PASSWORD = 2;
    public static final int TYPE_CODE_RESET_PAY_PASSWORD = 3;

    private final BehaviorSubject<Boolean> isValid = BehaviorSubject.create();


    public String mobile;
    public String authCode;
    public String password;
    public String newPassword;

    private int type;

    public PasswordViewModel(Object activity) {
        super(activity);
    }

    public void changPassword(Action1<String> action1) {
        submitRequestThrowError(AccountModel.changePassword(mobile, authCode, newPassword).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void resetPassword(Action1<String> action1) {
        submitRequestThrowError(AccountModel.resetPassword(mobile, authCode, newPassword).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void sendCode(Action1<String> action1) {
        if(!ValidUtil.phoneNumberValid(mobile)){
            error.onNext(getErrorString(R.string.message_input_valid_mobile));
            return;
        }
        submitRequestThrowError(AccountModel.sendCode(mobile, type).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void validateCode(Action1<String> action1) {
        submitRequestThrowError(AccountModel.validateCode(mobile, authCode, type).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void resetPayPassword(Action1<String> action1) {
        submitRequestThrowError(AccountModel.resetPayPassword(mobile, authCode, newPassword).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public abstract Action1<String> setMobile();

    public abstract Action1<String> setAuthCode();


    public abstract Action1<String> setNewPassword();

    public abstract Action1<String> setPassword();


    private boolean isStringValid(String string) {
        return string != null && !string.isEmpty();
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    protected boolean setInfoValid() {
        boolean isV = isStringValid(mobile) && isStringValid(authCode);
        isValid.onNext(isV);
        return isV;
    }

    protected boolean setPasswordValid() {
        boolean isV = isStringValid(password) && isStringValid(newPassword);
        isValid.onNext(isV);
        return isV;
    }


    public BehaviorSubject<Boolean> getIsValid() {
        return isValid;
    }
}

package com.olive.ui.main.my.password;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.R;
import com.olive.model.AccountModel;
import com.olive.model.UserModel;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class PasswordViewModel extends BaseViewModel {

    public static final int TYPE_CODE_FIND_PASSWORD = 1;
    public static final int TYPE_CODE_MODIFY_PASSWORD = 2;
    public static final int TYPE_CODE_RESET_PAY_PASSWORD = 3;

    public static final String INFO_VALID = "valid";


    public String mobile;
    public String authCode;
    private String password;
    private String newPassword;

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

    public void resetPayPassword(Action1<String> action1){
        submitRequestThrowError(AccountModel.resetPayPassword(mobile, authCode, newPassword).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public Action1<String> setMobile() {
        return s -> {
            this.mobile = s;
        };
    }

    public Action1<String> setAuthCode() {
        return s -> {
            this.authCode = s;
        };
    }


    public Action1<String> setNewPassword() {
        return s -> {
            this.newPassword = s + getString(R.string.string_password_suffix);
        };
    }

    public Action1<String> setPassword() {
        return s -> {
            this.password = s + getString(R.string.string_password_suffix);
        };
    }


    public void isInfoValid(Action1<String> action1) {
        final String[] message = new String[1];
        if(isMobileValid()){
            if(isCodeValid()){
                isOldAndNewPasswordValid(s -> {
                    message[0] = s;
                });
            }else {
                message[0] = getString(R.string.message_input_valid_code);
            }
        }else {
            message[0] = getString(R.string.message_input_valid_mobile);
        }

        Observable.just(message[0]).subscribe(action1);

    }

    public void isPasswordValid(Action1<String> action1) {
        String message = INFO_VALID;
        if (!isStringValid(password) || !isStringValid(newPassword)) {
            message = getActivity().getString(R.string.message_input_password);
            if (!password.equals(newPassword)) {
                message = getActivity().getString(R.string.message_password_not_same);
            }
        }
        Observable.just(message).subscribe(action1);
    }

    public void isOldAndNewPasswordValid(Action1<String> action1) {
        String message = INFO_VALID;
        if (!isStringValid(password) || !isStringValid(newPassword)) {
            message = getActivity().getString(R.string.message_input_password);
            if (!UserModel.getInstance().getPassword().equals(password)) {
                message = getActivity().getString(R.string.message_old_password_error);
            }
        }
        Observable.just(message).subscribe(action1);
    }

    public boolean isMobileValid() {
        if (!isStringValid(mobile) || mobile.length() < 11) {
            return false;
        }else return true;
    }

    public boolean isCodeValid() {
        if (!isStringValid(authCode) || authCode.length() < 6) {
            return false;
        }else return true;
    }

    private boolean isStringValid(String string) {
        if (string != null && !string.isEmpty()) {
            return true;
        } else return false;
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


}

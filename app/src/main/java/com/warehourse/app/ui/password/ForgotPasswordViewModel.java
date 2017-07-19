package com.warehourse.app.ui.password;

import android.text.TextUtils;

import com.biz.base.BaseActivity;
import com.biz.base.BaseViewModel;
import com.biz.util.ValidUtil;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by wangwei on 2016/3/19.
 */
class ForgotPasswordViewModel extends BaseViewModel {
    private String pwd1;
    private String pwd2;
    private String phone;
    private String code;
    private final BehaviorSubject<Boolean> datValid = BehaviorSubject.create();

    public ForgotPasswordViewModel(Object activity) {
        super(activity);
        BaseActivity baseActivity = getActivity();
        if (null != baseActivity) {
            phone = baseActivity.getIntent().getStringExtra(FindPasswordViewModel.KEY_FORGOT_PWD_PHONE);
            code = baseActivity.getIntent().getStringExtra(FindPasswordViewModel.KEY_FORGOT_PWD_CODE);
        }
    }

    public void forgot(Action1<Boolean> onNext) {
        if (!ValidUtil.phoneNumberValid(phone)) {
            error.onNext(getErrorString(R.string.text_error_register_phone_not_valid));
            return;
        }
        if (pwd1 != null && pwd1.length() < 6) {
            error.onNext(getErrorString(R.string.error_pwd_length));
            return;
        }
        if (pwd2 != null && pwd2.length() < 6) {
            error.onNext(getErrorString(R.string.error_pwd_length));
            return;
        }
        if(!ValidUtil.pwdValid(pwd1)){
            error.onNext(getErrorString(R.string.error_invalid_password));
            return;
        }
        if (TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2) || !pwd1.equals(pwd2)) {
            error.onNext(getErrorString(R.string.text_error_forgot_two_pwd_not_exists));
            return;
        }
        submitRequest(UserModel.forgotPassword(phone, pwd1, code),
                r -> {
                    if (r.isOk()) {
                        Observable.just(true).subscribe(onNext);
                    } else {
                        error.onNext(getErrorString(r.msg));
                    }
                },
                throwable -> {
                    error.onNext(getError(throwable));
                });
    }

    public Action1<String> setPwd1() {
        return s -> {
            this.pwd1 = s;
            setDataValid();
        };
    }

    public Action1<String> setPwd2() {
        return s -> {
            this.pwd2 = s;
            setDataValid();
        };
    }

    private void setDataValid() {
        boolean isV = !TextUtils.isEmpty(pwd1) && !TextUtils.isEmpty(pwd2);
        datValid.onNext(isV);
    }

    public Observable<Boolean> getDatValid() {
        return datValid;
    }
}

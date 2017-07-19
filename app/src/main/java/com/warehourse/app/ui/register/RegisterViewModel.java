package com.warehourse.app.ui.register;

import android.text.TextUtils;

import com.biz.base.BaseViewModel;
import com.biz.util.ValidUtil;
import com.warehourse.app.R;
import com.warehourse.app.model.SmsModel;
import com.warehourse.app.model.UserModel;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by wangwei on 2016/3/19.
 */
class RegisterViewModel extends BaseViewModel {
    private String phone;
    private String code;
    private String pwd;
    private String recommendCode;
    private final BehaviorSubject<Boolean> dataValid = BehaviorSubject.create();

    public RegisterViewModel(Object activity) {
        super(activity);
    }

    public void sendCode(Action1<Boolean> onNext) {
        if (phone != null) {
            phone = phone.trim();
        }
        if (!ValidUtil.phoneNumberValid(phone)) {
            error.onNext(getErrorString(R.string.text_error_register_phone_not_valid));
            return;
        }
        submitRequest(SmsModel.registerSendSms(phone), r -> {
            if (r.isOk()) {
                Observable.just(true).subscribe(onNext);
            } else {
                error.onNext(getErrorString(r.msg));
            }
        }, throwable -> {
            error.onNext(getError(throwable));
        });
    }

    public void register(Action1<Boolean> onNext) {
        if (phone != null) {
            phone = phone.trim();
        }
        if (code != null) {
            code = code.trim();
        }
        if (recommendCode != null) {
            recommendCode = recommendCode.trim();
        }

        if (!ValidUtil.phoneNumberValid(phone)) {
            error.onNext(getErrorString(R.string.text_error_register_phone_not_valid));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            error.onNext(getErrorString(R.string.text_error_register_code_not_valid));
            return;
        }
        if (pwd != null && pwd.length() < 6) {
            error.onNext(getErrorString(R.string.error_pwd_length));
            return;
        }
        if (!ValidUtil.pwdValid(pwd)) {
            error.onNext(getErrorString(R.string.error_invalid_password));
            return;
        }
        submitRequest(UserModel.register(phone, pwd, code, recommendCode),
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


    public Action1<String> setPhone() {
        return s -> {
            this.phone = s;
            setDataValid();
        };
    }

    public Action1<String> setCode() {
        return s -> {
            this.code = s;
            setDataValid();
        };
    }

    public Action1<String> setPwd() {
        return s -> {
            this.pwd = s;
            setDataValid();
        };
    }

    public Action1<String> setRecommendCode() {
        return s -> {
            this.recommendCode = s;
        };
    }

    private void setDataValid() {
        boolean isV = !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code) && !TextUtils.isEmpty(pwd);
        dataValid.onNext(isV);
    }

    public Observable<Boolean> getDataValid() {
        return dataValid;
    }
}

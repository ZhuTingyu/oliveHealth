package com.warehourse.app.ui.password;

import android.text.TextUtils;

import com.biz.base.BaseActivity;
import com.biz.base.BaseViewModel;
import com.biz.util.ValidUtil;
import com.warehourse.app.R;
import com.warehourse.app.model.SmsModel;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by wangwei on 2016/3/19.
 */
class FindPasswordViewModel extends BaseViewModel {
    private String phone;
    private String code;
    public static final String KEY_FORGOT_PWD_PHONE="KEY_FORGOT_PWD_PHONE";
    public static final String KEY_FORGOT_PWD_CODE="KEY_FORGOT_PWD_CODE";
    private final BehaviorSubject<Boolean> datValid = BehaviorSubject.create();

    public FindPasswordViewModel(Object activity) {
        super(activity);
    }

    public void sendCode(Action1<Boolean> onNext) {

        if(phone!=null)
        {
            phone=phone.trim();
        }
        if (!ValidUtil.phoneNumberValid(phone)) {
            error.onNext(getErrorString(R.string.text_error_register_phone_not_valid));
            return;
        }
        submitRequest(SmsModel.forgotPasswordSendSms(phone), r -> {
            if (r.isOk()) {
                Observable.just(true).subscribe(onNext);
            } else {
                error.onNext(getErrorString(r.msg));
            }
        }, throwable -> {
            error.onNext(getError(throwable));
        });
    }

    public void smsValidate(Action1<Boolean> onNext) {
        if(phone!=null)
        {
            phone=phone.trim();
        }
        if(code!=null)
        {
            code=code.trim();
        }
        if (!ValidUtil.phoneNumberValid(phone)) {
            error.onNext(getErrorString(R.string.text_error_register_phone_not_valid));
            return;
        }
        submitRequest(SmsModel.forgotPasswordSmsValidate(phone, code),
                r -> {
                    if (r.isOk()) {
                        BaseActivity baseActivity=getActivity();
                        if(null!=baseActivity)
                        {
                            baseActivity.getIntent().putExtra(KEY_FORGOT_PWD_PHONE,phone);
                            baseActivity.getIntent().putExtra(KEY_FORGOT_PWD_CODE,code);
                        }
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



    private void setDataValid() {
        boolean isV = !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code);
        datValid.onNext(isV);
    }

    public Observable<Boolean> getDatValid() {
        return datValid;
    }
}

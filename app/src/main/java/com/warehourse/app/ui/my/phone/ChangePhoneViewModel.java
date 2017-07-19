package com.warehourse.app.ui.my.phone;


import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.ValidUtil;
import com.warehourse.app.R;
import com.warehourse.app.event.UserEvent;
import com.warehourse.app.model.SmsModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.UserEntity;

import android.text.TextUtils;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by wangwei on 2016/3/19.
 */
class ChangePhoneViewModel extends BaseViewModel {
    private String phone;
    private String code;
    private final BehaviorSubject<Boolean> datValid = BehaviorSubject.create();

    public ChangePhoneViewModel(Object activity) {
        super(activity);
    }

    public void sendCode(Action1<Boolean> onNext) {

        if (!ValidUtil.phoneNumberValid(phone)) {
            error.onNext(getErrorString(R.string.text_error_register_phone_not_valid));
            return;
        }
        submitRequestThrowError(SmsModel.changeMobileSendSms(phone).map(r -> {
            if (r.isOk()) {
                return true;
            } else throw new HttpErrorException(r);
        }), onNext);
    }

    public void smsValidate(Action1<Boolean> onNext) {
        if (phone != null) {
            phone = phone.trim();
        }
        if (code != null) {
            code = code.trim();
        }
        if (!ValidUtil.phoneNumberValid(phone)) {
            error.onNext(getErrorString(R.string.text_error_register_phone_not_valid));
            return;
        }
        submitRequestThrowError(UserModel.changeMobile(phone, code).map(r -> {
            if (r.isOk()) {
                return true;
            } else throw new HttpErrorException(r);
        }), onNext);
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

package com.warehourse.app.ui.my.user;

import com.biz.http.HttpErrorException;
import com.warehourse.app.event.UserEvent;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.UserEntity;
import com.warehourse.app.ui.base.BaseUploadImageViewModel;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.functions.Action1;

/**
 * Title: UserInfoViewModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/24  15:40
 *
 * @author wangwei
 * @version 1.0
 */
public class UserInfoViewModel extends BaseUploadImageViewModel {
    public UserInfoViewModel(Object activity) {
        super(activity);
    }

    public void upload(String src, Action1<String> onNext) {
        super.upload(SystemModel.getInstance().getOssPublicBucketName(), src, onNext);
    }

    public void changeAvatar(String avatar, Action1<Boolean> onNext) {
        submitRequestThrowError(UserModel.changeAvatar(avatar).map(r -> {
            if (r.isOk()) {
                return true;
            } else throw new HttpErrorException(r);
        }), onNext);
    }
}

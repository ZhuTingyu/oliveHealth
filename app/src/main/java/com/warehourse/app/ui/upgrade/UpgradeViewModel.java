package com.warehourse.app.ui.upgrade;

import com.biz.base.BaseViewModel;
import com.warehourse.app.model.AvgModel;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.UpgradeEntity;

import rx.functions.Action1;


/**
 * Title: UpgradeViewModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2016/9/18  16:02
 *
 * @author wangwei
 * @version 1.0
 */
public class UpgradeViewModel extends BaseViewModel {
    public UpgradeViewModel(Object activity) {
        super(activity);
    }

    public void update(Action1<Boolean> action1) {
        submitRequest(SystemModel.upgrade(), action1, throwable -> {
        });
        submitRequest(SystemModel.init(), i -> {
        }, throwable -> {
        });
        submitRequest(AvgModel.initAvg(),r->{},throwable -> {});
        if(UserModel.getInstance().isLogin()) {
            submitRequest(UserModel.autoLogin(), r -> {
            }, throwable -> {
            });
        }
    }


    public void onResume(Action1<UpgradeEntity> onNext) {
        submitRequest(SystemModel.getUpgrade(), onNext, throwable -> {
        });
    }

    public void cancel() {
        submitRequest(SystemModel.cancelUpgrade(), b -> {
        }, throwable -> {
        });
    }
}

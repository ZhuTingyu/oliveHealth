package com.warehourse.app.ui.home;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.warehourse.app.model.HomeModel;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.entity.HomeEntity;

import java.util.List;

import rx.functions.Action1;

/**
 * Title: HomeViewModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/22  16:20
 *
 * @author wangwei
 * @version 1.0
 */
class HomeViewModel extends BaseViewModel {
    public HomeViewModel(Object activity) {
        super(activity);
    }

    public void request(Action1<List<HomeEntity>> onNext) {
        submitRequestThrowError(HomeModel.home().map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), onNext);
    }
    public void showNotice(Action1<Boolean> onNext){
        submitRequestThrowError(SystemModel.isShowNotice(),onNext);
    }
}

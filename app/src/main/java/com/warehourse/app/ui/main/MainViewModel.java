package com.warehourse.app.ui.main;

import com.warehourse.app.event.CartCountEvent;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.ui.cart.BaseAddCartViewModel;

import de.greenrobot.event.EventBus;
import rx.functions.Action1;

/**
 * Title: MainViewModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/27  17:18
 *
 * @author wangwei
 * @version 1.0
 */
public class MainViewModel extends BaseAddCartViewModel {
    public MainViewModel(Object activity) {
        super(activity);
    }

    @Override
    public void queryCartCount() {
        if(UserModel.getInstance().isLogin()){
            EventBus.getDefault().post(new CartCountEvent(UserModel.getInstance().getUserEntity().purchaseCount));
        }
        super.queryCartCount();
    }
}

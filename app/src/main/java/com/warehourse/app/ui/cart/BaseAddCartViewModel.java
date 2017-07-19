package com.warehourse.app.ui.cart;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.warehourse.app.model.CartModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.CartEntity;

import rx.functions.Action1;

/**
 * Title: BaseAddCartViewModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/27  10:59
 *
 * @author wangwei
 * @version 1.0
 */
public class BaseAddCartViewModel extends BaseViewModel{
    public BaseAddCartViewModel(Object activity) {
        super(activity);
    }

    public void addCart(String productId, int count, Action1<CartEntity> onNext)
    {
        submitRequestThrowError(CartModel.add(productId,count).map(r->{
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),onNext);
    }

    public void queryCartCount()
    {
        if(UserModel.getInstance().isLogin()) {
            submitRequestThrowError(CartModel.getCartCount().map(r -> {
                if (r.isOk()) {
                    return r.data;
                } else throw new HttpErrorException(r);
            }), count->{

            });
        }
    }
}

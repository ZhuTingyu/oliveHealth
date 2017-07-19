package com.warehourse.app.ui.cart;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.Lists;
import com.warehourse.app.model.CartModel;
import com.warehourse.app.model.entity.CartEntity;

import java.util.List;

import rx.functions.Action1;

/**
 * Title: CartViewModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/26  15:56
 *
 * @author wangwei
 * @version 1.0
 */
public class CartViewModel extends BaseViewModel {
    public CartViewModel(Object activity) {
        super(activity);
    }
    public void getCartList(Action1<CartEntity> onNext){
        submitRequestThrowError(CartModel.list().map(r->{
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),onNext);
    }
    public void deleteCart(String productId,Action1<CartEntity> onNext)
    {
        submitRequestThrowError(CartModel.delete(Lists.newArrayList(productId)).map(r->{
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),onNext);
    }
    public void deleteCart(List<String> listIds, Action1<CartEntity> onNext)
    {
        submitRequestThrowError(CartModel.delete(listIds).map(r->{
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),onNext);
    }
    public void updateCart(String productId,int count,Action1<CartEntity> onNext)
    {
        submitRequestThrowError(CartModel.update(productId,count).map(r->{
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),onNext);
    }
}

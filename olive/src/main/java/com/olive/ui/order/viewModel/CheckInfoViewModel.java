package com.olive.ui.order.viewModel;

import android.os.SystemClock;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.olive.model.AccountModel;
import com.olive.model.OrderModel;
import com.olive.model.UserModel;
import com.olive.model.entity.AccountEntity;
import com.olive.model.entity.OrderEntity;
import com.olive.model.entity.ProductEntity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/6.
 */

public class CheckInfoViewModel extends BaseViewModel {

    private List<ProductEntity> productEntities;
    private long totalPrice;
    private int addressId;
    private AccountEntity accountEntity;

    public CheckInfoViewModel(Object activity) {
        super(activity);
        productEntities = getActivity().getIntent().getParcelableArrayListExtra(IntentBuilder.KEY_DATA);
        totalPrice = getActivity().getIntent().getLongExtra(IntentBuilder.KEY_VALUE,0);
    }

    public void createOrder(Action1<OrderEntity> action1){
        submitRequestThrowError(OrderModel.createOrder(productEntities, addressId, getCheckNumber()).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }), action1);
    }

    public void getAccountInfo(Action1<AccountEntity> action1){
        submitRequestThrowError(AccountModel.account().map(r -> {
            if(r.isOk()){
                accountEntity = r.data;
                return accountEntity;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public int getTotalCount(){
        int count = 0;
        for(ProductEntity productEntity : productEntities){
            count += productEntity.quantity;
        }
        return count;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCheckNumber() {
        return UserModel.getInstance().getToken() + System.currentTimeMillis();
    }

    public boolean isHasDebt(){
        return accountEntity.debt > 0;
    }
}

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

public class CheckInfoViewModel extends OrderViewModel {

    private List<ProductEntity> productEntities;
    private long totalPrice;
    private AccountEntity accountEntity;

    public CheckInfoViewModel(Object activity) {
        super(activity);
        productEntities = getActivity().getIntent().getParcelableArrayListExtra(IntentBuilder.KEY_DATA);
        totalPrice = getActivity().getIntent().getLongExtra(IntentBuilder.KEY_VALUE,0);
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




    public boolean isHasDebt(){
        return accountEntity.debt > 0;
    }
}

package com.olive.ui.main.my.account.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.model.AccountModel;
import com.olive.model.entity.AccountEntity;
import com.olive.ui.BaseLoadMoreViewModel;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/9.
 */

public class AccountViewModel extends BaseViewModel {

    public AccountEntity accountEntity;

    public AccountViewModel(Object activity) {
        super(activity);
    }

    public void getAccountInfo(Action1<AccountEntity> action1){
        submitRequestThrowError(AccountModel.account().map(r -> {
            if (r.isOk()){
                accountEntity = r.data;
                return accountEntity;
            }else throw new HttpErrorException(r);
        }),action1);
    }

}

package com.olive.ui.account.viewModel;

import com.biz.http.HttpErrorException;
import com.olive.model.AccountModel;
import com.olive.model.entity.AccountEntity;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/9.
 */

public class AccountViewModel extends ConsumeViewModel {

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

package com.olive.model;

import com.biz.http.ResponseJson;
import com.google.gson.reflect.TypeToken;
import com.olive.R;
import com.olive.model.entity.AccountEntity;
import com.olive.model.entity.BankEntity;
import com.olive.model.entity.OrderEntity;
import com.olive.model.entity.ProductEntity;
import com.olive.util.HttpRequest;

import java.util.List;

import rx.Observable;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class AccountModel {

    public static Observable<ResponseJson<AccountEntity>> account(){
        return HttpRequest.<ResponseJson<AccountEntity>>builder()
                .setToJsonType(new TypeToken<ResponseJson<AccountEntity>>() {
                }.getType())
                .url(R.string.api_account_info)
                .requestPI();
    }

    public static Observable<ResponseJson<List<BankEntity>>> bankCards(){
        return HttpRequest.<ResponseJson<List<BankEntity>>>builder()
                .setToJsonType(new TypeToken<ResponseJson<List<BankEntity>>>() {
                }.getType())
                .url(R.string.api_account_bank_cards)
                .requestPI();
    }

}

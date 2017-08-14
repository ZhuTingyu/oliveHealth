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

    public static Observable<ResponseJson<String>> changePassword(String mobile, String authCode, String password, String newPassword){
        return HttpRequest.<ResponseJson<String>>builder()
                .setToJsonType(new TypeToken<ResponseJson<String>>() {
                }.getType())
                .addBody("mobile", mobile)
                .addBody("authCode", authCode)
                .addBody("password", password)
                .addBody("newPassword", newPassword)
                .url(R.string.api_account_change_password)
                .requestPI();
    }

    public static Observable<ResponseJson<String>> sendCode(String mobile, int type){
        return HttpRequest.<ResponseJson<String>>builder()
                .setToJsonType(new TypeToken<ResponseJson<String>>() {
                }.getType())
                .addBody("mobile", mobile)
                .addBody("type", type)
                .url(R.string.api_account_send_code)
                .requestPI();
    }

    public static Observable<ResponseJson<String>> validateCode(String mobile, String authCode, int type){
        return HttpRequest.<ResponseJson<String>>builder()
                .setToJsonType(new TypeToken<ResponseJson<String>>() {
                }.getType())
                .addBody("mobile", mobile)
                .addBody("authCode", authCode)
                .addBody("type", type)
                .url(R.string.api_account_change_password)
                .requestPI();
    }

    public static Observable<ResponseJson<String>> resetPassword(String mobile, String authCode, String newPassword){
        return HttpRequest.<ResponseJson<String>>builder()
                .setToJsonType(new TypeToken<ResponseJson<String>>() {
                }.getType())
                .addBody("mobile", mobile)
                .addBody("authCode", authCode)
                .addBody("newPassword", newPassword)
                .url(R.string.api_account_reset_password)
                .requestPI();
    }

}
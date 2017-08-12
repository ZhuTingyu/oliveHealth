package com.olive.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TingYu Zhu on 2017/7/26.
 */

public class UserEntity implements Parcelable {
    public String userId;   //用户ID
    public String userNo; //用户编码
    public String nickname;   //昵称
    public String mobile;   //手机号码
    public String account;   //商城登录账号
    public String token;   //验证token
    public String password;


    public UserEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.userNo);
        dest.writeString(this.nickname);
        dest.writeString(this.mobile);
        dest.writeString(this.account);
        dest.writeString(this.token);
        dest.writeString(this.password);
    }

    protected UserEntity(Parcel in) {
        this.userId = in.readString();
        this.userNo = in.readString();
        this.nickname = in.readString();
        this.mobile = in.readString();
        this.account = in.readString();
        this.token = in.readString();
        this.password = in.readString();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel source) {
            return new UserEntity(source);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };
}

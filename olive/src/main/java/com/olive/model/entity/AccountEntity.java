package com.olive.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class AccountEntity implements Parcelable {
    public long balance;   //余额（分）
    public long consumeAmount;  //总消费金额（分）
    public long debt;//欠款（分）

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.balance);
        dest.writeLong(this.consumeAmount);
        dest.writeLong(this.debt);
    }

    public AccountEntity() {
    }

    protected AccountEntity(Parcel in) {
        this.balance = in.readLong();
        this.consumeAmount = in.readLong();
        this.debt = in.readLong();
    }

    public static final Parcelable.Creator<AccountEntity> CREATOR = new Parcelable.Creator<AccountEntity>() {
        @Override
        public AccountEntity createFromParcel(Parcel source) {
            return new AccountEntity(source);
        }

        @Override
        public AccountEntity[] newArray(int size) {
            return new AccountEntity[size];
        }
    };
}

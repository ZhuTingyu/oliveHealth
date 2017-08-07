package com.olive.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class OrderEntity implements Parcelable {
    public String orderNo;
    public long amount;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderNo);
        dest.writeLong(this.amount);
    }

    public OrderEntity() {
    }

    protected OrderEntity(Parcel in) {
        this.orderNo = in.readString();
        this.amount = in.readLong();
    }

    public static final Parcelable.Creator<OrderEntity> CREATOR = new Parcelable.Creator<OrderEntity>() {
        @Override
        public OrderEntity createFromParcel(Parcel source) {
            return new OrderEntity(source);
        }

        @Override
        public OrderEntity[] newArray(int size) {
            return new OrderEntity[size];
        }
    };
}

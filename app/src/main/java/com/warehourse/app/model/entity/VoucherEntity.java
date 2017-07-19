package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangwei on 2016/3/23.
 */
public class VoucherEntity implements Parcelable {
    public long voucherTypeId;
    public String name;
    public long faceValue;
    public long paymentLimit;
    public String payPatternLimit;
    public int voucherPattern;
    public long categoryId;
    public int typeStatus;
    public long startTime;
    public long expireTime;
    public String categoryInfo;
    public int quantity;
    public int maxCount;
    public String freeMsg;
    public int remainAmount;
    public String productIds;
    public boolean isSelected;

    public VoucherEntity() {
    }


    protected VoucherEntity(Parcel in) {
        voucherTypeId = in.readLong();
        name = in.readString();
        faceValue = in.readLong();
        paymentLimit = in.readLong();
        payPatternLimit = in.readString();
        voucherPattern = in.readInt();
        categoryId = in.readLong();
        typeStatus = in.readInt();
        startTime = in.readLong();
        expireTime = in.readLong();
        categoryInfo = in.readString();
        quantity = in.readInt();
        maxCount = in.readInt();
        freeMsg = in.readString();
        remainAmount = in.readInt();
        productIds=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(voucherTypeId);
        dest.writeString(name);
        dest.writeLong(faceValue);
        dest.writeLong(paymentLimit);
        dest.writeString(payPatternLimit);
        dest.writeInt(voucherPattern);
        dest.writeLong(categoryId);
        dest.writeInt(typeStatus);
        dest.writeLong(startTime);
        dest.writeLong(expireTime);
        dest.writeString(categoryInfo);
        dest.writeInt(quantity);
        dest.writeInt(maxCount);
        dest.writeString(freeMsg);
        dest.writeInt(remainAmount);
        dest.writeString(productIds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VoucherEntity> CREATOR = new Creator<VoucherEntity>() {
        @Override
        public VoucherEntity createFromParcel(Parcel in) {
            return new VoucherEntity(in);
        }

        @Override
        public VoucherEntity[] newArray(int size) {
            return new VoucherEntity[size];
        }
    };
}

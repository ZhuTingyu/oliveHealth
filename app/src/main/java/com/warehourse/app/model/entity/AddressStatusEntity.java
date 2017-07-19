package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressStatusEntity implements Parcelable {
    public int provinceId;
    public String provinceName;
    public int cityId;
    public String cityName;
    public int districtId;
    public String districtName;
    public String deliveryAddress;
    public int auditStatus;
    public String reason;
    public String rejectReasons;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.provinceId);
        dest.writeString(this.provinceName);
        dest.writeInt(this.cityId);
        dest.writeString(this.cityName);
        dest.writeInt(this.districtId);
        dest.writeString(this.districtName);
        dest.writeString(this.deliveryAddress);
        dest.writeInt(this.auditStatus);
        dest.writeString(this.reason);
        dest.writeString(this.rejectReasons);
    }

    public AddressStatusEntity() {
    }

    protected AddressStatusEntity(Parcel in) {
        this.provinceId = in.readInt();
        this.provinceName = in.readString();
        this.cityId = in.readInt();
        this.cityName = in.readString();
        this.districtId = in.readInt();
        this.districtName = in.readString();
        this.deliveryAddress = in.readString();
        this.auditStatus = in.readInt();
        this.reason = in.readString();
        this.rejectReasons = in.readString();
    }

    public static final Parcelable.Creator<AddressStatusEntity> CREATOR = new Parcelable.Creator<AddressStatusEntity>() {
        @Override
        public AddressStatusEntity createFromParcel(Parcel source) {
            return new AddressStatusEntity(source);
        }

        @Override
        public AddressStatusEntity[] newArray(int size) {
            return new AddressStatusEntity[size];
        }
    };
}
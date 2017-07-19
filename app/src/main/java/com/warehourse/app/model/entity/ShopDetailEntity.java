package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ShopDetailEntity implements Parcelable {
    public String shopId;
    public String name;
    public long shopTypeId;
    public String shopTypeName;
    public String deliveryAddress;
    public int provinceId;
    public String provinceName;
    public int cityId;
    public String cityName;
    public int districtId;
    public String districtName;
    public List<String> rejectReasons;
    public int auditStatus;
    public String corporateName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopId);
        dest.writeString(this.name);
        dest.writeLong(this.shopTypeId);
        dest.writeString(this.shopTypeName);
        dest.writeString(this.deliveryAddress);
        dest.writeInt(this.provinceId);
        dest.writeString(this.provinceName);
        dest.writeInt(this.cityId);
        dest.writeString(this.cityName);
        dest.writeInt(this.districtId);
        dest.writeString(this.districtName);
        dest.writeStringList(this.rejectReasons);
        dest.writeInt(this.auditStatus);
        dest.writeString(this.corporateName);
    }

    public ShopDetailEntity() {
    }

    protected ShopDetailEntity(Parcel in) {
        this.shopId = in.readString();
        this.name = in.readString();
        this.shopTypeId = in.readLong();
        this.shopTypeName = in.readString();
        this.deliveryAddress = in.readString();
        this.provinceId = in.readInt();
        this.provinceName = in.readString();
        this.cityId = in.readInt();
        this.cityName = in.readString();
        this.districtId = in.readInt();
        this.districtName = in.readString();
        this.rejectReasons = in.createStringArrayList();
        this.auditStatus = in.readInt();
        this.corporateName = in.readString();
    }

    public static final Creator<ShopDetailEntity> CREATOR = new Creator<ShopDetailEntity>() {
        @Override
        public ShopDetailEntity createFromParcel(Parcel source) {
            return new ShopDetailEntity(source);
        }

        @Override
        public ShopDetailEntity[] newArray(int size) {
            return new ShopDetailEntity[size];
        }
    };
}
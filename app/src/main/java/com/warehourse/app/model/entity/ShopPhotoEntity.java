package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ShopPhotoEntity implements Parcelable {
    public String shopId;
    public String name;
    public String businessLicence;
    public String shopPhoto;
    public String liquorSellLicence;
    public String corporateIdPhoto;
    public int auditStatus;
    public List<String> rejectReasons;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopId);
        dest.writeString(this.name);
        dest.writeString(this.businessLicence);
        dest.writeString(this.shopPhoto);
        dest.writeString(this.liquorSellLicence);
        dest.writeString(this.corporateIdPhoto);
        dest.writeInt(this.auditStatus);
        dest.writeStringList(this.rejectReasons);
    }

    public ShopPhotoEntity() {
    }

    protected ShopPhotoEntity(Parcel in) {
        this.shopId = in.readString();
        this.name = in.readString();
        this.businessLicence = in.readString();
        this.shopPhoto = in.readString();
        this.liquorSellLicence = in.readString();
        this.corporateIdPhoto = in.readString();
        this.auditStatus = in.readInt();
        this.rejectReasons = in.createStringArrayList();
    }

    public static final Creator<ShopPhotoEntity> CREATOR = new Creator<ShopPhotoEntity>() {
        @Override
        public ShopPhotoEntity createFromParcel(Parcel source) {
            return new ShopPhotoEntity(source);
        }

        @Override
        public ShopPhotoEntity[] newArray(int size) {
            return new ShopPhotoEntity[size];
        }
    };
}
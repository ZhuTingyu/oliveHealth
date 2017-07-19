package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class ShopTypeEntity implements Parcelable {
    public long id;
    public String name;
    public String description;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
    }

    public ShopTypeEntity() {
    }

    protected ShopTypeEntity(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
    }

    public static final Creator<ShopTypeEntity> CREATOR = new Creator<ShopTypeEntity>() {
        @Override
        public ShopTypeEntity createFromParcel(Parcel source) {
            return new ShopTypeEntity(source);
        }

        @Override
        public ShopTypeEntity[] newArray(int size) {
            return new ShopTypeEntity[size];
        }
    };
}

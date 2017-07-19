package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Title: SalePromotionEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/22  10:25
 *
 * @author wangwei
 * @version 1.0
 */
public class SalePromotionEntity implements Parcelable {
    public String id;
    public String name;
    public String description;
    public boolean available;
    public boolean useCoupon;

    public SalePromotionEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeByte(this.available ? (byte) 1 : (byte) 0);
        dest.writeByte(this.useCoupon ? (byte) 1 : (byte) 0);
    }

    protected SalePromotionEntity(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.available = in.readByte() != 0;
        this.useCoupon = in.readByte() != 0;
    }

    public static final Creator<SalePromotionEntity> CREATOR = new Creator<SalePromotionEntity>() {
        @Override
        public SalePromotionEntity createFromParcel(Parcel source) {
            return new SalePromotionEntity(source);
        }

        @Override
        public SalePromotionEntity[] newArray(int size) {
            return new SalePromotionEntity[size];
        }
    };
}

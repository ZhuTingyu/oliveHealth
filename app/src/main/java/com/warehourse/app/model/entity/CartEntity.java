package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Title: CartEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/26  14:09
 *
 * @author wangwei
 * @version 1.0
 */
public class CartEntity implements Parcelable {
    public List<ProductEntity> items;
    public int cartNum;
    public long orderAmount;
    public long freeAmount;
    public long voucherAmount;
    public long payAmount;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.items);
        dest.writeInt(this.cartNum);
        dest.writeLong(this.orderAmount);
        dest.writeLong(this.freeAmount);
        dest.writeLong(this.voucherAmount);
        dest.writeLong(this.payAmount);
    }

    public CartEntity() {
    }

    protected CartEntity(Parcel in) {
        this.items = in.createTypedArrayList(ProductEntity.CREATOR);
        this.cartNum = in.readInt();
        this.orderAmount = in.readLong();
        this.freeAmount = in.readLong();
        this.voucherAmount = in.readLong();
        this.payAmount = in.readLong();
    }

    public static final Parcelable.Creator<CartEntity> CREATOR = new Creator<CartEntity>() {
        @Override
        public CartEntity createFromParcel(Parcel source) {
            return new CartEntity(source);
        }

        @Override
        public CartEntity[] newArray(int size) {
            return new CartEntity[size];
        }
    };
}

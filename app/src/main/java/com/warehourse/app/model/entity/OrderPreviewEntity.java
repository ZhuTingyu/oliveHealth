package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Title: OrderPreviewEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/31  10:26
 *
 * @author wangwei
 * @version 1.0
 */
public class OrderPreviewEntity implements Parcelable {
    public long orderAmount;
    public long freeAmount;
    public long voucherAmount;
    public long freight;
    public long payAmount;
    public int coupons;
    public List<PaymentTypeEntity> paymentTypes;
    public boolean valid;
    public String buyerName;
    public String buyerMobile;
    public String buyerAddress;
    public List<ProductEntity> items;
    public List<SalePromotionEntity> salePromotionDetail;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.orderAmount);
        dest.writeLong(this.freeAmount);
        dest.writeLong(this.voucherAmount);
        dest.writeLong(this.freight);
        dest.writeLong(this.payAmount);
        dest.writeInt(this.coupons);
        dest.writeTypedList(this.paymentTypes);
        dest.writeByte(this.valid ? (byte) 1 : (byte) 0);
        dest.writeString(this.buyerName);
        dest.writeString(this.buyerMobile);
        dest.writeString(this.buyerAddress);
        dest.writeTypedList(this.items);
        dest.writeTypedList(this.salePromotionDetail);
    }

    public OrderPreviewEntity() {
    }

    protected OrderPreviewEntity(Parcel in) {
        this.orderAmount = in.readLong();
        this.freeAmount = in.readLong();
        this.voucherAmount = in.readLong();
        this.freight = in.readLong();
        this.payAmount = in.readLong();
        this.coupons = in.readInt();
        this.paymentTypes = in.createTypedArrayList(PaymentTypeEntity.CREATOR);
        this.valid = in.readByte() != 0;
        this.buyerName = in.readString();
        this.buyerMobile = in.readString();
        this.buyerAddress = in.readString();
        this.items = in.createTypedArrayList(ProductEntity.CREATOR);
        this.salePromotionDetail = in.createTypedArrayList(SalePromotionEntity.CREATOR);
    }

    public static final Parcelable.Creator<OrderPreviewEntity> CREATOR = new Parcelable.Creator<OrderPreviewEntity>() {
        @Override
        public OrderPreviewEntity createFromParcel(Parcel source) {
            return new OrderPreviewEntity(source);
        }

        @Override
        public OrderPreviewEntity[] newArray(int size) {
            return new OrderPreviewEntity[size];
        }
    };
}

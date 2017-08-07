package com.olive.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class OrderListEntity implements Parcelable {

    public String orderNo;//订单号
    public long amount;  //金额（分）
    public int payStatus; //支付状态 0：未支付，1：已支付
    public int expressStatus;  //物流状态 0：默认（未支付时为0），1：待发货，2：已发货，3：已签收
    public int orderStatus;//订单状态1：正常状态，-1：已取消
    public int allowDebt; //是否欠款：0：否，1：是
    public List<ProductEntity> products;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderNo);
        dest.writeLong(this.amount);
        dest.writeInt(this.payStatus);
        dest.writeInt(this.expressStatus);
        dest.writeInt(this.orderStatus);
        dest.writeInt(this.allowDebt);
        dest.writeTypedList(this.products);
    }

    public OrderListEntity() {
    }

    protected OrderListEntity(Parcel in) {
        this.orderNo = in.readString();
        this.amount = in.readLong();
        this.payStatus = in.readInt();
        this.expressStatus = in.readInt();
        this.orderStatus = in.readInt();
        this.allowDebt = in.readInt();
        this.products = in.createTypedArrayList(ProductEntity.CREATOR);
    }

    public static final Parcelable.Creator<OrderListEntity> CREATOR = new Parcelable.Creator<OrderListEntity>() {
        @Override
        public OrderListEntity createFromParcel(Parcel source) {
            return new OrderListEntity(source);
        }

        @Override
        public OrderListEntity[] newArray(int size) {
            return new OrderListEntity[size];
        }
    };
}

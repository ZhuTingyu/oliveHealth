package com.olive.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class OrderEntity implements Parcelable {

    public String orderNo;//订单号
    public long amount;  //金额（分）
    public int payStatus; //支付状态 0：未支付，1：已支付
    public int expressStatus;  //物流状态 0：默认（未支付时为0），1：待发货，2：已发货，3：已签收
    public int orderStatus;//订单状态1：正常状态，-1：已取消
    public int allowDebt; //是否欠款：0：否，1：是
    public List<ProductEntity> products;

    //详情
    public int useBalancePay;   //是否用余额支付，0：否，1：是
    public long balancePayAmount;    //余额支付金额
    public int payType; //支付方式  //1:支付宝，2：微信，3：中国银行，4：中国建设银行，5：中国工商银行，6：中国农业银行
    public long payTime; //支付时间
    public String expressNo;    //物流单号
    public String expressInfo; //物流信息
    public long expressTime;  //发货时间
    public String outTradeNo;   //交易编号
    public long createTime;   //创建时间
    public String consigneeName;   //收货人
    public String mobile;//手机号
    public String address; //收货地址


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

    public OrderEntity() {
    }

    protected OrderEntity(Parcel in) {
        this.orderNo = in.readString();
        this.amount = in.readLong();
        this.payStatus = in.readInt();
        this.expressStatus = in.readInt();
        this.orderStatus = in.readInt();
        this.allowDebt = in.readInt();
        this.products = in.createTypedArrayList(ProductEntity.CREATOR);
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

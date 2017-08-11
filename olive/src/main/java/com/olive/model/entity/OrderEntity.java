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
    public long orderTime;//订单时间
    public String consigneeName;   //收货人
    public String mobile;//手机号
    public String address; //收货地址

    //明细
    public int type;

    //退货
    public int status;
    public long orderDate; //退货时间
    public String note;
    //退货申请详情
    public String approveDes;   //审核备注信息
    public String image;   //图片
    public String description;   //退货描述
    public String reason;   //退货理由


    public OrderEntity() {
    }

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
        dest.writeInt(this.useBalancePay);
        dest.writeLong(this.balancePayAmount);
        dest.writeInt(this.payType);
        dest.writeLong(this.payTime);
        dest.writeString(this.expressNo);
        dest.writeString(this.expressInfo);
        dest.writeLong(this.expressTime);
        dest.writeString(this.outTradeNo);
        dest.writeLong(this.createTime);
        dest.writeLong(this.orderTime);
        dest.writeString(this.consigneeName);
        dest.writeString(this.mobile);
        dest.writeString(this.address);
        dest.writeInt(this.type);
        dest.writeInt(this.status);
        dest.writeLong(this.orderDate);
        dest.writeString(this.note);
        dest.writeString(this.approveDes);
        dest.writeString(this.image);
        dest.writeString(this.description);
        dest.writeString(this.reason);
    }

    protected OrderEntity(Parcel in) {
        this.orderNo = in.readString();
        this.amount = in.readLong();
        this.payStatus = in.readInt();
        this.expressStatus = in.readInt();
        this.orderStatus = in.readInt();
        this.allowDebt = in.readInt();
        this.products = in.createTypedArrayList(ProductEntity.CREATOR);
        this.useBalancePay = in.readInt();
        this.balancePayAmount = in.readLong();
        this.payType = in.readInt();
        this.payTime = in.readLong();
        this.expressNo = in.readString();
        this.expressInfo = in.readString();
        this.expressTime = in.readLong();
        this.outTradeNo = in.readString();
        this.createTime = in.readLong();
        this.orderTime = in.readLong();
        this.consigneeName = in.readString();
        this.mobile = in.readString();
        this.address = in.readString();
        this.type = in.readInt();
        this.status = in.readInt();
        this.orderDate = in.readLong();
        this.note = in.readString();
        this.approveDes = in.readString();
        this.image = in.readString();
        this.description = in.readString();
        this.reason = in.readString();
    }

    public static final Creator<OrderEntity> CREATOR = new Creator<OrderEntity>() {
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

package com.warehourse.app.model.entity;

import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.warehourse.app.R;
import com.warehourse.app.application.WareApplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

/**
 * Title: OrderEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/26  15:21
 *
 * @author wangwei
 * @version 1.0
 */
public class OrderEntity implements Parcelable, MultiItemEntity {
    //0 全部订单;  1 已取消;  10 新创建; 20待支付；30待发货；40 待收货; 50 已完成；60 申请退货；65退货审核不通过；70退货中；80 退款完成
    public static final int STATUS_ALL = 0;
    public static final int STATUS_CANCEL = 1;
    public static final int STATUS_NEW = 10;
    public static final int STATUS_NO_PAY = 20;
    public static final int STATUS_WAIT_DELIVER = 30;
    public static final int STATUS_WAIT_RECEIPT_GOODS = 40;
    public static final int STATUS_COMPLETE = 50;
    public static final int STATUS_APPLY_RETURN = 60;
    public static final int STATUS_REFUSE_RETURN = 65;
    public static final int STATUS_RETURN = 70;
    public static final int STATUS_REFUND_COMPLETE = 80;

    public static final int PAY_DELIVER = 1;
    public static final int PAY_ALIPAY = 21;
    public static final int PAY_WEIXIN = 22;



    public static final int TYPE_SINGLE = 10;
    public static final int TYPE_MORE = 20;

    private int itemType;

    @Override
    public int getItemType() {
        itemType = getItems().size() == 1 ? TYPE_SINGLE : TYPE_MORE;
        return itemType;
    }

    public String getPayType() {
        int resId;
        switch (paymentType) {
            case PAY_DELIVER:
                resId = R.string.text_pay_way_cash;
                break;
            case PAY_ALIPAY:
                resId = R.string.text_pay_way_alipay;
                break;
            case PAY_WEIXIN:
                resId = R.string.text_pay_way_wechat;
                break;
            default:
                resId = R.string.text_pay_way_cash;

        }
        return WareApplication.getAppContext().getString(resId);
    }

    public String id;
    public String orderId;
    public String orderCode;
    public int status;
    public String statusDesc;
    public String statusMsg;
    public long createTime;
    public long orderAmount;
    public long payAmount;
    public long reeAmount;
    public long voucherAmount;
    public long payLimitTime;
    public int paymentType;
    public String buyerName;
    public String buyerMobile;
    public String buyerAddress;
    public boolean payable;
    public boolean cancelable;
    public boolean contactable;
    public boolean applyRefundable;
    public String description;
    public String hint;
    public int invoiceType;
    public String invoiceTitle;
    public String invoiceTaxId;

    public String getPayAmount() {
        int resId = R.string.text_order_list_price;
        if (status==STATUS_NO_PAY){
            resId = R.string.text_order_list_price_pay;
        } else if (status==STATUS_WAIT_DELIVER && paymentType == PAY_DELIVER){
            resId = R.string.text_order_list_price_should;
        }

        return WareApplication.getAppContext().getString(resId)+ PriceUtil.formatRMB(payAmount);
    }

    public String getPayAmountString() {
        int resId = R.string.text_order_list_price;
        if (status==STATUS_NO_PAY){
            resId = R.string.text_order_list_price_pay;
        } else if (status==STATUS_WAIT_DELIVER && paymentType == PAY_DELIVER){
            resId = R.string.text_order_list_price_should;
        }

        return WareApplication.getAppContext().getString(resId);
    }

    public String getInvoiceTitle() {
        return TextUtils.isEmpty(invoiceTitle)?WareApplication.getAppContext().getString(R.string.text_none):invoiceTitle;
    }

    public String getInvoiceTaxId() {
        return TextUtils.isEmpty(invoiceTaxId)?WareApplication.getAppContext().getString(R.string.text_none):invoiceTaxId;
    }

    public List<ProductEntity> items;

    public List<ProductEntity> getItems() {
        return items == null ? Lists.newArrayList() : items;
    }

    public ProductEntity getFirstItem() {
        return items.get(0);
    }

    public String getOrderId() {
        if(!TextUtils.isEmpty(id)) return id;
        return orderId;
    }

    public OrderEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.itemType);
        dest.writeString(this.id);
        dest.writeString(this.orderId);
        dest.writeString(this.orderCode);
        dest.writeInt(this.status);
        dest.writeString(this.statusDesc);
        dest.writeString(this.statusMsg);
        dest.writeLong(this.createTime);
        dest.writeLong(this.orderAmount);
        dest.writeLong(this.payAmount);
        dest.writeLong(this.reeAmount);
        dest.writeLong(this.voucherAmount);
        dest.writeLong(this.payLimitTime);
        dest.writeInt(this.paymentType);
        dest.writeString(this.buyerName);
        dest.writeString(this.buyerMobile);
        dest.writeString(this.buyerAddress);
        dest.writeByte(this.payable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.cancelable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.contactable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.applyRefundable ? (byte) 1 : (byte) 0);
        dest.writeString(this.description);
        dest.writeString(this.hint);
        dest.writeInt(this.invoiceType);
        dest.writeString(this.invoiceTitle);
        dest.writeTypedList(this.items);
    }

    protected OrderEntity(Parcel in) {
        this.itemType = in.readInt();
        this.id = in.readString();
        this.orderId = in.readString();
        this.orderCode = in.readString();
        this.status = in.readInt();
        this.statusDesc = in.readString();
        this.statusMsg = in.readString();
        this.createTime = in.readLong();
        this.orderAmount = in.readLong();
        this.payAmount = in.readLong();
        this.reeAmount = in.readLong();
        this.voucherAmount = in.readLong();
        this.payLimitTime = in.readLong();
        this.paymentType = in.readInt();
        this.buyerName = in.readString();
        this.buyerMobile = in.readString();
        this.buyerAddress = in.readString();
        this.payable = in.readByte() != 0;
        this.cancelable = in.readByte() != 0;
        this.contactable = in.readByte() != 0;
        this.applyRefundable = in.readByte() != 0;
        this.description = in.readString();
        this.hint = in.readString();
        this.invoiceType = in.readInt();
        this.invoiceTitle = in.readString();
        this.items = in.createTypedArrayList(ProductEntity.CREATOR);
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

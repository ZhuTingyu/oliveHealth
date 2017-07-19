package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Title: UserEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/16  10:25
 *
 * @author wangwei
 * @version 1.0
 */
public class UserEntity implements Parcelable{
    public String userId;
    public String shopId;
    public String name;
    public String mobile;
    public String avatar;
    public boolean isAdmin;
    public String deliveryAddress;
    public int detailAuditStatus;
    public List<String> detailRejectReasons;
    public int qualificationAuditStatus;
    public List<String> qualificationRejectReasons;
    public int shopStatus;
    public int userStatus;
    public int msgCount;
    public String corporateName;
    public String deliveryName;
    public boolean showPaymentButton;
    public int luckMoneyCount;
    public boolean showActivityRedPoint;
    public boolean emptyPaymentPassword;
    public String detailAddress;
    public int purchaseCount;
    public String csh;

    public String getUserId() {
        if(userId==null) return "";
        return userId;
    }
    public String getShopId() {
        if(shopId==null) return "";
        return shopId;
    }

    public UserEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.shopId);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.avatar);
        dest.writeByte(this.isAdmin ? (byte) 1 : (byte) 0);
        dest.writeString(this.deliveryAddress);
        dest.writeInt(this.detailAuditStatus);
        dest.writeStringList(this.detailRejectReasons);
        dest.writeInt(this.qualificationAuditStatus);
        dest.writeStringList(this.qualificationRejectReasons);
        dest.writeInt(this.shopStatus);
        dest.writeInt(this.userStatus);
        dest.writeInt(this.msgCount);
        dest.writeString(this.corporateName);
        dest.writeString(this.deliveryName);
        dest.writeByte(this.showPaymentButton ? (byte) 1 : (byte) 0);
        dest.writeInt(this.luckMoneyCount);
        dest.writeByte(this.showActivityRedPoint ? (byte) 1 : (byte) 0);
        dest.writeByte(this.emptyPaymentPassword ? (byte) 1 : (byte) 0);
        dest.writeString(this.detailAddress);
        dest.writeInt(this.purchaseCount);
        dest.writeString(csh);
    }

    protected UserEntity(Parcel in) {
        this.userId = in.readString();
        this.shopId = in.readString();
        this.name = in.readString();
        this.mobile = in.readString();
        this.avatar = in.readString();
        this.isAdmin = in.readByte() != 0;
        this.deliveryAddress = in.readString();
        this.detailAuditStatus = in.readInt();
        this.detailRejectReasons = in.createStringArrayList();
        this.qualificationAuditStatus = in.readInt();
        this.qualificationRejectReasons = in.createStringArrayList();
        this.shopStatus = in.readInt();
        this.userStatus = in.readInt();
        this.msgCount = in.readInt();
        this.corporateName = in.readString();
        this.deliveryName = in.readString();
        this.showPaymentButton = in.readByte() != 0;
        this.luckMoneyCount = in.readInt();
        this.showActivityRedPoint = in.readByte() != 0;
        this.emptyPaymentPassword = in.readByte() != 0;
        this.detailAddress = in.readString();
        this.purchaseCount = in.readInt();
        this.csh=in.readString();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel source) {
            return new UserEntity(source);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };
}

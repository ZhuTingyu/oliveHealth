package com.warehourse.app.model.entity;

import com.biz.application.BaseApplication;
import com.warehourse.app.R;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Title: PaymentTypeEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/31  10:27
 *
 * @author wangwei
 * @version 1.0
 */
public class PaymentTypeEntity implements Parcelable {
    public int paymentId;
    public long balance;

    public String getPayName(){
        if(paymentId==OrderPreviewParaEntity.TYPE_PAY_ALIPAY){
            return BaseApplication.getAppContext().getString(R.string.text_pay_way_alipay);
        }else if(paymentId==OrderPreviewParaEntity.TYPE_PAY_WEIXIN){
            return BaseApplication.getAppContext().getString(R.string.text_pay_way_wechat);
        }else if(paymentId==OrderPreviewParaEntity.TYPE_PAY_DELIVER){
            return BaseApplication.getAppContext().getString(R.string.text_pay_way_cash);
        }else {
            return BaseApplication.getAppContext().getString(R.string.text_pay_way_unknown);
        }
    }
    public boolean isAlipay(){
        return paymentId==OrderPreviewParaEntity.TYPE_PAY_ALIPAY;
    }
    public boolean isWechaty(){
        return paymentId==OrderPreviewParaEntity.TYPE_PAY_WEIXIN;
    }
    public boolean isDeliver(){
        return paymentId==OrderPreviewParaEntity.TYPE_PAY_DELIVER;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.paymentId);
        dest.writeLong(this.balance);
    }

    public PaymentTypeEntity() {
    }

    protected PaymentTypeEntity(Parcel in) {
        this.paymentId = in.readInt();
        this.balance = in.readLong();
    }

    public static final Parcelable.Creator<PaymentTypeEntity> CREATOR = new Parcelable.Creator<PaymentTypeEntity>() {
        @Override
        public PaymentTypeEntity createFromParcel(Parcel source) {
            return new PaymentTypeEntity(source);
        }

        @Override
        public PaymentTypeEntity[] newArray(int size) {
            return new PaymentTypeEntity[size];
        }
    };
}

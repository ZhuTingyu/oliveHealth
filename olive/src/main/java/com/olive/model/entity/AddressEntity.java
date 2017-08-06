package com.olive.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TingYu Zhu on 2017/8/6.
 */

public class AddressEntity implements Parcelable {
    public int id;//地址ID
    public String consignee;   //收货人
    public String mobile;    //手机号
    public String phone;    //固定电话
    public int isDefault;   //是否是默认地址  0：否，1：是
    public String province;    //省编码
    public String city;    //市编码
    public String district;    //区编码
    public String provinceText;   //省
    public String cityText;   //市
    public String districtText;   //区
    public String detailAddress;   //详情地址


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.consignee);
        dest.writeString(this.mobile);
        dest.writeString(this.phone);
        dest.writeInt(this.isDefault);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.provinceText);
        dest.writeString(this.cityText);
        dest.writeString(this.districtText);
        dest.writeString(this.detailAddress);
    }

    public AddressEntity() {
    }

    protected AddressEntity(Parcel in) {
        this.id = in.readInt();
        this.consignee = in.readString();
        this.mobile = in.readString();
        this.phone = in.readString();
        this.isDefault = in.readInt();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.provinceText = in.readString();
        this.cityText = in.readString();
        this.districtText = in.readString();
        this.detailAddress = in.readString();
    }

    public static final Parcelable.Creator<AddressEntity> CREATOR = new Parcelable.Creator<AddressEntity>() {
        @Override
        public AddressEntity createFromParcel(Parcel source) {
            return new AddressEntity(source);
        }

        @Override
        public AddressEntity[] newArray(int size) {
            return new AddressEntity[size];
        }
    };
}

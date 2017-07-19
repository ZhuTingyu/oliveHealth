package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Title: OssEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/15  15:14
 *
 * @author wangwei
 * @version 1.0
 */
public class OssEntity implements Parcelable{
    public String type;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.name);
    }

    public OssEntity() {
    }

    protected OssEntity(Parcel in) {
        this.type = in.readString();
        this.name = in.readString();
    }

    public static final Creator<OssEntity> CREATOR = new Creator<OssEntity>() {
        @Override
        public OssEntity createFromParcel(Parcel source) {
            return new OssEntity(source);
        }

        @Override
        public OssEntity[] newArray(int size) {
            return new OssEntity[size];
        }
    };
}

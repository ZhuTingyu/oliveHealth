package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Title: Propertie
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/22  10:22
 *
 * @author wangwei
 * @version 1.0
 */
public class PropertyEntity implements Parcelable {
    public String title;
    public String value;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.value);
    }

    public PropertyEntity() {
    }

    protected PropertyEntity(Parcel in) {
        this.title = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<PropertyEntity> CREATOR = new Parcelable.Creator<PropertyEntity>() {
        @Override
        public PropertyEntity createFromParcel(Parcel source) {
            return new PropertyEntity(source);
        }

        @Override
        public PropertyEntity[] newArray(int size) {
            return new PropertyEntity[size];
        }
    };
}

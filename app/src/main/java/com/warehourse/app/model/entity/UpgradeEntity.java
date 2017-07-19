package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Title: UpgradeEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/19  15:03
 *
 * @author wangwei
 * @version 1.0
 */
public class UpgradeEntity implements Parcelable {
    public boolean need;
    public boolean force;
    public String version;
    public String info;
    public String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.need ? (byte) 1 : (byte) 0);
        dest.writeByte(this.force ? (byte) 1 : (byte) 0);
        dest.writeString(this.version);
        dest.writeString(this.info);
        dest.writeString(this.url);
    }

    public UpgradeEntity() {
    }

    protected UpgradeEntity(Parcel in) {
        this.need = in.readByte() != 0;
        this.force = in.readByte() != 0;
        this.version = in.readString();
        this.info = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<UpgradeEntity> CREATOR = new Parcelable.Creator<UpgradeEntity>() {
        @Override
        public UpgradeEntity createFromParcel(Parcel source) {
            return new UpgradeEntity(source);
        }

        @Override
        public UpgradeEntity[] newArray(int size) {
            return new UpgradeEntity[size];
        }
    };
}

package com.olive.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TingYu Zhu on 2017/9/11.
 */

public class VersionEntity implements Parcelable {
    public String varsionDes;//说明
    public String desc;//版本号说明
    public String url;//ios APP地址，Android包下载地址
    public int forceUpdate;  //是否强制更新 0：否，1：是
    public int version; //版本号


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.varsionDes);
        dest.writeString(this.desc);
        dest.writeString(this.url);
        dest.writeInt(this.forceUpdate);
        dest.writeInt(this.version);
    }

    public VersionEntity() {
    }

    protected VersionEntity(Parcel in) {
        this.varsionDes = in.readString();
        this.desc = in.readString();
        this.url = in.readString();
        this.forceUpdate = in.readInt();
        this.version = in.readInt();
    }

    public static final Parcelable.Creator<VersionEntity> CREATOR = new Parcelable.Creator<VersionEntity>() {
        @Override
        public VersionEntity createFromParcel(Parcel source) {
            return new VersionEntity(source);
        }

        @Override
        public VersionEntity[] newArray(int size) {
            return new VersionEntity[size];
        }
    };
}

package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Title: AreaInfo
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2016/9/6  14:47
 *
 * @author wenfeng
 * @version 1.0
 */
public class AreaInfo implements Parcelable{
    public String name;
    public int id;
    public int level;
    public double lat;
    public double lon;

    public enum AreaInfoType {
        PROVINCE, CITY, DISTRICT,
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
        dest.writeInt(level);
    }

    public AreaInfo() {
    }

    protected AreaInfo(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
        this.lat = in.readDouble();
        this.lon = in.readDouble();
        this.level = in.readInt();
    }

    public static final Creator<AreaInfo> CREATOR = new Creator<AreaInfo>() {
        @Override
        public AreaInfo createFromParcel(Parcel source) {
            return new AreaInfo(source);
        }

        @Override
        public AreaInfo[] newArray(int size) {
            return new AreaInfo[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AreaInfo areaInfo = (AreaInfo) o;

        if (id != areaInfo.id) return false;
        if (Double.compare(areaInfo.lat, lat) != 0) return false;
        if (Double.compare(areaInfo.lon, lon) != 0) return false;
        return name != null ? name.equals(areaInfo.name) : areaInfo.name == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (int) (id ^ (id >>> 32));
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

package com.olive.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TingYu Zhu on 2017/8/2.
 */

public class CategoryEntity implements Parcelable {
    public String code;//编码
    public String name;//分类名称
    public String icon;//图标

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeString(this.icon);
    }

    public CategoryEntity() {
    }

    protected CategoryEntity(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.icon = in.readString();
    }

    public static final Parcelable.Creator<CategoryEntity> CREATOR = new Parcelable.Creator<CategoryEntity>() {
        @Override
        public CategoryEntity createFromParcel(Parcel source) {
            return new CategoryEntity(source);
        }

        @Override
        public CategoryEntity[] newArray(int size) {
            return new CategoryEntity[size];
        }
    };
}

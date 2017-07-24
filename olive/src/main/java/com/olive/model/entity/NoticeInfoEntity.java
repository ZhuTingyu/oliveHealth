package com.olive.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TingYu Zhu on 2017/7/24.
 */

public class NoticeInfoEntity implements Parcelable {
    public String title;      //标题
    public String content;//内容
    public Long  publishDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeValue(this.publishDate);
    }

    public NoticeInfoEntity() {
    }

    protected NoticeInfoEntity(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.publishDate = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<NoticeInfoEntity> CREATOR = new Parcelable.Creator<NoticeInfoEntity>() {
        @Override
        public NoticeInfoEntity createFromParcel(Parcel source) {
            return new NoticeInfoEntity(source);
        }

        @Override
        public NoticeInfoEntity[] newArray(int size) {
            return new NoticeInfoEntity[size];
        }
    };
}

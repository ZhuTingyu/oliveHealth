package com.olive.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TingYu Zhu on 2017/8/1.
 */

public class NoticeEntity implements Parcelable {
    public int id;
    public String title;
    public String image;
    public String intro;
    public long publishDate;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.intro);
        dest.writeLong(this.publishDate);
    }

    public NoticeEntity() {
    }

    protected NoticeEntity(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.image = in.readString();
        this.intro = in.readString();
        this.publishDate = in.readLong();
    }

    public static final Parcelable.Creator<NoticeEntity> CREATOR = new Parcelable.Creator<NoticeEntity>() {
        @Override
        public NoticeEntity createFromParcel(Parcel source) {
            return new NoticeEntity(source);
        }

        @Override
        public NoticeEntity[] newArray(int size) {
            return new NoticeEntity[size];
        }
    };
}

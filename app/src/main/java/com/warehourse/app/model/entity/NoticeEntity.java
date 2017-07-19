package com.warehourse.app.model.entity;

import android.os.Parcel;

/**
 * Title: NoticeEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/15  15:13
 *
 * @author wangwei
 * @version 1.0
 */
public class NoticeEntity implements android.os.Parcelable {
    public String pictureUrl;
    public String clickUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pictureUrl);
        dest.writeString(this.clickUrl);
    }

    public NoticeEntity() {
    }

    protected NoticeEntity(Parcel in) {
        this.pictureUrl = in.readString();
        this.clickUrl = in.readString();
    }

    public static final Creator<NoticeEntity> CREATOR = new Creator<NoticeEntity>() {
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

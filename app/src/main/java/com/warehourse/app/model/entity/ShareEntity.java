package com.warehourse.app.model.entity;

import com.warehourse.app.util.LoadImageUtil;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Title: ShareEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/6/6  18:10
 *
 * @author wangwei
 * @version 1.0
 */
public class ShareEntity implements Parcelable {
    public String recommendUrl;
    public String icon;
    public String shareUrl;
    public String title;
    public String content;

    public String getIcon() {
        if (icon!=null && icon.startsWith("http")){
           return icon;
        }else {
            icon = LoadImageUtil.Builder().load(icon).getImageLoadUrl();
        }
        return icon;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recommendUrl);
        dest.writeString(this.icon);
        dest.writeString(this.shareUrl);
        dest.writeString(this.title);
        dest.writeString(this.content);
    }

    public ShareEntity() {
    }

    protected ShareEntity(Parcel in) {
        this.recommendUrl = in.readString();
        this.icon = in.readString();
        this.shareUrl = in.readString();
        this.title = in.readString();
        this.content = in.readString();
    }

    public static final Creator<ShareEntity> CREATOR = new Creator<ShareEntity>() {
        @Override
        public ShareEntity createFromParcel(Parcel source) {
            return new ShareEntity(source);
        }

        @Override
        public ShareEntity[] newArray(int size) {
            return new ShareEntity[size];
        }
    };
}

package com.warehourse.app.model.entity;

import com.biz.util.Lists;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Title: InitEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/15  15:12
 *
 * @author wangwei
 * @version 1.0
 */
public class InitEntity implements Parcelable{
    public boolean registSkipUserInfo;
    public String searchPlaceHolder;
    public String tel400;
    public String hotKeywords;
    public NoticeEntity notice;
    public List<OssEntity> oss;
    public List<CategoryEntity> categories;
    public int finishOrderSendVoucherLimit;
    public ShareEntity share;


    public List<CategoryEntity> getCategories() {
        return categories==null? Lists.newArrayList():categories;
    }

    public InitEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.registSkipUserInfo ? (byte) 1 : (byte) 0);
        dest.writeString(this.searchPlaceHolder);
        dest.writeString(this.tel400);
        dest.writeString(this.hotKeywords);
        dest.writeParcelable(this.notice, flags);
        dest.writeTypedList(this.oss);
        dest.writeTypedList(this.categories);
        dest.writeInt(this.finishOrderSendVoucherLimit);
        dest.writeParcelable(this.share, flags);
    }

    protected InitEntity(Parcel in) {
        this.registSkipUserInfo = in.readByte() != 0;
        this.searchPlaceHolder = in.readString();
        this.tel400 = in.readString();
        this.hotKeywords = in.readString();
        this.notice = in.readParcelable(NoticeEntity.class.getClassLoader());
        this.oss = in.createTypedArrayList(OssEntity.CREATOR);
        this.categories = in.createTypedArrayList(CategoryEntity.CREATOR);
        this.finishOrderSendVoucherLimit = in.readInt();
        this.share = in.readParcelable(ShareEntity.class.getClassLoader());
    }

    public static final Creator<InitEntity> CREATOR = new Creator<InitEntity>() {
        @Override
        public InitEntity createFromParcel(Parcel source) {
            return new InitEntity(source);
        }

        @Override
        public InitEntity[] newArray(int size) {
            return new InitEntity[size];
        }
    };
}

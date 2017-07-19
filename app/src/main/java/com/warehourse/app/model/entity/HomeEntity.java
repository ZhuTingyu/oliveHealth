package com.warehourse.app.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Title: HomeEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/22  11:01
 *
 * @author wangwei
 * @version 1.0
 */
public class HomeEntity implements Parcelable {
    public static final String TYPE_SLIDE_BANNER = "SlideBanner";
    public static final String TYPE_IMAGE_NAVIGATION = "ImageNavigation";
    public static final String TYPE_IMAGE_SHOWCASE = "ImageShowcase";
    public static final String TYPE_PRODUCT_LIST = "ProductList";

    public String type;
    public List<HomeItemEntity> items;
    public long interval;


    public static class HomeItemEntity implements Parcelable {
        public String imgUrl;
        public String link;
        public String text;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.imgUrl);
            dest.writeString(this.link);
            dest.writeString(this.text);
        }

        public HomeItemEntity() {
        }

        protected HomeItemEntity(Parcel in) {
            this.imgUrl = in.readString();
            this.link = in.readString();
            this.text = in.readString();
        }

        public static final Creator<HomeItemEntity> CREATOR = new Creator<HomeItemEntity>() {
            @Override
            public HomeItemEntity createFromParcel(Parcel source) {
                return new HomeItemEntity(source);
            }

            @Override
            public HomeItemEntity[] newArray(int size) {
                return new HomeItemEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeTypedList(this.items);
        dest.writeLong(this.interval);
    }

    public HomeEntity() {
    }

    protected HomeEntity(Parcel in) {
        this.type = in.readString();
        this.items = in.createTypedArrayList(HomeItemEntity.CREATOR);
        this.interval = in.readLong();
    }

    public static final Parcelable.Creator<HomeEntity> CREATOR = new Parcelable.Creator<HomeEntity>() {
        @Override
        public HomeEntity createFromParcel(Parcel source) {
            return new HomeEntity(source);
        }

        @Override
        public HomeEntity[] newArray(int size) {
            return new HomeEntity[size];
        }
    };
}

package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Title: CategorieEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/15  15:16
 *
 * @author wangwei
 * @version 1.0
 */
public class CategoryEntity implements Parcelable {
    public String id;
    public String name;
    public CategoriesDataEntity postData;
    public String url;
    public static class CategoriesDataEntity implements Parcelable{
        public String categoryId;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.categoryId);
        }

        public CategoriesDataEntity() {
        }

        protected CategoriesDataEntity(Parcel in) {
            this.categoryId = in.readString();
        }

        public static final Creator<CategoriesDataEntity> CREATOR = new Creator<CategoriesDataEntity>() {
            @Override
            public CategoriesDataEntity createFromParcel(Parcel source) {
                return new CategoriesDataEntity(source);
            }

            @Override
            public CategoriesDataEntity[] newArray(int size) {
                return new CategoriesDataEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.postData, flags);
        dest.writeString(this.url);
    }

    public CategoryEntity() {
    }

    protected CategoryEntity(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.postData = in.readParcelable(CategoriesDataEntity.class.getClassLoader());
        this.url = in.readString();
    }

    public static final Creator<CategoryEntity> CREATOR = new Creator<CategoryEntity>() {
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

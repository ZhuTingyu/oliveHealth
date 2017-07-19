package com.warehourse.app.model.entity;

import com.biz.util.Lists;
import com.warehourse.app.R;
import com.warehourse.app.application.WareApplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Title: ProductBrandEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/23  10:54
 *
 * @author wangwei
 * @version 1.0
 */
public class ProductBrandEntity implements Parcelable {
    public String field;
    public List<BrandItemEntity> items;

    public List<BrandItemEntity> getItems() {
        if (items == null)
            items = Lists.newArrayList();
        items.add(0,
                new BrandItemEntity(WareApplication.getAppContext().getString(R.string.all), ""));
        return items;
    }

    public static class BrandItemEntity implements Parcelable {
        public String label;
        public String value;

        public BrandItemEntity(String label, String value) {
            this.label = label;
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.label);
            dest.writeString(this.value);
        }

        public BrandItemEntity() {
        }

        protected BrandItemEntity(Parcel in) {
            this.label = in.readString();
            this.value = in.readString();
        }

        public static final Parcelable.Creator<BrandItemEntity> CREATOR = new Parcelable.Creator<BrandItemEntity>() {
            @Override
            public BrandItemEntity createFromParcel(Parcel source) {
                return new BrandItemEntity(source);
            }

            @Override
            public BrandItemEntity[] newArray(int size) {
                return new BrandItemEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.field);
        dest.writeTypedList(this.items);
    }

    public ProductBrandEntity() {
    }

    protected ProductBrandEntity(Parcel in) {
        this.field = in.readString();
        this.items = in.createTypedArrayList(BrandItemEntity.CREATOR);
    }

    public static final Parcelable.Creator<ProductBrandEntity> CREATOR = new Parcelable.Creator<ProductBrandEntity>() {
        @Override
        public ProductBrandEntity createFromParcel(Parcel source) {
            return new ProductBrandEntity(source);
        }

        @Override
        public ProductBrandEntity[] newArray(int size) {
            return new ProductBrandEntity[size];
        }
    };
}

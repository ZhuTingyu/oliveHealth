package com.warehourse.app.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Title: ProductFilterEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/23  10:50
 *
 * @author wangwei
 * @version 1.0
 */
public class ProductFilterEntity implements Parcelable {
    public String label;
    public String field;
    public boolean hasMore;
    public boolean usePrefix;
    public List<ProductFilterItemEntity> filterItems;
    public boolean showImage;

    public static void clearSelectedAll(List<ProductFilterEntity> list)
    {
        if(list==null||list.size()==0) return;
        for(ProductFilterEntity searchFieldsInfo:list)
        {
            if(searchFieldsInfo.filterItems==null||searchFieldsInfo.filterItems.size()==0)
                continue;
            for(ProductFilterItemEntity searchFieldsValueInfo:searchFieldsInfo.filterItems)
            {
                searchFieldsValueInfo.isSelected=false;
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
        dest.writeString(this.field);
        dest.writeByte(this.hasMore ? (byte) 1 : (byte) 0);
        dest.writeByte(this.usePrefix ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.filterItems);
        dest.writeByte(this.showImage ? (byte) 1 : (byte) 0);
    }

    public ProductFilterEntity() {
    }

    protected ProductFilterEntity(Parcel in) {
        this.label = in.readString();
        this.field = in.readString();
        this.hasMore = in.readByte() != 0;
        this.usePrefix = in.readByte() != 0;
        this.filterItems = in.createTypedArrayList(ProductFilterItemEntity.CREATOR);
        this.showImage = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ProductFilterEntity> CREATOR = new Parcelable.Creator<ProductFilterEntity>() {
        @Override
        public ProductFilterEntity createFromParcel(Parcel source) {
            return new ProductFilterEntity(source);
        }

        @Override
        public ProductFilterEntity[] newArray(int size) {
            return new ProductFilterEntity[size];
        }
    };
}

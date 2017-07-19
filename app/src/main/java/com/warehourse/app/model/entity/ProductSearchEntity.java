package com.warehourse.app.model.entity;

import com.biz.util.Lists;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

/**
 * Title: ProductSearchEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/23  10:49
 *
 * @author wangwei
 * @version 1.0
 */
public class ProductSearchEntity implements Parcelable {
    public String lastFlag;
    public List<ProductEntity> result;
    public List<ProductFilterEntity> filters;
    public ProductBrandEntity brands;

    public String getBrandsField() {
        return brands==null|| TextUtils.isEmpty(brands.field)?"brandId":brands.field;
    }
    public ProductBrandEntity getBrands() {
        return brands==null?new ProductBrandEntity():brands;
    }

    public List<ProductFilterEntity> getFilters() {
        return filters==null?Lists.newArrayList():filters;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lastFlag);
        dest.writeTypedList(this.result);
        dest.writeTypedList(this.filters);
        dest.writeParcelable(this.brands, flags);
    }

    public List<ProductEntity> getList() {
        return result==null? Lists.newArrayList():result;
    }

    public ProductSearchEntity() {
    }

    protected ProductSearchEntity(Parcel in) {
        this.lastFlag = in.readString();
        this.result = in.createTypedArrayList(ProductEntity.CREATOR);
        this.filters = in.createTypedArrayList(ProductFilterEntity.CREATOR);
        this.brands = in.readParcelable(ProductBrandEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProductSearchEntity> CREATOR = new Parcelable.Creator<ProductSearchEntity>() {
        @Override
        public ProductSearchEntity createFromParcel(Parcel source) {
            return new ProductSearchEntity(source);
        }

        @Override
        public ProductSearchEntity[] newArray(int size) {
            return new ProductSearchEntity[size];
        }
    };
}

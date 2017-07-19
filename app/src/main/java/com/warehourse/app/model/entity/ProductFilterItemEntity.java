package com.warehourse.app.model.entity;

import com.biz.util.IdsUtil;
import com.biz.util.Lists;
import com.warehourse.app.R;
import com.warehourse.app.application.WareApplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

/**
 * Title: ProductFilterItemEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/23  10:51
 *
 * @author wangwei
 * @version 1.0
 */
public class ProductFilterItemEntity implements Parcelable {
    public String label;
    public String value;
    public String image;
    public String prefix;
    public boolean highlightShow;
    public boolean isSelected;


    public static String getLabels(List<ProductFilterItemEntity> list)
    {
        if(list==null||list.size()==0)
            return WareApplication.getAppContext().getString(R.string.all);
        List<String> listStr= Lists.newArrayList();
        for(ProductFilterItemEntity searchFieldsValueInfo:list)
        {
            if(searchFieldsValueInfo.isSelected)
                listStr.add(searchFieldsValueInfo.label);
        }
        if(listStr==null||listStr.size()==0)
            return WareApplication.getAppContext().getString(R.string.all);
        return IdsUtil.toString(listStr,"/");
    }


    public String getPrefix() {
        if(TextUtils.isEmpty(prefix)) return "#";
        return prefix;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
        dest.writeString(this.value);
        dest.writeString(this.image);
        dest.writeString(this.prefix);
        dest.writeByte(this.highlightShow ? (byte) 1 : (byte) 0);
    }

    public ProductFilterItemEntity() {
    }

    protected ProductFilterItemEntity(Parcel in) {
        this.label = in.readString();
        this.value = in.readString();
        this.image = in.readString();
        this.prefix = in.readString();
        this.highlightShow = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ProductFilterItemEntity> CREATOR = new Parcelable.Creator<ProductFilterItemEntity>() {
        @Override
        public ProductFilterItemEntity createFromParcel(Parcel source) {
            return new ProductFilterItemEntity(source);
        }

        @Override
        public ProductFilterItemEntity[] newArray(int size) {
            return new ProductFilterItemEntity[size];
        }
    };
}

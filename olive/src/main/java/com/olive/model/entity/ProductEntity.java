package com.olive.model.entity;

import com.biz.util.Lists;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

/**
 * Title: ProductEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/22  10:18
 *
 * @author wangwei
 * @version 1.0
 */
public class ProductEntity implements Parcelable {
    public String productNo;  //商品编码
    public String name;   //商品名称
    public String productName;   //商品名称
    public String standard;   //规格
    public String unit;   //单位
    public int orderCardinality; //订单基数
    public String intro;    //简介
    public String desc;    //详情内容
    public long originalPrice;   //原价
    public long salePrice;//促销价（没有促销价时为0）
    public long saleEndDate; //促销结束时间
    public String images;//商品展示图片（英文逗号分隔）
    public String imgLogo;  //商品logo
    public String imageLogo;  //商品logo
    public int favorited;   //是否已收藏  0：否， 1：是

    //购物车商品属性
    public int quantity;    //数量
    public long price;//价格（分）
    public int productStauts; //0:新品，1：销售中，2:已下架（只有状态是销售中的才能加入订单）
    public boolean isChoose;

    //库存信息属性
    public int saleQuantity;     //销售量

    public int stockQuantity;  //进货量

    public ProductEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productNo);
        dest.writeString(this.name);
        dest.writeString(this.productName);
        dest.writeString(this.standard);
        dest.writeString(this.unit);
        dest.writeInt(this.orderCardinality);
        dest.writeString(this.intro);
        dest.writeString(this.desc);
        dest.writeLong(this.originalPrice);
        dest.writeLong(this.salePrice);
        dest.writeLong(this.saleEndDate);
        dest.writeString(this.images);
        dest.writeString(this.imgLogo);
        dest.writeString(this.imageLogo);
        dest.writeInt(this.favorited);
        dest.writeInt(this.quantity);
        dest.writeLong(this.price);
        dest.writeInt(this.productStauts);
        dest.writeInt(this.saleQuantity);
        dest.writeInt(this.stockQuantity);
    }

    protected ProductEntity(Parcel in) {
        this.productNo = in.readString();
        this.name = in.readString();
        this.productName = in.readString();
        this.standard = in.readString();
        this.unit = in.readString();
        this.orderCardinality = in.readInt();
        this.intro = in.readString();
        this.desc = in.readString();
        this.originalPrice = in.readLong();
        this.salePrice = in.readLong();
        this.saleEndDate = in.readLong();
        this.images = in.readString();
        this.imgLogo = in.readString();
        this.imageLogo = in.readString();
        this.favorited = in.readInt();
        this.quantity = in.readInt();
        this.price = in.readLong();
        this.productStauts = in.readInt();
        this.saleQuantity = in.readInt();
        this.stockQuantity = in.readInt();
    }

    public static final Creator<ProductEntity> CREATOR = new Creator<ProductEntity>() {
        @Override
        public ProductEntity createFromParcel(Parcel source) {
            return new ProductEntity(source);
        }

        @Override
        public ProductEntity[] newArray(int size) {
            return new ProductEntity[size];
        }
    };
}

package com.warehourse.app.model.entity;

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
    public static final int STATUS_NORMAL=10;
    public static final int STATUS_OFF_SHELF=0;
    //NORMAL普通商品;GIFT赠品
    public static final String ITEM_TYPE_NORMAL="NORMAL";
    public static final String ITEM_TYPE_GIFT="GIFT";

    public String id;//商品id
    public String productId;
    public String productCode;//商品编码
    public String productName;//商品编码
    public String name;//商品名称
    public String subTitle;
    public String brief;//简介
    public String logo;//Logo图
    public long salePrice;//仓库价格单位：分
    public long suggestPrice;//建议售价单位：分
    public long marketPrice;//市场采购价单位：分
    public List<String> images;//商品图片
    public List<String> introImages;//介绍图片
    public int minQuantity;//最小起售数量
    public int maxQuantity;//最大可购买数量
    public int stock;//显示的库存数量
    public List<PropertyEntity> properties;//商品属性(底层页表格内容)
    public int status;//商品状态
    public List<SalePromotionEntity> salePromotionDetail;//促销详情
    public boolean isSupportSpecialOffer;//是否支持特价
    public long specialOfferPrice;//特价

    public List<String> tags;//商品标签
    public String apartTagImage;//商品角标图片
    public boolean isSupportVoucher;//是否支持优惠券
    public List<String> supportPromotions;//支持的促销
    public int quantity;


    public boolean returnFlag;//是否已经申请退货

    public boolean checked;

    public String itemType;

    public boolean isGift(){
       return ITEM_TYPE_GIFT.contains(itemType);
    }


    public String getName(){
        return TextUtils.isEmpty(name)?productName:name;
    }

    public List<String> getTags() {
        return tags==null?Lists.newArrayList():tags;
    }

    public String getProductId() {
        if(!TextUtils.isEmpty(id)) return id;
        return productId;
    }

    public String getApartTagImage() {
        return apartTagImage;
    }

    public String getLogo() {
        return logo == null ? "" : logo;
    }

    public List<String> getImages() {
        return images == null ? Lists.newArrayList() : images;
    }

    public List<String> getIntroImages() {
        return introImages == null ? Lists.newArrayList() : introImages;
    }

    public List<PropertyEntity> getProperties() {
        return properties == null ? Lists.newArrayList() : properties;
    }

    public List<String> getSupportPromotions() {
        return supportPromotions == null ? Lists.newArrayList() : supportPromotions;
    }

    public List<SalePromotionEntity> getSalePromotionDetail() {
        return salePromotionDetail==null ? Lists.newArrayList(): salePromotionDetail;
    }

    public int getMaxQuantity() {
        if (maxQuantity <= 0) return 10000;
        return maxQuantity;
    }

    public boolean isOutOfStock() {

        return stock == 0;

    }

    public int getShowQuantity()
    {
        if(stock<minQuantity)
        {
            return 0;
        }
        return stock;
    }

    public boolean isOutShelf(){
        return status == STATUS_OFF_SHELF;
    }


    public ProductEntity() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.productId);
        dest.writeString(this.productCode);
        dest.writeString(this.productName);
        dest.writeString(this.name);
        dest.writeString(this.subTitle);
        dest.writeString(this.brief);
        dest.writeString(this.logo);
        dest.writeLong(this.salePrice);
        dest.writeLong(this.suggestPrice);
        dest.writeLong(this.marketPrice);
        dest.writeStringList(this.images);
        dest.writeStringList(this.introImages);
        dest.writeInt(this.minQuantity);
        dest.writeInt(this.maxQuantity);
        dest.writeInt(this.stock);
        dest.writeTypedList(this.properties);
        dest.writeInt(this.status);
        dest.writeTypedList(this.salePromotionDetail);
        dest.writeByte(this.isSupportSpecialOffer ? (byte) 1 : (byte) 0);
        dest.writeLong(this.specialOfferPrice);
        dest.writeStringList(this.tags);
        dest.writeString(this.apartTagImage);
        dest.writeByte(this.isSupportVoucher ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.supportPromotions);
        dest.writeInt(this.quantity);
        dest.writeByte(this.returnFlag ? (byte) 1 : (byte) 0);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
        dest.writeString(this.itemType);
    }

    protected ProductEntity(Parcel in) {
        this.id = in.readString();
        this.productId = in.readString();
        this.productCode = in.readString();
        this.productName = in.readString();
        this.name = in.readString();
        this.subTitle = in.readString();
        this.brief = in.readString();
        this.logo = in.readString();
        this.salePrice = in.readLong();
        this.suggestPrice = in.readLong();
        this.marketPrice = in.readLong();
        this.images = in.createStringArrayList();
        this.introImages = in.createStringArrayList();
        this.minQuantity = in.readInt();
        this.maxQuantity = in.readInt();
        this.stock = in.readInt();
        this.properties = in.createTypedArrayList(PropertyEntity.CREATOR);
        this.status = in.readInt();
        this.salePromotionDetail = in.createTypedArrayList(SalePromotionEntity.CREATOR);
        this.isSupportSpecialOffer = in.readByte() != 0;
        this.specialOfferPrice = in.readLong();
        this.tags = in.createStringArrayList();
        this.apartTagImage = in.readString();
        this.isSupportVoucher = in.readByte() != 0;
        this.supportPromotions = in.createStringArrayList();
        this.quantity = in.readInt();
        this.returnFlag = in.readByte() != 0;
        this.checked = in.readByte() != 0;
        this.itemType = in.readString();
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

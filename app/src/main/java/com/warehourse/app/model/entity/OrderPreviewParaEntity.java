package com.warehourse.app.model.entity;

import com.biz.util.GsonUtil;
import com.biz.util.Lists;
import com.biz.util.Maps;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Title: OrderPreviewParaEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/31  10:36
 *
 * @author wangwei
 * @version 1.0
 */
public class OrderPreviewParaEntity {
    public static final int TYPE_PAY_DELIVER = 1;
    public static final int TYPE_PAY_ALIPAY = 21;
    public static final int TYPE_PAY_WEIXIN = 22;
    public static final int INVOICE_TYPE_NO = 0;
    public static final int INVOICE_TYPE_SINGLE = 1;
    public static final int INVOICE_TYPE_COMPANY = 2;
    public int paymentType =-1;
    public List<String> usedCoupons;
    public ArrayList<ProductEntity> items;
    public String promotionId;
    public int invoiceType;
    public String invoiceTitle;
    public String invoiceNumber;

    public String description;

    public String toPreviewJson() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("paymentType", paymentType);
        if (usedCoupons != null && usedCoupons.size() > 0) {
            map.put("usedCoupons", usedCoupons);
        }
        if (items != null && items.size() > 0) {
            List<Map<String, Object>> list = Lists.newArrayList();

            for (ProductEntity productEntity : items) {
                Map<String, Object> mapItem = Maps.newHashMap();
                mapItem.put("productId", productEntity.getProductId());
                mapItem.put("quantity", productEntity.quantity);
                list.add(mapItem);
            }
            map.put("items", list);
        }
        return GsonUtil.toJson(map);
    }

    public String toCreateJson() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("paymentType", paymentType);
        if (usedCoupons != null && usedCoupons.size() > 0) {
            map.put("usedCoupons", usedCoupons);
        }
        if (items != null && items.size() > 0) {
            List<Map<String, Object>> list = Lists.newArrayList();

            for (ProductEntity productEntity : items) {
                Map<String, Object> mapItem = Maps.newHashMap();
                mapItem.put("productId", productEntity.getProductId());
                mapItem.put("quantity", productEntity.quantity);
                list.add(mapItem);
            }
            map.put("items", list);
        }
        if (invoiceType == INVOICE_TYPE_SINGLE || invoiceType == INVOICE_TYPE_COMPANY) {
            map.put("invoiceType", invoiceType);
            map.put("invoiceTitle", invoiceTitle);
            map.put("invoiceTaxId", invoiceNumber);
        }
        if (!TextUtils.isEmpty(promotionId)) {
            map.put("promotionId", promotionId);
        }
        if (!TextUtils.isEmpty(description)) {
            map.put("description", description);
        }
        return GsonUtil.toJson(map);
    }

    public String toCreateWeiXinJson() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("paymentType", paymentType);
        if (usedCoupons != null && usedCoupons.size() > 0) {
            map.put("usedCoupons", usedCoupons);
        }
        if (items != null && items.size() > 0) {
            List<Map<String, Object>> list = Lists.newArrayList();

            for (ProductEntity productEntity : items) {
                Map<String, Object> mapItem = Maps.newHashMap();
                mapItem.put("productId", productEntity.getProductId());
                mapItem.put("quantity", productEntity.quantity);
                list.add(mapItem);
            }
            map.put("items", list);
        }
        if (invoiceType == INVOICE_TYPE_SINGLE || invoiceType == INVOICE_TYPE_COMPANY) {
            map.put("invoiceType", invoiceType);
            map.put("invoiceTitle", invoiceTitle);
            map.put("invoiceTaxId",  invoiceNumber);
        }
        if (!TextUtils.isEmpty(promotionId)) {
            map.put("promotionId", promotionId);
        }
        if (!TextUtils.isEmpty(description)) {
            map.put("description", description);
        }
        map.put("appId", com.biz.share.weixin.SendWX.getAppId());
        return GsonUtil.toJson(map);
    }
}

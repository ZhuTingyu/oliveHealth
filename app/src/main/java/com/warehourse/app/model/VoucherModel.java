package com.warehourse.app.model;

import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.google.gson.reflect.TypeToken;

import com.biz.util.GsonUtil;
import com.biz.util.Lists;
import com.biz.util.Maps;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.model.entity.VoucherEntity;
import com.warehourse.app.model.entity.VoucherMainEntity;
import com.warehourse.app.util.HttpRequest;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by wangwei on 2016/3/23.
 */
public class VoucherModel{
    public static Observable<ResponseJson<VoucherMainEntity>> getAll()
    {
        return HttpRequest.<ResponseJson<VoucherMainEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_vouchers_all)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<VoucherMainEntity>>() {
                }.getType())
                .requestPI();
    }
    public static Observable<ResponseJson<List<VoucherEntity>>> getCreateOrderVoucher(List<ProductEntity> list)
    {
        Map<String,Object> mapObject= Maps.newHashMap();
        List<Map<String,Object>> listMap= Lists.newArrayList();
        if(list!=null&&list.size()>0)
        {
            for (ProductEntity productInfo:list)
            {
                Map<String,Object> map= Maps.newHashMap();
                map.put("productId",productInfo.getProductId());
                map.put("quantity",productInfo.quantity<=0?productInfo.minQuantity:productInfo.quantity);
                listMap.add(map);
            }
        }
        mapObject.put("items",listMap);
        return HttpRequest.<ResponseJson<List<VoucherEntity>>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_vouchers_create_order)
                .addBody(GsonUtil.toJson(mapObject))
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<List<VoucherEntity>>>() {
                }.getType())
                .requestPI();
    }
}

package com.warehourse.app.model.entity;

import android.text.TextUtils;

import com.biz.util.GsonUtil;
import com.biz.util.Lists;
import com.biz.util.Maps;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Title: ProductSearchParaEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/23  10:59
 *
 * @author wangwei
 * @version 1.0
 */
public class ProductSearchParaEntity {
    public static final String SORT_SALES_VOLUME_DESC = "salesVolumeDesc";
    public static final String SORT_SALES_VOLUME_ASC = "salesVolumeAsc";
    public static final String SORT_SALE_PRICE_DESC = "salePriceDesc";
    public static final String SORT_SALE_PRICE_ASC = "salePriceAsc";
    public String keyword;
    public int categoryId;
    public String sort = SORT_SALES_VOLUME_DESC;
    public String lastFlag;
    public int pageSize = 30;
    public List<ProductFilterEntity> fields;
    public List<FieldEntity> mFieldEntityList;

    public static class FieldEntity {
        String field;
        List<String> values;
    }

    public String toJson() {
        Map<String, Object> map = Maps.newHashMap();
        if (!TextUtils.isEmpty(keyword)) {
            map.put("keyword", keyword);
        }
        if (categoryId > 0) {
            map.put("categoryId", categoryId);
        }
        map.put("sort", sort);
        if (!TextUtils.isEmpty(lastFlag)) {
            map.put("lastFlag", lastFlag);
        }
        map.put("pageSize", pageSize);

        List<FieldEntity> fieldEntityList = Lists.newArrayList();
        if (mFieldEntityList != null) {
            fieldEntityList.addAll(mFieldEntityList);
        }
        if (fields != null && fields.size() > 0) {
            for (ProductFilterEntity entity : fields) {
                if (entity.field == null || entity.filterItems == null || entity.filterItems.size() == 0) {
                    continue;
                }
                Observable.from(entity.filterItems)
                        .filter(e -> {
                            return !TextUtils.isEmpty(e.value) && e.isSelected;
                        })
                        .map(e -> {
                            return e.value;
                        })
                        .toList()
                        .filter(list -> {
                            return list.size() > 0;
                        })
                        .subscribe(list -> {
                            FieldEntity e = new FieldEntity();
                            e.field = entity.field;
                            e.values = list;
                            fieldEntityList.add(e);
                        });
            }
        }
        if (fieldEntityList.size() > 0) {
            map.put("fields", fieldEntityList);
        }
        return GsonUtil.toJson(map);
    }
}

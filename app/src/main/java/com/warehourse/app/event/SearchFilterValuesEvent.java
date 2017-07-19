package com.warehourse.app.event;

import com.warehourse.app.model.entity.ProductFilterItemEntity;

import java.util.ArrayList;

/**
 * Created by wangwei on 2016/5/31.
 */
public class SearchFilterValuesEvent {
    public String keyCode;
    public ArrayList<ProductFilterItemEntity> fields;
    public boolean isAll=false;
    public SearchFilterValuesEvent(String keyCode,ArrayList<ProductFilterItemEntity> fields)
    {
        this.keyCode=keyCode;
        this.fields=fields;
    }
    public SearchFilterValuesEvent(String keyCode,ArrayList<ProductFilterItemEntity> fields,boolean isAll)
    {
        this.keyCode=keyCode;
        this.fields=fields;
        this.isAll=isAll;
    }
}

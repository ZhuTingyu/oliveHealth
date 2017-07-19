package com.warehourse.app.event;

import com.warehourse.app.model.entity.ProductFilterEntity;

import java.util.ArrayList;

/**
 * Created by wangwei on 2016/5/31.
 */
public class SearchFilterEvent {
    public String sort;
    public String keyCode;
    public ArrayList<ProductFilterEntity> fields;
    public SearchFilterEvent(String keyCode,String sort,ArrayList<ProductFilterEntity> fields)
    {
        this.keyCode=keyCode;
        this.fields=fields;
        this.sort=sort;
    }
}

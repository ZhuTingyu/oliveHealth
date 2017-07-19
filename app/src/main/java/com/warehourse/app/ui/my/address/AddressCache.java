package com.warehourse.app.ui.my.address;


import com.warehourse.app.model.entity.AreaInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: AddressCache
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2016/9/7  15:59
 *
 * @author wenfeng
 * @version 1.0
 */
public class AddressCache {

    private Map<String,List<AreaInfo>> districtMap;
    private Map<String,List<AreaInfo>> cityMap;
    private Map<String,List<AreaInfo>> provinceMap;

    public AddressCache(){
        districtMap = new HashMap<>();
        cityMap = new HashMap<>();
        provinceMap = new HashMap<>();
    }

    /**
     * 添加进入缓存列表
     *
     * @param key
     * @param value
     */
    public void addProvince(String key, List<AreaInfo> value) {
        provinceMap.put(key, value);
    }

    public List<AreaInfo> getProvince(String key) {
        return (List<AreaInfo>) provinceMap.get(key);
    }

    public void addCity(String key, List<AreaInfo> value) {
      cityMap.put(key,value);
    }

    public List<AreaInfo> getCity(String key) {
        return (List<AreaInfo>) cityMap.get(key);
    }
    public void addDistrict(String key, List<AreaInfo> value) {
        districtMap.put(key, value);
    }

    public List<AreaInfo> getDistrict(String key) {
        return (List<AreaInfo>) districtMap.get(key);
    }



}

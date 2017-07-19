package com.biz.http;

import android.text.TextUtils;

import com.biz.application.BaseApplication;
import com.biz.util.GsonUtil;
import com.biz.util.MathUtil;
import com.biz.util.SharedPreferencesUtil;
import com.google.gson.reflect.TypeToken;

/**
 * Title: LocationCache
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2016/9/13  20:18
 *
 * @author wangwei
 * @version 1.0
 */
public class LocationCache {
    public static final String NAME = "LocationCache";
    public static final String NAME_TOKEN = "TOKEN_TOKEN";
    private static LocationCache locationCache;
    private LocationInfo locationInfo;
    private String authToken;


    public static LocationCache getInstance() {
        if (locationCache == null) {
            synchronized (LocationCache.class) {
                locationCache = new LocationCache();
            }
        }
        return locationCache;
    }

    public LocationCache() {
        init();
    }

    public boolean isLocation() {
        if (locationInfo == null)
            return false;
        return !(MathUtil.compareEquals(locationInfo.lat, 0) && MathUtil.compareEquals(locationInfo.lon, 0));
    }

    public boolean isExistsCity() {
        if (locationInfo == null)
            return false;
        return locationInfo.cityId > 0;
    }

    public void setCityId(long cityId, String cityName) {
        getLocationInfo().cityId = cityId;
        getLocationInfo().cityName = cityName;
        save();
    }

    public void setLocation(double lat, double lon, String address) {
        if (MathUtil.compareEquals(lat, 0) && MathUtil.compareEquals(lon, 0))
            return;
        getLocationInfo().lat = lat;
        getLocationInfo().lon = lon;
        getLocationInfo().address = address;
        save();
    }

    public LocationInfo getLocationInfo() {
        if (locationInfo == null)
            locationInfo = new LocationInfo();
        return locationInfo;
    }

    private void save() {
        SharedPreferencesUtil.set(BaseApplication.getAppContext(), NAME, NAME, GsonUtil.toJson(getLocationInfo()));
    }

    private void saveToken() {
        SharedPreferencesUtil.set(BaseApplication.getAppContext(), NAME_TOKEN, NAME_TOKEN, authToken);
    }

    private void init() {
        String json = SharedPreferencesUtil.get(BaseApplication.getAppContext(), NAME, NAME);
        if (!TextUtils.isEmpty(json)) {
            locationInfo = GsonUtil.fromJson(json, new TypeToken<LocationInfo>() {
            }.getType());
        }
        authToken = SharedPreferencesUtil.get(BaseApplication.getAppContext(), NAME_TOKEN, NAME_TOKEN);
    }

    public double lat() {
        return getLocationInfo().lat;
    }

    public double lon() {
        return getLocationInfo().lon;
    }

    public long cityId() {
        return getLocationInfo().cityId;
    }

    public String cityName() {
        return getLocationInfo().cityName;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        saveToken();
    }

    public String getAuthToken() {
        return authToken;
    }
}

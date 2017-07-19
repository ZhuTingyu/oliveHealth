package com.biz.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by wangwei on 2016/1/27.
 */
public class GsonUtil {
    public static <T> T fromJson(String json, Type classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(json, classOfT);
    }

    public static String toJson(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }


}

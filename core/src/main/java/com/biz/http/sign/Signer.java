package com.biz.http.sign;

import com.biz.http.LocationCache;
import com.biz.http.ParaAndroidConfig;
import com.biz.http.R;
import com.biz.http.RestMethodEnum;
import com.biz.application.BaseApplication;
import com.biz.util.Lists;
import com.biz.util.LogUtil;
import com.biz.util.MD5;
import com.biz.util.Maps;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class Signer {
    public static String KEY = "sign";
    public static String Token = "Token";

    public static String toSign(String userId, RestMethodEnum restMethodEnum) {
        return toSign(userId, restMethodEnum, null);
    }

    public static String toSign(String userId, RestMethodEnum restMethodEnum, Object obj) {
        Map<String, Object> paraPublic = Maps.newHashMap();
        paraPublic.put("userId", userId);
        return toSign(paraPublic, restMethodEnum, obj);
    }

    public static String toSign(Map<String, Object> paraPublic, RestMethodEnum restMethodEnum, Object obj) {
        boolean isInterfaceUris = false;
        HashMap<String, Object> properties = toSign(ParaAndroidConfig.getInstance(), isInterfaceUris);
        if (paraPublic != null && paraPublic.size() > 0) {
            for (String key : paraPublic.keySet()) {
                if (TextUtils.isEmpty(key)) continue;
                properties.remove(key);
                properties.put(key, "" + paraPublic.get(key));
            }
        }
        if (LocationCache.getInstance().isLocation()) {
            properties.put("lat", LocationCache.getInstance().lat());
            properties.put("lon", LocationCache.getInstance().lon());
        }
        if (LocationCache.getInstance().isExistsCity()) {
            properties.put("cityId", LocationCache.getInstance().cityId());
        }
        if (!TextUtils.isEmpty(LocationCache.getInstance().getAuthToken())) {
            properties.put("authToken", LocationCache.getInstance().getAuthToken());
        }
        //过滤掉KEY
        Set<String> propertiesKeySets = properties.keySet();
        TreeSet<String> signParamNames = new TreeSet<String>();
        ;
        for (Object propertiesKeySet : propertiesKeySets) {
            String propertyKey = propertiesKeySet.toString();
            if (KEY.equals(propertyKey) || Token.equals(propertyKey)) {
                continue;
            }
            signParamNames.add(propertyKey);
        }
        //添加KEY
        List<String> keyValuePair = Lists.newArrayList();
        for (String signParamName : signParamNames) {
            Object signParamValue = properties.get(signParamName);
            try {
                keyValuePair.add(signParamName + "=" + signParamValue.toString());
            } catch (Exception e) {
                keyValuePair.add(signParamName + "=");
            }
        }
        String sign = join(keyValuePair, "&");
        if (!BaseApplication.getAppContext().getResources().getBoolean(R.bool.isSiner)) return sign;
        if (restMethodEnum != null && restMethodEnum == RestMethodEnum.GET) {//计算 sign
            obj = "";
        }
        if (obj == null) {
            obj = "";
        }
        sign = sign + ParaAndroidConfig.getInstance().Token + obj.toString();
        LogUtil.print(KEY + ":" + sign);
        signParamNames.add(KEY);
        properties.put(KEY, MD5.toMD5(sign));
        keyValuePair.clear();
        for (String signParamName : signParamNames) {
            Object signParamValue = properties.get(signParamName);
            if (signParamValue != null) {
                try {
                    keyValuePair.add(signParamName + "=" + URLEncoder.encode(signParamValue.toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    keyValuePair.add(signParamName + "=");
                }
            } else {
                keyValuePair.add(signParamName + "=");
            }
        }
        sign = join(keyValuePair, "&");
        return sign;
    }

    private static HashMap<String, Object> toSign(Object object, boolean isAttr) {
        Class<?> aClass = object.getClass();
        HashMap<String, Object> properties = Maps.newHashMap();
        Field[] declaredFields = aClass.getDeclaredFields();
        if (declaredFields != null && declaredFields.length != 0) {
            try {
                for (Field declaredField : declaredFields) {
                    if (declaredField.isAnnotationPresent(SignIgnore.class)) {
                        continue;
                    }
                    if (!isAttr && declaredField.isAnnotationPresent(SignIgnoreAttr.class)) {
                        continue;
                    }
                    String name = declaredField.getName();
                    if (name.indexOf("$") > -1) {
                        continue;
                    }
                    Object value;
                    String stringLetter = name.substring(0, 1).toUpperCase();
                    String getName = "get" + (stringLetter + name.substring(1));
                    try {
                        Method getMethod = aClass.getMethod(getName, new Class[]{});
                        value = getMethod.invoke(object, new Object[]{});
                    } catch (Exception e) {
                        declaredField.setAccessible(true);
                        value = declaredField.get(object);
                    }
                    SignProperty signProperty = declaredField.getAnnotation(SignProperty.class);
                    if (signProperty != null && !TextUtils.isEmpty(signProperty.value())) {
                        properties.put(signProperty.value(), value);
                    } else {
                        properties.put(name, value);
                    }
                }
                return properties;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static String join(final Iterable<?> iterable, final String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    private static String join(final Iterator<?> iterator, final String separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        final Object first = iterator.next();
        if (!iterator.hasNext()) {
            @SuppressWarnings("deprecation")
            // ObjectUtils.toString(Object) has been deprecated in 3.2
            final String result = objectToString(first);
            return result;
        }

        // two or more elements
        final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            final Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    public static String objectToString(final Object obj) {
        return obj == null ? "" : obj.toString();
    }
}

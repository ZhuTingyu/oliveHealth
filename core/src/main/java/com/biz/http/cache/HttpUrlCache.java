package com.biz.http.cache;

import com.biz.application.BaseApplication;
import com.google.gson.reflect.TypeToken;

import com.biz.util.GsonUtil;
import com.biz.util.Lists;
import com.biz.util.MD5;
import com.biz.util.SharedPreferencesUtil;
import com.biz.http.R;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wangwei on 2016/3/15.
 */
public class HttpUrlCache {
    public static final String KEY_SHARE_CONFIG_NAME = "HttpUrlCache";
    public static final String KEY_DEFAULT_URL = "KEY_DEFAULT_URL";
    public static final String KEY_DEFAULT_MASTER_URL = "KEY_DEFAULT_MASTER_URL";
    public static final long TIME_UPDATE_CACHE_CYCLE = 1000 * 60 * 10;
    private static HttpUrlCache httpUrlCache;
    private List<String> listUrl;
    private Lock lock;

    public static HttpUrlCache getInstance() {
        if (httpUrlCache == null) {
            synchronized (HttpUrlCache.class) {
                httpUrlCache = new HttpUrlCache();
            }
        }
        return httpUrlCache;
    }

    public boolean isUpdateCache(String key) {
        if (TextUtils.isEmpty(key)) return true;
        String cacheStr = SharedPreferencesUtil.get(BaseApplication.getAppContext(), KEY_SHARE_CONFIG_NAME, MD5.toMD5(key));
        if (TextUtils.isEmpty(cacheStr)) return true;
        HttpCacheInfo httpCacheInfo = GsonUtil.fromJson(cacheStr, new TypeToken<HttpCacheInfo>() {
        }.getType());
        if (httpCacheInfo == null || httpCacheInfo.time + TIME_UPDATE_CACHE_CYCLE < System.currentTimeMillis()) {
            return true;
        }
//        String masterUrl = "";
//        if (httpCacheInfo.url != null && httpCacheInfo.url.size() > 0) {
//            masterUrl = httpCacheInfo.url.get(0);
//        }
//        if (!getMasterHeadUrl().equals(masterUrl)) return true;
        setUrlList(httpCacheInfo.url, key);
        return false;
    }


    public HttpUrlCache() {
        lock = new ReentrantLock();
        listUrl = Lists.newArrayList();
        //读取缓存中的URL列表
        String str = SharedPreferencesUtil.get(BaseApplication.getAppContext(), KEY_SHARE_CONFIG_NAME, KEY_DEFAULT_URL);
        if (!TextUtils.isEmpty(str)) {
            HttpCacheInfo cacheInfo = GsonUtil.fromJson(str, new TypeToken<HttpCacheInfo>() {
            }.getType());
            if (cacheInfo != null)
                listUrl = cacheInfo.url;
        }
        if (listUrl == null || listUrl.size() == 0) {
            String s = BaseApplication.getAppContext().getString(R.string.defualt_http_url_list);
            listUrl = Arrays.asList(s.split(","));
        }
    }

    public String getMasterHeadUrl() {
        return BaseApplication.getAppContext().getString(R.string.api_init_master_head);
    }

    private void setUrlList(List<String> list, String key) {
        if (list == null || list.size() == 0) return;
        lock.lock();
        try {
            listUrl = Lists.newArrayList();
            listUrl.addAll(list);
            HttpCacheInfo httpCacheInfo = new HttpCacheInfo();
            httpCacheInfo.time = System.currentTimeMillis();
            httpCacheInfo.url = listUrl;
            httpCacheInfo.key = key;
            SharedPreferencesUtil.set(BaseApplication.getAppContext(), KEY_SHARE_CONFIG_NAME, KEY_DEFAULT_URL, GsonUtil.toJson(httpCacheInfo));
        } finally {
            lock.unlock();
        }
    }

    public void saveUrlList(List<String> list, String key) {
        setUrlList(list, key);
        if (!TextUtils.isEmpty(key)) {
            HttpCacheInfo httpCacheInfo = new HttpCacheInfo();
            httpCacheInfo.time = System.currentTimeMillis();
            httpCacheInfo.url = list;
            httpCacheInfo.key = key;
            SharedPreferencesUtil.set(BaseApplication.getAppContext(), KEY_SHARE_CONFIG_NAME, MD5.toMD5(key), GsonUtil.toJson(httpCacheInfo));
        }
    }


    public List<String> copyUrlList() {
        lock.lock();
        try {
            List<String> list = Lists.newArrayList();
            Collections.addAll(list, new String[listUrl.size()]);
            Collections.copy(list, listUrl);
            return list;
        } finally {
            lock.unlock();
        }
    }

    public String getUrl(String url) {
        if (TextUtils.isEmpty(url)) return url;
        if (url.indexOf("http") > -1) {
            return url;
        }
        List<String> list = copyUrlList();
        if (list == null || list.size() == 0) return url;
        return list.get(0) + "" + url;
    }

    public void sortIndex(String url) {
        lock.lock();
        try {
            String str = SharedPreferencesUtil.get(BaseApplication.getAppContext(), KEY_SHARE_CONFIG_NAME, KEY_DEFAULT_URL);
            if (!TextUtils.isEmpty(str)) {
                HttpCacheInfo cacheInfo = GsonUtil.fromJson(str, new TypeToken<HttpCacheInfo>() {
                }.getType());
                if (cacheInfo != null) {
                    List<String> copyList = copyUrlList();
                    int index = -1;
                    String urlStr = null;
                    for (int i = 0; i < copyList.size(); i++) {
                        String s = copyList.get(i);
                        if (s.equals(url)) {
                            urlStr = s;
                            index = i;
                            break;
                        }
                    }
                    if (index > 0) {
                        copyList.remove(index);
                        copyList.add(0, urlStr);
                        cacheInfo.url = copyList;
                        SharedPreferencesUtil.set(BaseApplication.getAppContext(), KEY_SHARE_CONFIG_NAME, KEY_DEFAULT_URL, GsonUtil.toJson(cacheInfo));
                        SharedPreferencesUtil.set(BaseApplication.getAppContext(), KEY_SHARE_CONFIG_NAME, MD5.toMD5(cacheInfo.key), GsonUtil.toJson(cacheInfo));
                        listUrl = copyList;
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }
}

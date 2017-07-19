package com.biz.http.cache;

import java.util.List;

/**
 * Created by wangwei on 2016/3/15.
 */
class HttpCacheInfo {
    public String key;
    public long time;
    public List<String> url;

    public HttpCacheInfo() {
    }

    public HttpCacheInfo(String key, long time, List<String> url) {
        this.key = key;
        this.time = time;
        this.url = url;
    }
}

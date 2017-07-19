package com.biz.http;


import com.biz.http.cache.HttpUrlCache;
import com.biz.http.sign.Signer;

import java.util.List;

/**
 * Created by wangwei on 2016/3/31.
 */
final public class UrlSinger {
    private String url;
    private String head;
    private String userId;

    public UrlSinger() {
        List<String> headUrl = headUrl = HttpUrlCache.getInstance().copyUrlList();
        if (headUrl != null && headUrl.size() > 0) {
            this.head = headUrl.get(0);
        }
    }

    public static UrlSinger builder() {
        UrlSinger urlSinger = new UrlSinger();
        return urlSinger;
    }

    public UrlSinger userId(String userId) {
        this.userId = userId;
        return this;
    }

    public UrlSinger url(String url) {
        this.url = url;
        return this;
    }

    public String toUrl() {
        if (this.url != null && this.url.indexOf("http") > -1) {
            if (this.url.indexOf("?") > -1) {
                return url + "&" + Signer.toSign(userId, RestMethodEnum.GET);
            } else {
                return url + "?" + Signer.toSign(userId, RestMethodEnum.GET);
            }
        } else {
            if (this.url.indexOf("?") > -1) {
                return head + url + "&" + Signer.toSign(userId, RestMethodEnum.GET);
            } else {
                return head + url + "?" + Signer.toSign(userId, RestMethodEnum.GET);
            }
        }
    }
}

package com.biz.http;

/**
 * Created by wangwei on 2016/3/15.
 */
final class ResponsePara {
    public int code = -1;

    public boolean isOk() {
        return code == 0;
    }
}

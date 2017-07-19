package com.biz.http;

/**
 * Created by wangwei on 2016/4/18.
 */
public class HttpConfig {
    private static boolean log = true;

    public static boolean isLog() {
        return log;
    }

    public static void setLog(boolean log) {
        HttpConfig.log = log;
    }
}

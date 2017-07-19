package com.biz.http.dispatcher;


import com.biz.util.Lists;

import java.util.List;

/**
 * Created by wangwei on 2016/3/15.
 */
final public class DispatcherInfo {
    public ServerInfo server;
    public long execTime;

    public class ServerInfo {
        public List<String> rest;
    }

    public List<String> getList() {
        if (server == null || server.rest == null) return Lists.newArrayList();
        return server.rest;
    }
}

package com.biz.http;

import android.text.TextUtils;

import com.biz.application.BaseApplication;
import com.biz.http.cache.HttpUrlCache;
import com.biz.http.sign.Signer;
import com.biz.util.GsonUtil;
import com.biz.util.Maps;
import com.biz.util.SysTimeUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by wangwei on 2016/3/15.
 */
public class Request<T> {
    private Map<String, Object> mapBody = Maps.newHashMap();
    private Type toJsonType;
    private Object bodyObj;
    private List<String> headUrl;
    private String url;
    private boolean isAddUrlPara = true;
    private RestMethodEnum restMethodEnum = RestMethodEnum.REST_POST;
    private long beginTime = 0, endTime = 0;
    private boolean isHtml = false;
    private String userId;
    private long connectTime = 15 * 1000;
    private long readTime = 20 * 1000;
    private boolean isHttps = false;
    private Map<String, Object> paraPublic = Maps.newHashMap();

    public Request() {
        headUrl = HttpUrlCache.getInstance().copyUrlList();
    }

    public static <T> Request<T> builder() {
        Request<T> request = new Request<T>();
        request.https(false);
        request.setDefaultConnectTime();
        return request;
    }
    public Request<T> setDefaultConnectTime(){
        return  connectTime(BaseApplication.getAppContext().getResources().getInteger(R.integer.time_connect_out))
                .readTime(BaseApplication.getAppContext().getResources().getInteger(R.integer.time_read_out));
    }

    /**
     * 是否走HTTPS通道
     *
     * @param isHttps
     * @return
     */
    public Request<T> https(boolean isHttps) {
        this.isHttps = isHttps;
        return this;
    }

    /**
     * 设置访问网络的连接时间
     *
     * @param connectTime
     * @return
     */
    public Request<T> connectTime(long connectTime) {
        this.connectTime = connectTime;
        return this;
    }

    /**
     * 设置访问网络的读取时间
     *
     * @param readTime
     * @return
     */
    public Request<T> readTime(long readTime) {
        this.readTime = readTime;
        return this;
    }

    /**
     * 设置访问head
     * 默认为缓存库里面的
     *
     * @param headUrl
     * @return
     */
    public Request<T> headUrl(List<String> headUrl) {
        this.headUrl = headUrl;
        return this;
    }

    /**
     * 转换JSON的对象 参考goson
     *
     * @param toJsonType
     * @return
     */
    public Request<T> setToJsonType(Type toJsonType) {
        this.toJsonType = toJsonType;
        return this;
    }

    /**
     * 网络访问url
     *
     * @param url
     * @return
     */
    public Request<T> url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 网络访问url
     *
     * @param url res api
     * @return
     */
    public Request<T> url(int url) {
        this.url = BaseApplication.getAppContext().getString(url);
        return this;
    }

    /**
     * 是否是html
     *
     * @param isHtml
     * @return
     */
    public Request<T> html(boolean isHtml) {
        this.isHtml = isHtml;
        return this;
    }

    /**
     * 添加网络访问参数
     * 不能与addBody(Object obj) 共用
     * 如果设置了addBody(Object obj)后 本方法失效
     *
     * @param key
     * @param obj
     * @return
     */
    public Request<T> addBody(String key, Object obj) {
        if (mapBody.containsKey(key)) mapBody.remove(key);
        mapBody.put(key, obj);
        return this;
    }

    public Request<T> addPublicPara(String key, Object obj) {
        if (paraPublic.containsKey(key)) paraPublic.remove(key);
        paraPublic.put(key, obj);
        return this;
    }

    /**
     * 添加网络访问参数
     *
     * @param obj
     * @return
     */
    public Request<T> addBody(Object obj) {
        bodyObj = obj;
        return this;
    }

    /**
     * userId
     *
     * @param userId
     * @return
     */
    public Request<T> userId(String userId) {
        this.userId = userId;
        return this;
    }

    public RestMethodEnum getRestMethodEnum() {
        return restMethodEnum;
    }

    public String getBodyObj() {
        if (bodyObj != null) return bodyObj.toString();
        if (mapBody == null || mapBody.size() == 0) return null;
        return GsonUtil.toJson(mapBody);
    }

    /**
     * 访问方式
     *
     * @param restMethodEnum
     * @return
     */
    public Request<T> restMethod(RestMethodEnum restMethodEnum) {
        this.restMethodEnum = restMethodEnum;
        return this;
    }

    public List<String> getHeadUrl() {
        return headUrl;
    }

    public boolean isDispatcher() {
        return restMethodEnum == null && restMethodEnum == RestMethodEnum.DISPATCHER;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getConnectTime() {
        return connectTime;
    }

    public long getReadTime() {
        return readTime;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public boolean isHttps() {
        return isHttps;
    }

    /**
     * 返回网络访问对象 没开线程
     *
     * @return
     */
    public Observable<T> requestPI() {
        Observable<T> observable = RxNet.newRequest(this)
                .map(s -> GsonUtil.fromJson(s, toJsonType));
        observable = observable.map(e -> {
            if (e instanceof ResponseJson) {
                ResponseJson responseJson = (ResponseJson) e;
                if(responseJson.isOk()){
                    SysTimeUtil.initTime(BaseApplication.getAppContext(),responseJson.ts);
                }
                responseJson.execTime = endTime - beginTime;
            }
            return e;
        });
        return observable;
    }

    /**
     * @return
     */
    public Observable<String> requestHtml() {
        return RxNet.newRequest(this);
    }

    public String getUrl(String head) {
        if (isAddUrlPara) {
            String s = head + url;
            if (s.indexOf("?") > -1) {
                return s + "&" + Signer.toSign(getParaPublic(), restMethodEnum, getBodyObj());
            } else {
                return s + "?" + Signer.toSign(getParaPublic(), restMethodEnum, getBodyObj());
            }
        } else return url;
    }
    private Map<String,Object> getParaPublic()
    {
        if(!TextUtils.isEmpty(userId))
        {
            if(paraPublic.containsKey("userId")) paraPublic.remove("userId");
            paraPublic.put("userId",userId);
        }
        return paraPublic;
    }
}

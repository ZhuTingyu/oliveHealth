package com.biz.base;

import com.biz.http.ResponseJson;

public class RestErrorInfo {
    public int code;
    public String message;
    public RestErrorInfo(ResponseJson responseJson){
        this.code=responseJson.code;
        this.message=responseJson.msg;
    }
    public RestErrorInfo(int code,String message){
        this.code=code;
        this.message=message;
    }
    public RestErrorInfo(String message)
    {
        this.code=-1;
        this.message=message;
    }
}

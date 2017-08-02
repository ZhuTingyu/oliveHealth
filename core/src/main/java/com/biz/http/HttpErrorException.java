package com.biz.http;

public class HttpErrorException extends RuntimeException {
    private ResponseJson responseJson;
    public HttpErrorException(ResponseJson responseJson) {
        super(responseJson!=null?responseJson.message:"");
        this.responseJson=responseJson;
    }

    public ResponseJson getResponseJson() {
        return responseJson;
    }
}
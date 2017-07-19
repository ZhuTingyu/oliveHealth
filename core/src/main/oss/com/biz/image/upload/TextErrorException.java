package com.biz.image.upload;

public class TextErrorException extends RuntimeException {
    public TextErrorException(String error) {
        super(error);
    }
}
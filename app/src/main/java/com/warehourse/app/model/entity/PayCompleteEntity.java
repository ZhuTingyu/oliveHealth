package com.warehourse.app.model.entity;

public class PayCompleteEntity {
    public String orderId;
    public boolean isComplete;
    public String message;
    public PayCompleteEntity(String orderId){
        this.orderId=orderId;
        this.isComplete=true;
    }
    public PayCompleteEntity(String orderId,String message){
        this.orderId=orderId;
        this.isComplete=false;
        this.message=message;
}
}
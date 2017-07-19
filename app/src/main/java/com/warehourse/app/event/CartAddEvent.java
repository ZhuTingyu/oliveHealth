package com.warehourse.app.event;

/**
 * Created by johnzheng on 3/21/16.
 */
public class CartAddEvent {
    public int count=0;
    public long productId=0;
    public CartAddEvent(){
    }
    public CartAddEvent(long productId,int count){
        this.productId=productId;
        this.count=count;
    }
}

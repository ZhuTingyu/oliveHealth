package com.olive.ui.main.my.address;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.model.AddressModel;
import com.olive.model.entity.AddressEntity;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/6.
 */

public class AddressViewModel extends BaseViewModel {

    private int addressId;
    protected AddressEntity addressEntity;
    public static final int IS_DEFAULT = 1;
    public static final int NOT_DEFAULT  = 0;

    private List<AddressEntity> addressEntities;


    public AddressViewModel(Object activity) {
        super(activity);
    }

    public void getAddressList(Action1<List<AddressEntity>> action1){
        submitRequestThrowError(AddressModel.AddressList().map(r -> {
            if(r.isOk()){
                addressEntities = r.data;
                return addressEntities;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public void deleteAddress(Action1<String> action1){
        submitRequestThrowError(AddressModel.AddressDelete(addressId).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public void updateAddress(Action1<AddressEntity> action1){
        submitRequestThrowError(AddressModel.AddressUpdate(addressEntity).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }


    public void setAddressEntity(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public void setIsDefault(){
        addressEntity.isDefault = IS_DEFAULT;
    }

    public void setNotDefault(){
        addressEntity.isDefault = NOT_DEFAULT;
    }

    public AddressEntity getDefaultAddress(){
        for(AddressEntity entity : addressEntities){
            if(entity.isDefault == IS_DEFAULT){
                return entity;
            }
        }
        return null;
    }

    public void cancelDefaultAddress(Action1<AddressEntity> action1){
        addressEntity = getDefaultAddress();
        if(addressEntity != null){
            setNotDefault();
            updateAddress(action1);
        }else {
            Observable.just(addressEntity).subscribe(action1);
        }
    }
}

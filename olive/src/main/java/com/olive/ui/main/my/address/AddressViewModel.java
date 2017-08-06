package com.olive.ui.main.my.address;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.olive.model.AddressModel;
import com.olive.model.entity.AddressEntity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/6.
 */

public class AddressViewModel extends BaseViewModel {

    private int addressId;

    public AddressViewModel(Object activity) {
        super(activity);
    }

    public void getAddressList(Action1<List<AddressEntity>> action1){
        submitRequestThrowError(AddressModel.AddressList().map(r -> {
            if(r.isOk()){
                return r.data;
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


    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}

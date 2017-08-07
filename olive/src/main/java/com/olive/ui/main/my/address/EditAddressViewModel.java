package com.olive.ui.main.my.address;

import com.biz.util.IntentBuilder;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class EditAddressViewModel extends AddressViewModel {

    public int id;//地址ID
    public String consignee;   //收货人
    public String mobile;    //手机号
    public String area;
    public String detailAddress;   //详情地址

    public EditAddressViewModel(Object activity) {
        super(activity);
        addressEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
    }

    public Action1<String> setConsignee() {
        return s -> {
            this.consignee = s;
        };
    }

    public Action1<String> setMobile() {
        return s -> {
            this.mobile = s;
        };
    }

    public Action1<String> setArea(){
        return s -> {
            area = s;
        };
    }

    public Action1<String> setDetailAddress() {
        return s -> {
            this.detailAddress = s;
        };
    }
}

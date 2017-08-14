package com.olive.ui.main.my.address;

import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.olive.R;
import com.olive.model.AddressModel;
import com.olive.model.CityModel;
import com.olive.model.entity.AddressEntity;
import com.olive.model.entity.CityEntity;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class EditAddressViewModel extends AddressViewModel {

    public static final int TYPE_PROVINCE = 0;
    public static final int TYPE_CITY = 1;
    public static final int TYPE_COUNTY = 2;

    public int id;//地址ID
    public String consignee;   //收货人
    public String mobile;    //手机号
    public String area;
    public String detailAddress;   //详情地址

    private String code;

    public int positionProvince = 0;
    public int positionCity = 0;
    public int positionCounty = 0;

    public List<CityEntity> provinceList;
    public List<CityEntity> cityList;
    public List<CityEntity> countyList;

    public EditAddressViewModel(Object activity) {
        super(activity);
        addressEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
    }


    public void getCityList(int type, Action1<List<String>> action1) {
        submitRequestThrowError(CityModel.cityList(code).map(r -> {
            if (r.isOk()) {
                List<CityEntity> entities = r.data;
                if (TYPE_PROVINCE == type) {
                    provinceList = entities;
                } else if (TYPE_CITY == type) {
                    cityList = entities;
                } else if (TYPE_COUNTY == type) {
                    countyList = entities;
                }
                return getCitiesName(entities);
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void addAddress(Action1<AddressEntity> action1) {
        submitRequestThrowError(AddressModel.AddressAdd(getNewAddress()).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public AddressEntity getNewAddress() {
        AddressEntity entity = new AddressEntity();
        entity.consignee = consignee;
        entity.mobile = mobile;
        entity.province = getCurrentProvince().code;
        entity.city = getCurrentCity().code;
        entity.district = getCurrentCounty().code;
        entity.detailAddress = detailAddress;
        return entity;
    }

    private List<String> getCitiesName(List<CityEntity> cityEntities) {
        List<String> nameList = Lists.newArrayList();
        for (CityEntity cityEntity : cityEntities) {
            nameList.add(cityEntity.name);
        }
        return nameList;
    }

    public String getAreaString() {
        return getCurrentProvince().name + " "
                + getCurrentCity().name + " "
                + getCurrentCounty().name;
    }

    public void checkAddressInfo(Action1<String> action1){

        Observable.<String>create(subscriber -> {

            if(isStringValid(consignee) && isStringValid(mobile) && isStringValid(area) && isStringValid(detailAddress)){
                if(mobile.length() != 11){
                    subscriber.onNext(getString(R.string.message_input_valid_mobile));
                }else {
                    subscriber.onNext(getString(R.string.message_info_is_valid));
                }
            }else {
                subscriber.onNext(getString(R.string.message_perfect_info));
            }
        }).subscribe(action1);
    }

    private CityEntity getCurrentProvince() {
        return provinceList.get(positionProvince);
    }

    private CityEntity getCurrentCity() {
        return cityList.get(positionCity);
    }

    private CityEntity getCurrentCounty() {
        return countyList.get(positionCounty);
    }

    public void cleanAreaData() {
        if (provinceList != null) {
            provinceList.clear();
        }
        if (cityList != null) {
            cityList.clear();
        }
        if (countyList != null) {
            countyList.clear();
        }

        positionProvince = 0;
        positionCity = 0;
        positionCounty = 0;

        code = null;

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

    public Action1<String> setArea() {
        return s -> {
            area = s;
        };
    }

    public Action1<String> setDetailAddress() {
        return s -> {
            this.detailAddress = s;
        };
    }

    private boolean isStringValid(String string) {
        if (string != null && !string.isEmpty()) {
            return true;
        } else return false;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

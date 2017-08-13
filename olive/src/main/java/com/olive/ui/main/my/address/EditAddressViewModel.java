package com.olive.ui.main.my.address;

import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.olive.model.CityModel;
import com.olive.model.entity.CityEntity;

import java.util.List;

import rx.Observable;
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

    private String parentCode;

    public int positionProvince = 1;
    public int positionCity = 1;
    public int positionDistrict = 1;

    public List<CityEntity> provinceList;
    public List<CityEntity> cityList;
    public List<CityEntity> districtList;

    public EditAddressViewModel(Object activity) {
        super(activity);
        addressEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
    }

    public void initCityList(Action1<Object> action1) {
        getCityList(provinceEntities -> {
            provinceList = provinceEntities;
            if(provinceEntities != null && !provinceEntities.isEmpty()){
                parentCode = provinceEntities.get(positionProvince).parentCode;
            }
            getCityList(cityEntities -> {
                cityEntities = cityList;
                if(cityEntities != null && !cityEntities.isEmpty()){
                    parentCode = cityEntities.get(positionCity).parentCode;
                }
                getCityList(districtEntities -> {
                    districtList = districtEntities;
                });
            });
        });
        Observable.just(new Object()).subscribe(action1);
    }

    public void getCityList(Action1<List<CityEntity>> action1){
        submitRequestThrowError(CityModel.cityList(parentCode).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }), action1);
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

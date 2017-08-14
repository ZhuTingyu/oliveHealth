package com.olive.widget;


import com.biz.util.Lists;
import com.biz.util.Utils;
import com.biz.widget.picker.WheelPicker;
import com.biz.widget.picker.WheelView;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 地址选择器（包括省级、地级、县级）。
 */
public class AddressPicker extends WheelPicker {
    private OnAddressPickListener onAddressPickListener;
    private boolean hideProvince = false;

    private OnWheelViewsListener wheelViewsListener;


    public WheelView provinceView;
    public WheelView cityView;
    public WheelView countyView;

    private LinearLayout centerView;

    /**
     * Instantiates a new Address picker.
     *
     * @param activity the activity
     */
    public AddressPicker(Activity activity) {
        super(activity);
        initCenterView();
    }

    /**
     * Sets selected item.
     *
     * @param province the province
     * @param city     the city
     * @param county   the county
     */
    public void setSelectedItem(String province, String city, String county) {

    }


    /**
     * 隐藏省级行政区，只显示地市级和区县级。
     * 设置为true的话，地址数据中只需要某个省份的即可
     * 参见示例中的“city2.json”
     *
     * @param hideProvince the hide province
     */
    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    /**
     * Sets on address pick listener.
     *
     * @param listener the listener
     */
    public void setOnAddressPickListener(OnAddressPickListener listener) {
        this.onAddressPickListener = listener;
    }

    public void setProvinceItems(List<String> provinceList, int position){
        provinceView.setItems(provinceList, position);
    }

    public void setCityItems(List<String> cityList, int position){
        provinceView.setItems(cityList, position);
    }

    public void setCountyItems(List<String> countyList, int position){
        provinceView.setItems(countyList, position);
    }

    protected void initCenterView(){
        centerView = new LinearLayout(activity);
        centerView.setOrientation(LinearLayout.HORIZONTAL);
        centerView.setGravity(Gravity.CENTER);
        centerView.setPadding(0, Utils.dip2px(36), 0, Utils.dip2px(36));
        provinceView = new WheelView(activity);
        provinceView.setLayoutParams(new LinearLayout.LayoutParams(screenWidthPixels / 3, WRAP_CONTENT));
        provinceView.setTextSize(textSize);
        provinceView.setTextColor(textColorNormal, textColorFocus);
        provinceView.setLineVisible(false);
        provinceView.setLineColor(lineColor);
        provinceView.setOffset(offset);
        centerView.addView(provinceView);
        if (hideProvince) {
            provinceView.setVisibility(View.GONE);
        }
        provinceView.setOnWheelViewListener((isUserScroll, selectedIndex, item) -> {
            wheelViewsListener.onProvince(isUserScroll, selectedIndex, item);
        });
        cityView = new WheelView(activity);
        cityView.setLayoutParams(new LinearLayout.LayoutParams(screenWidthPixels / 3, WRAP_CONTENT));
        cityView.setTextSize(textSize);
        cityView.setTextColor(textColorNormal, textColorFocus);
        cityView.setLineVisible(false);
        cityView.setLineColor(lineColor);
        cityView.setOffset(offset);
        cityView.setOnWheelViewListener((isUserScroll, selectedIndex, item) -> {
            wheelViewsListener.onCity(isUserScroll, selectedIndex, item);
        });
        centerView.addView(cityView);
        countyView = new WheelView(activity);
        countyView.setLayoutParams(new LinearLayout.LayoutParams(screenWidthPixels / 3, WRAP_CONTENT));
        countyView.setTextSize(textSize);
        countyView.setTextColor(textColorNormal, textColorFocus);
        countyView.setLineVisible(false);
        countyView.setLineColor(lineColor);
        countyView.setOffset(offset);
        centerView.addView(countyView);
        countyView.setOnWheelViewListener((isUserScroll, selectedIndex, item) -> {
            wheelViewsListener.onCounty(isUserScroll, selectedIndex, item);
        });
    }

    @Override
    @NonNull
    protected View makeCenterView() {
        return centerView;
    }

    @Override
    public void onSubmit() {
        if (onAddressPickListener != null) {
            onAddressPickListener.onClick();
        }
    }

    /**
     * The interface On address pick listener.
     */
    public interface OnAddressPickListener {
        void onClick();
    }

    public interface OnWheelViewsListener{

        void onProvince(boolean isUserScroll, int selectedIndex, String item);
        void onCity(boolean isUserScroll, int selectedIndex, String item);
        void onCounty(boolean isUserScroll, int selectedIndex, String item);
    }

    public void setOnWheelViewsListener(OnWheelViewsListener wheelViewsListener){
        this.wheelViewsListener = wheelViewsListener;
    }

}


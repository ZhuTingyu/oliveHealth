package com.warehourse.app.ui.my.address;


import com.biz.base.BaseFragment;
import com.biz.base.FragmentAdapter;
import com.biz.base.FragmentBackHelper;
import com.biz.util.Lists;
import com.biz.util.Utils;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.AreaInfo;
import com.warehourse.app.model.entity.AreaInfo.AreaInfoType;
import com.warehourse.app.ui.base.BaseProvinceViewModel;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Observable;

/**
 * Title: AddressPickPagerFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:9/19/16  6:50 PM
 *
 * @author johnzheng
 * @version 1.0
 */

public class AddressPickPagerFragment extends BaseFragment implements FragmentBackHelper {

    protected View mContentView;

    private Animation mInAnim;
    private Animation mOutAnim;


    public ViewPager mViewPager;
    public TabLayout mTabLayout;
    FragmentAdapter mAdapter;
    private EditText mEditText;
    BaseProvinceViewModel mViewModel;

    public AreaInfo districtAreaInfo = new AreaInfo();
    public AreaInfo provinceAreaInfo = new AreaInfo();
    public AreaInfo cityAreaInfo = new AreaInfo();

    public void setEditText(EditText editText) {
        mEditText = editText;
    }

    public void setViewModel(BaseProvinceViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(AreaPickFragment.AreaPickEvent event) {
        mTabLayout.getTabAt(event.index.ordinal()).setText(event.mAreaInfo.name);
        if (event.index == AreaInfoType.PROVINCE) {
            mViewModel.requestCity(list -> {
                getFragment(event.index.ordinal() + 1).setList(list);
                addTab(event.index.ordinal() + 1);
                mTabLayout.getTabAt(event.index.ordinal()+1)
                        .setText(getString(R.string.text_hint_choose));
                if (mTabLayout.getTabAt(2)!=null)
                    mTabLayout.removeTab(mTabLayout.getTabAt(2));
                mViewPager.setCurrentItem(event.index.ordinal() + 1);
            }, throwable -> {
                error(throwable.getMessage());
            }, event.mAreaInfo.id, event.mAreaInfo.name);
            provinceAreaInfo = event.mAreaInfo;
            Observable.just(event.mAreaInfo.name).subscribe(mViewModel.setProvinceText());
            Observable.just(event.mAreaInfo.id).subscribe(mViewModel.setProvince());

        } else if (event.index == AreaInfoType.CITY) {
            mViewModel.requestDistrict(list -> {
                if (list!=null && list.size()>0) {
                    getFragment(event.index.ordinal() + 1).setList(list);
                    addTab(event.index.ordinal() + 1);
                    mTabLayout.getTabAt(event.index.ordinal() + 1)
                            .setText(getString(R.string.text_hint_choose));
                    mViewPager.setCurrentItem(event.index.ordinal() + 1);
                }else {
                    if (mEditText != null)
                        mEditText.setText(provinceAreaInfo.name + " " + cityAreaInfo.name );
                    onBackPressed();
                }
            }, throwable -> {
                error(throwable.getMessage());
            }, event.mAreaInfo.id, event.mAreaInfo.name);

            cityAreaInfo = event.mAreaInfo;
            Observable.just(event.mAreaInfo.name).subscribe(mViewModel.setCityText());
            Observable.just(event.mAreaInfo.id).subscribe(mViewModel.setCity());

        }
       else if (event.index == AreaInfoType.DISTRICT) {
            Observable.just(event.mAreaInfo.name).subscribe(mViewModel.setDistrictText());
            Observable.just(event.mAreaInfo.id).subscribe(mViewModel.setDistrict());
            districtAreaInfo = event.mAreaInfo;
            if (mEditText != null)
                mEditText.setText(provinceAreaInfo.name + " " + cityAreaInfo.name + " " + districtAreaInfo.name);
            onBackPressed();
            return;
        }

    }

    @Override
    public void error(String error) {
        super.error(error);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_choose_layout, container, false);
        mContentView = view.findViewById(R.id.content);
        List<Fragment> mFragmentList = Lists.newArrayList();
        List<String> mStringList = Lists.newArrayList();
        mTabLayout = (TabLayout) view.findViewById(R.id.tab);
        mTabLayout.setTabTextColors(getColors(R.color.color_666666), getColors(R.color.base_color));
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setSelectedTabIndicatorHeight(Utils.dip2px(1));

        for (int i = 0; i < 3; i++) {
            AreaPickFragment fragment = new AreaPickFragment();
            fragment.setIndex(AreaInfoType.values()[i]);
            mFragmentList.add(fragment);
            mStringList.add(getString(R.string.text_hint_choose));

        }
        mAdapter = new FragmentAdapter(getChildFragmentManager(),
                mFragmentList, mStringList);
        mViewPager.setAdapter(mAdapter);
        //if (TextUtils.isEmpty(provinceAreaInfo.name)) {
            mViewModel.requestProvince(list -> {
                getFragment(0).setList(list);
                addTab(0);
            }, throwable -> {
                error(throwable.getMessage());
            });
       // }


        initListener();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            mInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_in_from_bottom);
            mOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out_from_top);

            mContentView.setVisibility(View.VISIBLE);
            mContentView.startAnimation(mInAnim);

        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_in_from_bottom);
        mOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out_from_top);

        mContentView.setVisibility(View.VISIBLE);
        mContentView.startAnimation(mInAnim);

        getView().setOnClickListener(o -> {
            onBackPressed();
        });

    }

    public AreaPickFragment getFragment(int i) {
        return (AreaPickFragment) mAdapter.getItem(i);
    }

    private void addTab(int i) {
        addTab(i, null);
    }


    private void addTab(int i, String name) {
        if (mTabLayout.getTabAt(i) == null)
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(TextUtils.isEmpty(name) ?
                            getString(R.string.text_hint_choose) : name), true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewPager = null;
        mTabLayout = null;

    }

    @Override
    public boolean onBackPressed() {
        if (isVisible()) {
            mContentView.setVisibility(View.GONE);
            mContentView.startAnimation(mOutAnim);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(0, R.anim.alpha_out)
                    .hide(this).commitAllowingStateLoss();
            return true;
        }
        return false;
    }

    private void initListener(){
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setScrollPosition(position, 0, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}

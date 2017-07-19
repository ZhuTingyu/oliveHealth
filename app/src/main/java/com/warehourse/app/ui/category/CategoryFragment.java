package com.warehourse.app.ui.category;

import com.biz.base.BaseLazyFragment;
import com.biz.base.FragmentAdapter;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.warehourse.app.R;
import com.warehourse.app.event.LoginEvent;
import com.warehourse.app.event.MainPointEvent;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.CategoryEntity;
import com.warehourse.app.ui.home.HomeFragment;
import com.warehourse.app.ui.message.MessagesFragment;
import com.warehourse.app.ui.search.SearchActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Title: CategoryFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:10/05/2017  17:36
 *
 * @author johnzheng
 * @version 1.0
 */

public class CategoryFragment extends BaseLazyFragment {

    private View mDotView;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;

    public void onEventMainThread(MainPointEvent event) {
        setDotView(event.isPoint());
    }

    private void setDotView(boolean show){
        mDotView.postDelayed(()->{
            mDotView.setVisibility(show?View.VISIBLE:View.GONE);
        }, 500);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

//    @Override
//    public void error(String error) {
//
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mDrawerLayout != null) {
            mDrawerLayout.setDrawerLockMode(isVisibleToUser ?
                    DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDrawerLayout = getView(getActivity(), R.id.drawer);
        mTabLayout = findViewById(R.id.tab);
        mViewPager = findViewById(R.id.viewpager);

        View actionView = View.inflate(getContext(), R.layout.toolbar_message_dot_layout, null);
        mToolbar.getMenu().add(0, 0, 0, R.string.title_message)
                .setIcon(R.drawable.vector_message_gray)
                .setActionView(actionView)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        bindUi(RxUtil.click(actionView), s -> {
            MessagesFragment.startMessage(getActivity());
            mDotView.setVisibility(View.GONE);
        });
        mAppBarLayout.setPadding(0, Utils.getStatusBarHeight(getActivity()), 0, 0);

        mDotView = getView(actionView, R.id.text_unread);
        mDotView.setVisibility(UserModel.getInstance().isLogin() ? View.VISIBLE : View.GONE);
        setDotView(false);
        EditText searchEdit = getView(R.id.edit_search);
        searchEdit.setHint(SystemModel.getInstance().getPlaceHolder());
        bindUi(RxUtil.click(searchEdit), s -> {
            SearchActivity.startSearch(getContext());
        });

        ImageView icon = getView(actionView, R.id.icon);
        icon.setImageResource(R.drawable.vector_message_gray);

    }

    @Override
    public void lazyLoad() {
        List<String> titles = Lists.newArrayList();
        List<Fragment> fragments = Lists.newArrayList();
        List<CategoryEntity> list= SystemModel.getInstance().getCategories();
        for(CategoryEntity entity:list){
            fragments.add(CategoryChildFragment.newInstance(entity));
            titles.add(entity.name);
        }
        mViewPager.setAdapter(new FragmentAdapter(getChildFragmentManager(), fragments, titles));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(fragments.size());
        setHasLoaded(true);

    }
}

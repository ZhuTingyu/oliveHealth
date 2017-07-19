package com.warehourse.app.ui.search;

import com.biz.base.BaseActivity;
import com.biz.util.IntentBuilder;
import com.biz.widget.BadgeView;
import com.warehourse.app.R;
import com.warehourse.app.event.CartCountEvent;
import com.warehourse.app.event.HotKeyClickEvent;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.ui.main.MainActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import de.greenrobot.event.EventBus;

/**
 * Title: SearchActivity
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:17/05/2017  14:45
 *
 * @author johnzheng
 * @version 1.0
 */

public class SearchActivity extends BaseActivity {

    private AppBarLayout mAppBarLayout;
    private FrameLayout mFrameLayout;
    private Fragment mSearchOverlayFragment, mSearchListFragment;

    private BadgeView cartText;
    private EditText searchText;
    private CartCountViewModel mViewModel;

    public static void startSearch(Context context){
        IntentBuilder.Builder()
                .overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                .setClass(context, SearchActivity.class)
                .startActivity();
    }


    public static void startSearch(Context context, String key){
        IntentBuilder.Builder()
                .setClass(context, SearchActivity.class)
                .putExtra(IntentBuilder.KEY_KEY, key)
                .startActivity();
    }

    public void onEventMainThread(CartCountEvent event) {
        if (event != null) {
            cartText.setText("" + event.count);
        }
    }

    public void onEventMainThread(HotKeyClickEvent event) {
        dismissKeyboard();
        mToolbar.getMenu().findItem(0).setVisible(true);
        if(mSearchListFragment.isHidden()) {
            searchText.setBackgroundResource(R.drawable.shape_corner_gray_gray_background);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .hide(mSearchOverlayFragment)
                    .show(mSearchListFragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_holder);
        mSearchOverlayFragment = new SearchOverlayFragment();
        mSearchListFragment = new SearchListFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_holder, mSearchListFragment)
                .add(R.id.frame_holder, mSearchOverlayFragment)
                .hide(mSearchListFragment)
                .commitNowAllowingStateLoss();
        EventBus.getDefault().register(this);
        mViewModel=new CartCountViewModel(this);
        initViewModel(mViewModel);

        View actionView  = View.inflate(getActivity(), R.layout.toolbar_cart_dot_layout, null);
        mToolbar.getMenu().add(0,0,0, R.string.action_cart)
                .setIcon(R.drawable.vector_cart)
                .setActionView(actionView)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mToolbar.getMenu().findItem(0).setVisible(false);
        cartText = (BadgeView) actionView.findViewById( R.id.text_unread);
        actionView.setOnClickListener(v->{
            MainActivity.startMainWithAnim(getActivity(), 2);
            getActivity().finish();
            getActivity().overridePendingTransition(0, R.anim.right_out);
        });

        searchText = getView(R.id.edit_search);
        searchText.setHint(SystemModel.getInstance().getPlaceHolder());
        searchText.setOnFocusChangeListener((View v, boolean hasFocus) -> {
                if (hasFocus){
                    searchText.setBackgroundResource(R.drawable.shape_rect_gray_white_background);
                    if(mSearchOverlayFragment.isHidden()) {
                        mToolbar.getMenu().findItem(0).setVisible(false);
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                                .show(mSearchOverlayFragment)
                                .hide(mSearchListFragment)
                                .commitNowAllowingStateLoss();
                    }
                }
            }
        );


        mViewModel.queryCartCount();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

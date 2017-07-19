package com.biz.base;



import com.biz.http.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;


public class FragmentParentActivity extends BaseActivity {


    public static void startActivity(Activity context, Class clz, boolean isToolbar) {
        Intent intent = new Intent(context, FragmentParentActivity.class);
        intent.putExtra(FragmentParentActivity.KEY_FRAGMENT, clz);
        intent.putExtra(FragmentParentActivity.KEY_TOOLBAR, isToolbar);
        context. startActivity(intent);
    }


    public static void startActivity(Activity context, Class clz) {
        Intent intent = new Intent(context, FragmentParentActivity.class);
        intent.putExtra(FragmentParentActivity.KEY_FRAGMENT, clz);
        context. startActivity(intent);
    }

    public final static String KEY_FRAGMENT = "KEY_FRAGMENT";
    public final static String KEY_TOOLBAR = "KEY_TOOLBAR";
    private BaseFragment baseFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isToolbar = getIntent().getBooleanExtra(KEY_TOOLBAR, true);
        Class clz = (Class) getIntent().getSerializableExtra(KEY_FRAGMENT);

        String cls = clz.getName();
        if (isToolbar) {
            setContentView(R.layout.activity_with_toolbar_layout);
            Fragment fragment= Fragment.instantiate(getActivity(), cls);
            if(fragment instanceof BaseFragment)
                baseFragment= (BaseFragment) fragment;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_holder,fragment, cls);
            ft.commitAllowingStateLoss();
        }else {
            Fragment fragment= Fragment.instantiate(getActivity(), cls);
            if(fragment instanceof BaseFragment)
                baseFragment= (BaseFragment) fragment;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(android.R.id.content, fragment, cls);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(baseFragment!=null)
            baseFragment.onActivityResult(requestCode,resultCode,data);
    }

}

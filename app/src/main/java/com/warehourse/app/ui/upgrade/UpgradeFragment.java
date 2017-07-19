package com.warehourse.app.ui.upgrade;

import com.biz.base.BaseFragment;
import com.biz.base.FragmentBackHelper;
import com.biz.util.IntentBuilder;
import com.biz.util.RxUtil;
import com.warehourse.app.R;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.entity.UpgradeEntity;
import com.warehourse.app.ui.adv.AdvertisingFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Title: UpgradeFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:22/05/2017  13:32
 *
 * @author johnzheng
 * @version 1.0
 */

public class UpgradeFragment extends BaseFragment implements FragmentBackHelper {

    private TextView textVersion;
    private TextView textDesc;
    private AppCompatImageView btnClose;

    private Button btn1;
    private Button btn2;
    private boolean force = false;
    private UpgradeViewModel mViewModel;

    public static UpgradeFragment newInstance(UpgradeEntity entity) {

        Bundle args = new Bundle();
        args.putParcelable(IntentBuilder.KEY_DATA, entity);
        UpgradeFragment fragment = new UpgradeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment  fragment = getActivity().getSupportFragmentManager()
                .findFragmentByTag(AdvertisingFragment.class.getName());
        if (fragment!=null && fragment.isAdded()){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commitAllowingStateLoss();
        }
        mViewModel=new UpgradeViewModel(this);
        initViewModel(mViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upgrade_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        textVersion =  findViewById(R.id.text_version);

        btn1 =findViewById(R.id.btn1);
        btn2 =  findViewById(R.id.btn2);
        textVersion =  findViewById(R.id.text_version);
        textDesc = findViewById(R.id.text_desc);
        btnClose =  findViewById(R.id.btn_close);

        UpgradeEntity entity = getArguments().getParcelable(IntentBuilder.KEY_DATA);
        force = entity.force;
        textVersion.setText(entity.version);
        textDesc.setText(entity.info);

        btn1.setVisibility(entity.force?View.GONE:View.VISIBLE);
        btnClose.setVisibility(entity.force?View.GONE:View.VISIBLE);

        btnClose.setOnClickListener(v->{
           removeFragment();
        });

        btn2.setOnClickListener(v -> {


            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(entity.url));
                startActivity(intent);
                if (!entity.force)
                    removeFragment();
            } catch (Exception e) {
            }
        });

        btn1.setOnClickListener(v -> {
           mViewModel.cancel();
            removeFragment();
        });

        view.setOnClickListener(v->{});

    }

    private void removeFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(UpgradeFragment.this)
                .commitAllowingStateLoss();
    }


    @Override
    public boolean onBackPressed() {
        if (!force) {
            removeFragment();
            return true;
        }
        return false;
    }
}

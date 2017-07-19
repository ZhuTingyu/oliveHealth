package com.warehourse.app.ui.order.detail;


import com.biz.base.BaseFragment;
import com.warehourse.app.R;
import com.warehourse.app.event.ShareEvent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import de.greenrobot.event.EventBus;

/**
 * Created by johnzheng on 3/22/16.
 */
public class OrderShareFragment extends BaseFragment {

    ImageView btnClose;
    Button btnShare;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_share_layout, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnClose = (ImageView) findViewById(R.id.btn_close);
        btnShare = (Button) findViewById(R.id.btn_share);

        btnClose.setOnClickListener(e->{
            getActivity().getSupportFragmentManager().beginTransaction()
                    .hide(OrderShareFragment.this).commitAllowingStateLoss();
        });
        btnShare.setOnClickListener(e->{
            EventBus.getDefault().post(new ShareEvent());
            getActivity().getSupportFragmentManager().beginTransaction()
                    .hide(OrderShareFragment.this).commitAllowingStateLoss();

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

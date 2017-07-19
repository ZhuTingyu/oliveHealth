package com.warehourse.app.ui.my;

import com.biz.base.BaseLazyFragment;
import com.biz.util.DialogUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.warehourse.app.R;
import com.warehourse.app.event.HotKeyClickEvent;
import com.warehourse.app.event.LoginEvent;
import com.warehourse.app.event.MainPointEvent;
import com.warehourse.app.event.OrderCountEvent;
import com.warehourse.app.event.UserEvent;
import com.warehourse.app.model.OrderModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.ui.coupon.CouponActivity;
import com.warehourse.app.ui.login.LoginActivity;
import com.warehourse.app.ui.my.settings.SettingsFragment;
import com.warehourse.app.ui.order.list.OrderAllFragment;
import com.warehourse.app.ui.order.list.OrderDoneFragment;
import com.warehourse.app.ui.order.list.OrderWaitPayFragment;
import com.warehourse.app.ui.order.list.OrderWaitReceiveFragment;
import com.warehourse.app.ui.order.list.OrderWaitSendFragment;
import com.warehourse.app.ui.web.WebViewActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

/**
 * Title: UserFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:11/05/2017  11:36
 *
 * @author johnzheng
 * @version 1.0
 */

public class UserFragment extends BaseLazyFragment implements View.OnClickListener {

    RecyclerView mRecyclerView;
    UserAdapter mAdapter;

    public void onEventMainThread(LoginEvent event) {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void onEventMainThread(UserEvent event) {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void onEventMainThread(OrderCountEvent event) {
        if (mRecyclerView != null) {
            mRecyclerView.postDelayed(() -> {
                if (mAdapter != null) {
                    mAdapter.notifyEvent(event);
                }
            }, 500);
        }
    }

    public void onEventMainThread(MainPointEvent event) {
        if (mRecyclerView != null) {
            mRecyclerView.postDelayed(() -> {
                if (mAdapter != null) {
                    mAdapter.notifyEvent(event);
                }
            }, 500);
        }
    }

    @Override
    public void lazyLoad() {
        setHasLoaded(true);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(getContext());
        return mRecyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new UserAdapter();

        mAdapter.setList(Lists.newArrayList("", "",
                getString(R.string.title_money),
                getString(R.string.text_settings_protocol),
                getString(R.string.title_contact),
                getString(R.string.title_settings))
        );
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .marginProvider(mAdapter)
                        .colorProvider(mAdapter).build());
        mAdapter.setOnClickListener(this);
        OrderModel.getInstance().updateOrderCount();
    }


    @Override
    public void onClick(View v) {

        TextView textView = getView(v, R.id.title);
        String s = textView.getText().toString().trim();
        if (s.equalsIgnoreCase(getString(R.string.text_settings_protocol))) {
            WebViewActivity.startWebViewAgreement(getActivity());
            return;
        } else if (s.equalsIgnoreCase(getString(R.string.title_contact))) {
            UserModel.getInstance().createContactDialog(getContext());
            return;
        }
        if (!UserModel.getInstance().isLogin()) {
            UserModel.getInstance().createLoginDialog(v.getContext(), () -> {
            });
            return;
        }
        if (s.equalsIgnoreCase(getString(R.string.title_order))) {
            IntentBuilder.Builder()
                    .startParentActivity(getActivity(), OrderAllFragment.class);
        } else if (s.equalsIgnoreCase(getString(R.string.tab_wait_pay_order))) {
            IntentBuilder.Builder()
                    .startParentActivity(getActivity(), OrderWaitPayFragment.class);
        } else if (s.equalsIgnoreCase(getString(R.string.tab_order_done))) {
            IntentBuilder.Builder()
                    .startParentActivity(getActivity(), OrderDoneFragment.class);
        } else if (s.equalsIgnoreCase(getString(R.string.tab_wait_receive_order))) {
            IntentBuilder.Builder()
                    .startParentActivity(getActivity(), OrderWaitSendFragment.class);
        } else if (s.equalsIgnoreCase(getString(R.string.tab_wait_send_order))) {
            IntentBuilder.Builder()
                    .startParentActivity(getActivity(), OrderWaitReceiveFragment.class);
        } else if (s.equalsIgnoreCase(getString(R.string.title_money))) {
            IntentBuilder.Builder()
                    .setClass(getActivity(), CouponActivity.class)
                    .startActivity();
        } else if (s.equalsIgnoreCase(getString(R.string.text_my_account))) {

        } else if (s.equalsIgnoreCase(getString(R.string.title_settings))) {
            IntentBuilder.Builder()
                    .startParentActivity(getActivity(), SettingsFragment.class);
        }
    }


}

package com.olive.ui.main.my;

import com.biz.base.BaseLazyFragment;
import com.biz.util.ActivityStackManager;
import com.biz.util.Lists;
import com.biz.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.olive.R;
import com.olive.ui.login.LoginActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Title: UserFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:20/07/2017  10:57
 *
 * @author johnzheng
 * @version 1.0
 */

public class UserFragment  extends BaseLazyFragment  {

    RecyclerView mRecyclerView;

    List<String> mList;
    @Override
    public void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view;

        UserAdapter adapter = new UserAdapter();
        mRecyclerView.setAdapter(adapter);

        adapter.setNewData(mList =Lists.newArrayList(getResources().getStringArray(R.array.array_my)));
        View header = View.inflate(getActivity(), R.layout.item_my_header_layout, null);

        adapter.addHeaderView(header);

        View footer = View.inflate(getActivity(), R.layout.button_layout, null);

        getView(footer, R.id.btn).setOnClickListener(v -> {
            //EXIT
            LoginActivity.startLogin(getActivity());
            ActivityStackManager.finish();

        });

        adapter.addFooterView(footer);
        adapter.setOnItemClickListener(this::onItemClick);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
        .marginProvider(adapter).colorProvider(adapter).sizeProvider(adapter).build());
    }


    private void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        String s = mList.get(position);
        ToastUtils.showLong(getActivity(), s);
        if (equalsString(s, R.string.text_my_order)){

        }else  if (equalsString(s, R.string.text_refund)){

        }else if (equalsString(s, R.string.text_my_favor)){

        }else if (equalsString(s, R.string.text_my_stock)){

        }else if (equalsString(s, R.string.text_my_account)){

        }else if (equalsString(s, R.string.text_my_address)){

        }else if (equalsString(s, R.string.text_my_password)){

        }else if (equalsString(s, R.string.text_my_password_pay)){

        }else if (equalsString(s, R.string.text_system_info)){

        }
    }

    private boolean equalsString(String s, @StringRes int resId){
        return s.equalsIgnoreCase(getString(resId));
    }
}

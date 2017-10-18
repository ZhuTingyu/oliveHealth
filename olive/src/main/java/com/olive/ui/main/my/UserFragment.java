package com.olive.ui.main.my;

import com.biz.base.BaseLazyFragment;
import com.biz.util.ActivityStackManager;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.UserModel;
import com.olive.ui.account.MyAccountFragment;
import com.olive.ui.adapter.UserAdapter;
import com.olive.ui.address.AddressManageFragment;
import com.olive.ui.favorite.FavoriteFragment;
import com.olive.ui.login.LoginActivity;
import com.olive.ui.order.OrderActivity;
import com.olive.ui.password.ModifyPasswordFragment;
import com.olive.ui.password.ResetPayPasswordFragment;
import com.olive.ui.refund.RefundActivity;
import com.olive.ui.stock.StockManageActivity;
import com.olive.ui.system.SystemMessageFragment;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        TextView title = (TextView) header.findViewById(R.id.title);
        title.setText(UserModel.getInstance().getNickName());

        adapter.addHeaderView(header);

        View footer = View.inflate(getActivity(), R.layout.button_layout, null);

        getView(footer, R.id.btn).setOnClickListener(v -> {
            UserModel.getInstance().loginOut();
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
        if (equalsString(s, R.string.text_my_order)){
            OrderActivity.startOrder(getActivity(), OrderActivity.ORDER_TAB_ALL);
        }else  if (equalsString(s, R.string.text_refund)){
            RefundActivity.start(getActivity(), 0);
        }else if (equalsString(s, R.string.text_my_favor)){
            IntentBuilder.Builder().startParentActivity(getActivity(), FavoriteFragment.class);
        }else if (equalsString(s, R.string.text_my_stock)){
            StockManageActivity.startStock(getActivity());
        }else if (equalsString(s, R.string.text_my_account)){
            IntentBuilder.Builder().startParentActivity(getActivity(), MyAccountFragment.class, true);
        }else if (equalsString(s, R.string.text_my_address)){
            IntentBuilder.Builder().startParentActivity(getActivity(), AddressManageFragment.class, true);
        }else if (equalsString(s, R.string.text_my_password)){
            IntentBuilder.Builder().startParentActivity(getActivity(), ModifyPasswordFragment.class, true);
        }else if (equalsString(s, R.string.text_my_password_pay)){
            IntentBuilder.Builder().startParentActivity(getActivity(), ResetPayPasswordFragment.class, true);
        }else if (equalsString(s, R.string.text_system_info)){
            IntentBuilder.Builder().startParentActivity(getActivity(), SystemMessageFragment.class);
        }
    }

    private boolean equalsString(String s, @StringRes int resId){
        return s.equalsIgnoreCase(getString(resId));
    }

    @Override
    public void error(int code, String error) {
        if(code == 1004){
            UserModel.getInstance().loginOut();
            IntentBuilder.Builder(getActivity(), LoginActivity.class)
                    .putExtra(IntentBuilder.KEY_TYPE, LoginActivity.TYPE_LOGIN_INVALID)
                    .startActivity();
            getActivity().finish();
            return;
        }
        super.error(code, error);
    }
}

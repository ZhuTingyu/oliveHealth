package com.warehourse.app.ui.message;


/**
 * Created by johnzheng on 3/19/16.
 */

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.DrawableHelper;
import com.biz.util.IntentBuilder;
import com.biz.util.RxUtil;
import com.biz.util.StringUtils;
import com.biz.util.TimeUtil;
import com.biz.util.Utils;
import com.biz.widget.recyclerview.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.event.LoginEvent;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.ui.base.EmptyViewHolder;
import com.warehourse.app.ui.login.LoginActivity;
import com.warehourse.app.ui.main.MainActivity;
import com.warehouse.dao.MessageBean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.greenrobot.event.EventBus;


public class MessagesFragment extends BaseFragment {

    XRecyclerView mRecyclerView;
    MessageAdapter mAdapter;
    private MessageListViewModel viewModel;

    public void onEventMainThread(LoginEvent event) {
        if (UserModel.getInstance().isLogin()){
            refresh();
            mRecyclerView.setRefreshListener(this::refresh);

            mAdapter.setOnLoadMoreListener(()->{
                viewModel.loadMore(mAdapter::loadMoreEnd, mAdapter::addData);
            }, mRecyclerView.getRecyclerView());
        }else {
            mRecyclerView.setEnabled(false);
        }
    }

    @Override
    public void error(String error) {
        super.error(error);
        emptyView(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static void startMessage(Activity context) {
        IntentBuilder.Builder()
                .startParentActivity(context, MessagesFragment.class);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);

        viewModel = new MessageListViewModel(this);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = getView(R.id.list);
        setTitle(R.string.title_message);
        mAdapter = new MessageAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshListener(this::refresh);
        addItemDecorationLine(mRecyclerView.getRecyclerView());
        if (UserModel.getInstance().isLogin()) {
            emptyView(mAdapter);
        } else {
            loginView(mAdapter);
        }
        onEventMainThread(new LoginEvent());
    }

    private void refresh() {
        viewModel.refresh(mAdapter::loadMoreEnd, list->{
            mAdapter.setNewData(list);
            emptyView(mAdapter);

        });
    }

    private void loginView(BaseQuickAdapter adapter) {
        View view = View.inflate(getContext(), R.layout.view_nologin_layout, null);
        EmptyViewHolder holder = new EmptyViewHolder(view);
        holder.empty.setVisibility(View.GONE);
        holder.btnEmpty.setText(R.string.text_message_login);
        holder.btnEmpty.setBackgroundResource(R.drawable.btn_red_rect_selector);
        holder.btnEmpty.setOnClickListener(LoginActivity::goLogin);
        holder.btnEmpty.setTextColor(getColor(R.color.white));
        holder.btnEmpty.getLayoutParams().width = Utils.dip2px(200);
        mAdapter.setEmptyView(holder.itemView);
    }

    private void emptyView(BaseQuickAdapter adapter) {
            EmptyViewHolder holder = EmptyViewHolder.createHolder(getContext())
                    .setIcon(R.color.color_transparent)
                    .setTitle(R.string.text_message_none);
            mAdapter.setEmptyView(holder.itemView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class MessageAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {


        public MessageAdapter() {
            super(R.layout.item_message_layout);
        }


        @Override
        protected void convert(BaseViewHolder holder, MessageBean info) {
            TextView textView = holder.findViewById(R.id.title_line_2);
            holder.setText(R.id.title_line_1, info.getTitle());
            holder.setText(R.id.title_line_2, TimeUtil.format(info.getCreateTime(), TimeUtil.FORMAT_YYYYMMDDHHMM));
            holder.setText(R.id.title_line_3, info.getContent());
            if (StringUtils.isBlank(info.getUrl())) {
                textView.setCompoundDrawables(null, null, null, null);
            } else {
                textView.setCompoundDrawables(null, null,
                        DrawableHelper.getDrawable(holder.itemView.getContext(),
                                R.drawable.ic_arrow_right_gray), null);
            }
            RxUtil.click(holder.itemView).subscribe(o -> {
                viewModel.updateRead(info, b -> {
                });
                if (!TextUtils.isEmpty(info.getUrl())) {
                    MainActivity.startUri(getActivity(), info.getUrl());
                }
            });
        }


    }

}


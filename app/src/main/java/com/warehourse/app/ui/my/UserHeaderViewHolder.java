package com.warehourse.app.ui.my;

import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.widget.CustomDraweeView;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.ui.login.LoginActivity;
import com.warehourse.app.ui.message.MessagesFragment;
import com.warehourse.app.ui.my.user.UserInfoActivity;
import com.warehourse.app.util.LoadImageUtil;

import android.app.Activity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

/**
 * Title: UserHeaderAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:11/05/2017  12:14
 *
 * @author johnzheng
 * @version 1.0
 */

class UserHeaderViewHolder extends BaseViewHolder {
    public AppCompatImageView mIcon;
    public View mDotView;
    public CustomDraweeView mAvatar;
    public TextView mTitle;
    public TextView mText;

    public UserHeaderViewHolder(View itemView) {
        super(itemView);

        mIcon = findViewById(R.id.icon);
        mDotView = findViewById(R.id.text_unread);
        mAvatar = findViewById(R.id.avatar);
        mTitle = findViewById(R.id.title);
        mText = findViewById(R.id.text);

        setTextUnread();


        ((View) mIcon.getParent()).setOnClickListener(v -> {
            MessagesFragment.startMessage((Activity) v.getContext());
            mDotView.setVisibility(View.GONE);
        });
        setDotView(false);

    }

    public void setDotView(boolean show){
        mDotView.setVisibility(show?View.VISIBLE:View.GONE);
    }

    void bindData(){
        if (UserModel.getInstance().isLogin()) {
            setUserInfoText();
        } else {
            setNoLogin();
        }
    }

    public void setUserInfoText() {
        mTitle.setText(UserModel.getInstance().getName());
        setAvatar();
        mDotView.setVisibility(View.VISIBLE);
        mText.setOnClickListener(null);
        mText.setOnClickListener(v -> {
            IntentBuilder.Builder().setClass(itemView.getContext(), UserInfoActivity.class)
                    .startActivity();
        });

    }

    public void setNoLogin() {
        mTitle.setText(R.string.text_login_register);
        mDotView.setVisibility(View.GONE);
        setAvatar();
        mTitle.setOnClickListener(v -> {
            IntentBuilder.Builder().setClass(itemView.getContext(), LoginActivity.class)
                    .startActivity();
        });
        mText.setOnClickListener(null);
        mText.setOnClickListener(v -> {
            UserModel.getInstance().createLoginDialog(v.getContext(),()->{});
        });
    }

    void setAvatar(){
        LoadImageUtil.Builder()
                .load(UserModel.getInstance().getAvatar())
                .build().imageOptions(R.drawable.vector_avatar)
                .displayImage(mAvatar);
    }

    public void setTextUnread() {
        mDotView.setBackgroundResource(R.drawable.white_dot);
    }
}

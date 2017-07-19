package com.warehourse.app.ui.my.user;

import com.biz.base.BaseActivity;
import com.biz.util.IntentBuilder;
import com.biz.util.LogUtil;
import com.biz.util.PhotoUtil;
import com.biz.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.event.UserEvent;
import com.warehourse.app.ui.bottomsheet.BottomSheetBuilder;
import com.warehourse.app.ui.bottomsheet.BottomSheetMultipleItem;
import com.warehourse.app.ui.my.address.AddressInfoFragment;
import com.warehourse.app.ui.my.phone.PhoneInfoFragment;
import com.warehourse.app.ui.my.receiver.ReceiverInfoFragment;
import com.warehourse.app.ui.password.ChangeNeedOldFragment;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.greenrobot.event.EventBus;
import rx.Observable;

/**
 * Title: UserInfoActivity
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:16/05/2017  14:04
 *
 * @author johnzheng
 * @version 1.0
 */
public class UserInfoActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private UserInfoAdapter mAdapter;
    private Uri uriCamera;
    private BottomSheetDialog mSheetDialog;
    private UserInfoViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_toolbar_layout);
        getLayoutInflater().inflate(R.layout.activity_recyclerview, getView(R.id.frame_holder));
        mToolbar.setTitle(R.string.text_my_account);
        EventBus.getDefault().register(this);
        mViewModel = new UserInfoViewModel(this);
        initViewModel(mViewModel);
        mRecyclerView = getView(android.R.id.list);
        mAdapter = new UserInfoAdapter(UserMultipleItem.getList());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .marginProvider(mAdapter)
                        .colorProvider(mAdapter).build());

        mAdapter.setOnItemClickListener((BaseQuickAdapter baseQuickAdapter, View view, int i) -> {
            switch (mAdapter.getItem(i).getItemType()) {
                case UserMultipleItem.AVATAR:
                    createBottomSheet();
                    break;
                case UserMultipleItem.USERNAME:
                    break;
                case UserMultipleItem.PHONE:
                    IntentBuilder.Builder().startParentActivity(getActivity(), PhoneInfoFragment.class);
                    break;
                case UserMultipleItem.DELIVERY_NAME:
                    IntentBuilder.Builder().startParentActivity(getActivity(), ReceiverInfoFragment.class);
                    break;
                case UserMultipleItem.DELIVERY_ADDRESS:
                    IntentBuilder.Builder().startParentActivity(getActivity(), AddressInfoFragment.class);
                    break;
                case UserMultipleItem.PASSWORD:
                    IntentBuilder.Builder()
                            .startParentActivity(getActivity(), ChangeNeedOldFragment.class);
                    break;
                default:
            }
        });
    }

    private void createBottomSheet() {
        mSheetDialog = BottomSheetBuilder.createPhotoBottomSheet(getActivity(),
                (BaseQuickAdapter adapter, View v, int index) -> {
                    BottomSheetMultipleItem item = (BottomSheetMultipleItem) adapter.getItem(index);
                    if (item != null) {
                        switch (item.getItemType()) {
                            case BottomSheetMultipleItem.CAMERA:
                                PhotoUtil.photo(UserInfoActivity.this,uri->{
                                    uriCamera=uri;
                                });
                                mSheetDialog.dismiss();
                                break;
                            case BottomSheetMultipleItem.GALLERY:
                                PhotoUtil.gallery(UserInfoActivity.this);
                                mSheetDialog.dismiss();
                                break;
                            case BottomSheetMultipleItem.CANCEL:
                                mSheetDialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == PhotoUtil.CAMERA_SUCCESS) {
            setIcon(uriCamera.getPath());
        } else if (requestCode == PhotoUtil.PHOTO_SUCCESS) {
            Uri originalUri = intent.getData();
            setIcon(PhotoUtil.getPath(getActivity(), originalUri));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(UserEvent event) {
        if (event != null && mAdapter != null) {
            if (event.type == UserEvent.TYPE_CHANGE_MOBILE) {
                mAdapter.notifyItemChangedPhone();
            } else if (event.type == UserEvent.TYPE_CHANGE_ADDRESS) {
                mAdapter.notifyItemChangedDelivery();
            }
        }
    }

    private void setIcon(String url) {
        setProgressVisible(true);
        mViewModel.upload(url, s -> {
            LogUtil.print("upload:" + s);
            setProgressVisible(true);
            mViewModel.changeAvatar(s, b -> {
                if (mAdapter != null)
                    mAdapter.notifyItemChangedAvatar();
                setProgressVisible(false);
            });
        });
    }
}

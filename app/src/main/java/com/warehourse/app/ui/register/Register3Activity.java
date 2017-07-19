package com.warehourse.app.ui.register;


import com.biz.base.BaseActivity;
import com.biz.util.DialogUtil;
import com.biz.util.DrawableHelper;
import com.biz.util.IntentBuilder;
import com.biz.util.LogUtil;
import com.biz.util.PhotoUtil;
import com.biz.util.Utils;
import com.biz.widget.CustomDraweeView;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.drawable.ScalingUtils;
import com.warehourse.app.R;
import com.warehourse.app.ui.bottomsheet.BottomSheetBuilder;
import com.warehourse.app.ui.bottomsheet.BottomSheetMultipleItem;
import com.warehourse.app.ui.login.LoginActivity;
import com.warehourse.app.util.LoadImageUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rx.Observable;

/**
 * Created by johnzheng on 3/18/16.
 */
public class Register3Activity extends BaseActivity {


    CustomDraweeView icon1;
    TextView text1;
    CustomDraweeView icon2;
    TextView text2;
    CustomDraweeView icon3;
    CustomDraweeView icon4;
    TextView textInfo;
    BottomSheetDialog dialog;
    Uri uriCamera;
    int type = 0;
    private EditShopPhotoViewModel viewModel;


    String url1, url2, url3, url4, url_1, url_2, url_3, url_4;

    public boolean isChanged() {
        if (url1 != null && !url1.equals(url_1)) return true;
        if (url2 != null && !url2.equals(url_2)) return true;
        if (url3 != null && !url3.equals(url_3)) return true;
        return url4 != null && !url4.equals(url_4);
    }


    @Override
    public void onBackPressed() {
        if (isChanged()) {
            DialogUtil.createDialogViewWithCancel(getActivity(), R.string.dialog_title_notice,
                    R.string.dialog_msg_saved,
                    (dialog, which) -> {
                        LoginActivity.goLogin(getActivity());
                        getActivity().finish();
                    }, R.string.btn_confirm);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_toolbar_layout);
        viewModel = new EditShopPhotoViewModel(this);
        initViewModel(viewModel);
        getLayoutInflater().inflate(R.layout.fragment_register_3_layout, getView(R.id.frame_holder));

        icon1 = (CustomDraweeView) findViewById(R.id.icon1);
        text1 = (TextView) findViewById(R.id.text1);
        icon2 = (CustomDraweeView) findViewById(R.id.icon2);
        text2 = (TextView) findViewById(R.id.text2);
        icon3 = (CustomDraweeView) findViewById(R.id.icon3);
        icon4 = (CustomDraweeView) findViewById(R.id.icon4);
        textInfo = (AppCompatTextView) findViewById(R.id.text_info);

        mToolbar.getMenu().add(0, 0, 0, R.string.text_upload).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mToolbar.setTitle(R.string.text_upload_profile);
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 0) {
                setProgressVisible(true);
                viewModel.submit(b -> {
                    setProgressVisible(false);
                    IntentBuilder.Builder().startParentActivity(getActivity(), RegisterDoneFragment.class);
                    ActivityCompat.finishAffinity(getActivity());
                });
            }
            return false;
        });
        icon1.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        icon2.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        icon3.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        icon4.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        int size = Utils.dip2px(170);
        icon1.setSize(size, size);
        icon2.setSize(size, size);
        icon3.setSize(size, size);
        icon4.setSize(size, size);


        icon1.setOnClickListener(e -> {
            type = 1;
            createBottomSheet("ic_business_license_example.jpg");

        });

        icon2.setOnClickListener(e -> {
            type = 2;
            createBottomSheet("ic_gate_photo_example.jpg");

        });

        icon3.setOnClickListener(e -> {
            type = 3;
            createBottomSheet("ic_wine_license_example.jpg");

        });

        icon4.setOnClickListener(e -> {
            type = 4;
            createBottomSheet("ic_identity_license_example.jpg");
        });


        LoadImageUtil.Builder().loadAssets("ic_business_license.jpg").build().displayImage(icon1);
        LoadImageUtil.Builder().loadAssets("ic_gate_photo.jpg").build().displayImage(icon2);
        LoadImageUtil.Builder().loadAssets("ic_wine_license1.jpg").build().displayImage(icon3);
        LoadImageUtil.Builder().loadAssets("ic_identity_license1.jpg").build().displayImage(icon4);
        bindData(viewModel.getBusinessLicence(), url -> {
            if (!TextUtils.isEmpty(url)) {
                url_1 = url;
                LoadImageUtil.Builder().isPrivate(true)
                        .load(url).build().displayImage(icon1);
            }
        });
        bindData(viewModel.getShopPhoto(), url -> {
            if (!TextUtils.isEmpty(url)) {
                url_2 = url;
                LoadImageUtil.Builder().isPrivate(true)
                        .load(url).build().displayImage(icon2);
            }
        });
        bindData(viewModel.getLiquorSellLicence(), url -> {
            if (!TextUtils.isEmpty(url)) {
                url_3 = url;
                LoadImageUtil.Builder().isPrivate(true)
                        .load(url).build().displayImage(icon3);
            }
        });
        bindData(viewModel.getCorporateIdPhoto(), url -> {
            if (!TextUtils.isEmpty(url)) {
                url_4 = url;
                LoadImageUtil.Builder().isPrivate(true)
                        .load(url).build().displayImage(icon4);
            }
        });
        setTitleLeftDrawable(text1);
        setTitleLeftDrawable(text2);
        setTitleLeftDrawable(textInfo);
        url1 = "";
        url2 = "";
        url3 = "";
        url4 = "";
        request();
    }

    private void request() {
        setProgressVisible(true);
        viewModel.checkToken(b -> {
            viewModel.requestPhoto(isLoad -> {
                setProgressVisible(false);
            });
        }, throwable -> {
            setProgressVisible(false);
            DialogUtil.createDialogView(getActivity(), R.string.dialog_title_notice,
                    R.string.text_error_load_oss_token,
                    (dialog, which) -> {
                        dialog.dismiss();
                        getActivity().finish();
                    }, R.string.btn_cancel, (dialog1, which) -> {
                        dialog1.dismiss();
                        request();
                    }, R.string.btn_confirm);
        });
    }

    public void setTitleLeftDrawable(TextView textView) {
        textView.setCompoundDrawables(DrawableHelper
                        .getDrawableWithBounds(textView.getContext(), R.drawable.ic_star_small),
                null, null
                , null);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    void createBottomSheet(String url) {
        dialog = BottomSheetBuilder.createPhotoBottomSheetWithHeader(getActivity(), url,
                (BaseQuickAdapter adapter, View v, int index) -> {
                    BottomSheetMultipleItem item = (BottomSheetMultipleItem) adapter.getItem(index);
                    if (item != null) {
                        switch (item.getItemType()) {
                            case BottomSheetMultipleItem.CAMERA:
                                PhotoUtil.photo(Register3Activity.this,uri -> {
                                    uriCamera=uri;
                                });
                                dialog.dismiss();
                                break;
                            case BottomSheetMultipleItem.GALLERY:
                                PhotoUtil.gallery(Register3Activity.this);
                                dialog.dismiss();
                                break;
                            case BottomSheetMultipleItem.CANCEL:
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });


    }


    private void setIcon(String url) {
        if (TextUtils.isEmpty(url)) return;
//        switch (type) {
//            case 1:
//
//                LoadImageUtil.Builder().loadFile(url_1 = url).build().displayImage(icon1);
//                break;
//            case 2:
//                LoadImageUtil.Builder().loadFile(url_2 = url).build().displayImage(icon2);
//                break;
//            case 3:
//                LoadImageUtil.Builder().loadFile(url_3 = url).build().displayImage(icon3);
//                break;
//            case 4:
//                LoadImageUtil.Builder().loadFile(url_4 = url).build().displayImage(icon4);
//                break;
//            default:
//                break;
//        }
        setProgressVisible(true);
        viewModel.upload(url, s -> {
            setProgressVisible(false);
            LogUtil.print("upload:" + s);
            switch (type) {
                case 1:
                    Observable.just(url_1 = s).subscribe(viewModel.setBusinessLicence());
                    LoadImageUtil.Builder().load(url_1).isPrivate(true).build().displayImage(icon1);
                    break;
                case 2:
                    Observable.just(url_2 = s).subscribe(viewModel.setShopPhoto());
                    LoadImageUtil.Builder().load(url_2).isPrivate(true).build().displayImage(icon2);
                    break;
                case 3:
                    Observable.just(url_3 = s).subscribe(viewModel.setLiquorSellLicence());
                    LoadImageUtil.Builder().load(url_3).isPrivate(true).build().displayImage(icon3);
                    break;
                case 4:
                    Observable.just(url_4 = s).subscribe(viewModel.setCorporateIdPhoto());
                    LoadImageUtil.Builder().load(url_4).isPrivate(true).build().displayImage(icon4);
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if ((PhotoUtil.CAMERA_SUCCESS == requestCode || PhotoUtil.PHOTO_SUCCESS == requestCode) && resultCode == Activity.RESULT_OK) {
            String url = "";
            if (requestCode == PhotoUtil.CAMERA_SUCCESS) {
                url = uriCamera.getPath();
            } else if (requestCode == PhotoUtil.PHOTO_SUCCESS) {
                Uri originalUri = intent.getData();
                url = PhotoUtil.getPath(getActivity(), originalUri);
            }
            setIcon(url);
        }
    }

}

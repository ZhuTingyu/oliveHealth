package com.biz.util;

import com.biz.base.BaseActivity;
import com.biz.base.BaseFragment;
import com.tbruyelle.rxpermissions.RxPermissions;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;

import rx.functions.Action1;

/**
 * Title: PhotoUtil
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/7/4  14:44
 *
 * @author wangwei
 * @version 1.0
 */
public class PhotoUtil {
    public static final int PHOTO_SUCCESS = 2083;
    public static final int CAMERA_SUCCESS = 2082;

    public static void photo(BaseActivity baseActivity, Action1<Uri> onNext) {
        if (baseActivity == null) return;
        RxPermissions rxPermissions = new RxPermissions(baseActivity);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(
                result -> {
                    if (result) {
                        photo(rxPermissions, baseActivity, onNext);
                    } else {
                        ToastUtils.showShort(baseActivity, com.biz.http.R.string.text_error_permission_storage);
                    }
                });
    }

    public static void photo(BaseFragment baseFragment, Action1<Uri> onNext) {
        if (baseFragment == null) return;
        RxPermissions rxPermissions = new RxPermissions(baseFragment.getBaseActivity());
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(
                result -> {
                    if (result) {
                        photo(rxPermissions, baseFragment, onNext);
                    } else {
                        ToastUtils.showShort(baseFragment.getContext(), com.biz.http.R.string.text_error_permission_storage);
                    }
                });
    }

    private static void photo(RxPermissions rxPermissions, BaseActivity baseActivity, Action1<Uri> onNext) {
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(result -> {
                    if (result) {
                        Uri uri = goCamera(baseActivity);
                        if (onNext != null)
                            onNext.call(uri);
                    } else
                        ToastUtils.showShort(baseActivity, com.biz.http.R.string.text_error_permission_photo);
                });
    }

    private static void photo(RxPermissions rxPermissions, BaseFragment baseFragment, Action1<Uri> onNext) {
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(result -> {
                    if (result) {
                        Uri uri = goCamera(baseFragment);
                        if (onNext != null)
                            onNext.call(uri);
                    } else
                        ToastUtils.showShort(baseFragment.getContext(), com.biz.http.R.string.text_error_permission_photo);
                });
    }


    public static void gallery(BaseActivity baseActivity){
        if(baseActivity==null) return;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT < 19) { //19以后这个api不可用，demo这里简单处理成图库选择图片
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        baseActivity.startActivityForResult(intent, PHOTO_SUCCESS);
    }

    public static void gallery(BaseFragment baseFragment){
        if(baseFragment==null) return;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT < 19) { //19以后这个api不可用，demo这里简单处理成图库选择图片
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        baseFragment.startActivityForResult(intent, PHOTO_SUCCESS);
    }






    private static void goGallery(Fragment context) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT < 19) { //19以后这个api不可用，demo这里简单处理成图库选择图片
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        context.startActivityForResult(intent, PHOTO_SUCCESS);
    }

    private static Uri goCamera(Fragment context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return goCameraAfter24(context);
        } else {
            return goCameraBefore24(context);
        }
    }
    private static Uri goCamera(Activity context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return goCameraAfter24(context);
        } else {
            return goCameraBefore24(context);
        }
    }

    public static String getPath(Activity context, Uri uri) {
        if (uri != null && uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            return uri.getPath().toString();
        }
        String filePath = "";
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            if (columnIndex > -1)
                filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }




    private static Uri goCameraBefore24(Activity context) {
        File file = new File(context.getExternalCacheDir()
                , System.currentTimeMillis() + ".jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        context.startActivityForResult(intent, CAMERA_SUCCESS);
        return outputFileUri;
    }

    private static Uri goCameraAfter24(Activity context) {
        File file = new File(context.getExternalCacheDir()
                , System.currentTimeMillis() + ".jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri outputFileUri = Uri.fromFile(file);
        Uri outputContentUri = FileProvider.getUriForFile(context, context.getPackageName(), file);

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputContentUri);
        context.startActivityForResult(intent, CAMERA_SUCCESS);

        return outputFileUri;
    }

    private static Uri goCameraBefore24(Fragment context) {
        File file = new File(context.getActivity().getExternalCacheDir()
                , System.currentTimeMillis() + ".jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        context.startActivityForResult(intent, CAMERA_SUCCESS);
        return outputFileUri;
    }

    private static Uri goCameraAfter24(Fragment context) {
        File file = new File(context.getActivity().getExternalCacheDir()
                , System.currentTimeMillis() + ".jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri outputFileUri = Uri.fromFile(file);
        Uri outputContentUri = FileProvider.getUriForFile(context.getContext(), context.getActivity().getPackageName(), file);

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputContentUri);
        context.startActivityForResult(intent, CAMERA_SUCCESS);

        return outputFileUri;
    }

}

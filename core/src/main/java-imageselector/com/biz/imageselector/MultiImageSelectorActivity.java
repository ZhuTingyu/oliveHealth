package com.biz.imageselector;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.biz.http.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Title: MultiImageSelectorActivity
 * Description: 多图选择
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:16/7/27  下午3:11
 *
 * @author dengqinsheng
 * @version 1.0
 */

public class MultiImageSelectorActivity extends AppCompatActivity implements MultiImageSelectorFragment.Callback {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    /**
     * 最大图片选择次数，int类型，默认9
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 图片选择模式，默认多选
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 是否显示相机，默认显示
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "select_result";
    /**
     * 默认选择集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;

    private ArrayList<String> resultList = new ArrayList<>();
    private Toolbar mToolbar;
    private int mDefaultCount;
    private MenuItem mDoneItem;

    public static void selectPicture(Activity context, boolean isShowCamera, int maxCount,
                                     ArrayList<String> iconPaths, int requestCode) {

        Intent intent = new Intent(context, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, isShowCamera);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxCount == 0 ? 1 : maxCount);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MODE_MULTI);//MODE_MULTI
        // 默认选择
        if (iconPaths != null) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, iconPaths);
        }
        context.startActivityForResult(intent, requestCode);
    }


    public static void selectPicture(Activity context, boolean isShowCamera, int requestCode) {

        Intent intent = new Intent(context, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, isShowCamera);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MODE_SINGLE);//MODE_MULTI
        // 默认选择
        /*if (iconPaths != null) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, iconPaths);
        }*/
        context.startActivityForResult(intent, requestCode);
    }

    public static void selectPicture(Activity context, int requestCode) {

        selectPicture(context, true, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageselector_activity_default);
        Intent intent = getIntent();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.getMenu().add(0, 0, 0, R.string.imageselector_action_done).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 0) {
                if (resultList != null && resultList.size() > 0) {
                    // 返回已选择的图片数据
                    Intent data = new Intent();
                    data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
            return false;
        });
        mToolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
        int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commit();


        // 完成按钮
        mDoneItem = mToolbar.getMenu().findItem(0);
        if (resultList == null || resultList.size() <= 0) {
            mDoneItem.setTitle(R.string.imageselector_action_done);
            mDoneItem.setEnabled(false);
        } else {
            updateDoneText();
            mDoneItem.setEnabled(true);
        }

    }

    private void updateDoneText() {
        mDoneItem.setTitle(String.format("%s(%d/%d)",
                getString(R.string.imageselector_action_done), resultList.size(), mDefaultCount));
    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {
        if (!resultList.contains(path)) {
            resultList.add(path);
        }
        // 有图片之后，改变按钮状态
        if (resultList.size() > 0) {
            updateDoneText();
            if (!mDoneItem.isEnabled()) {
                mDoneItem.setEnabled(true);
            }
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if (resultList.contains(path)) {
            resultList.remove(path);
        }
        updateDoneText();
        // 当为选择图片时候的状态
        if (resultList.size() == 0) {
            mDoneItem.setTitle(R.string.imageselector_action_done);
            mDoneItem.setEnabled(false);
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {

            // notify system
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));

            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}

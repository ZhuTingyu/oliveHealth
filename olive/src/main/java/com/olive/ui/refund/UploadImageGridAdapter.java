package com.olive.ui.refund;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.biz.base.BaseActivity;
import com.biz.base.BaseArrayListAdapter;
import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.DrawableHelper;
import com.biz.util.Lists;
import com.biz.util.PhotoUtil;
import com.biz.util.Utils;
import com.biz.widget.CustomDraweeView;
import com.olive.util.LoadImageUtil;

import java.util.List;
import java.util.Random;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/7/29.
 */

public class UploadImageGridAdapter extends BaseArrayListAdapter<String> {

    public static final int CAMERA = 0;
    public static final int GALLERY = 1;
    public int CAMERA_SUCCESS_REQUEST = 2082;
    public int PHOTO_SUCCESS_REQUEST = 2083;

    protected final static int TYPE_ADD = 1;
    protected int maxCount = 3;
    protected int chooseMode = CAMERA;
    protected String hisSelectedPath;

    protected OnImageItemClickListener onImageItemClickListener;
    protected Action1<List<String>> onImageDataChange;

    protected BaseFragment mFragment;

    public interface OnImageItemClickListener {
        void onClick(UploadImageGridAdapter adapter, String path);
    }

    public void setOnImageDataChange(Action1<List<String>> onImageDataChange) {
        this.onImageDataChange = onImageDataChange;
    }

    public void setOnImageItemClickListener(OnImageItemClickListener onImageItemClickListener) {
        this.onImageItemClickListener = onImageItemClickListener;
    }

    public UploadImageGridAdapter(Context context) {
        super(context);
        mList = Lists.newArrayList();
        initCameraRequest();
        initPhotoRequest();
    }

    public void setChooseMode(int chooseMode) {
        this.chooseMode = chooseMode;
    }

    private void initCameraRequest() {
        int max = 2400;
        int min = 2200;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        CAMERA_SUCCESS_REQUEST = s;
    }

    private void initPhotoRequest() {
        int max = 2600;
        int min = 2400;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        CAMERA_SUCCESS_REQUEST = s;
    }

    @Override
    public int getCount() {
        if (super.getCount() == maxCount)
            return super.getCount();
        return super.getCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (super.getCount() != maxCount && position == getCount() - 1) return TYPE_ADD;
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    protected class ImageViewHolder extends BaseViewHolder {
        public CustomDraweeView icon;
        public ImageView del;

        public ImageViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);
            icon = (CustomDraweeView) itemView.findViewById(com.biz.http.R.id.icon);
            del = (ImageView) itemView.findViewById(com.biz.http.R.id.del);

        }

        void bindData(int position) {
            del.setVisibility(View.VISIBLE);
            del.setOnClickListener(v -> {
                removeItem((Integer) v.getTag());
                if (onImageDataChange != null)
                    onImageDataChange.call(mList);
            });
            if (getItem(position).startsWith("file"))
                LoadImageUtil.Builder().loadFile(getItem(position)).file()
                        .build().displayImage(icon);
            else
                LoadImageUtil.Builder().load(getItem(position)).build()
                        .imageOptions(com.biz.http.R.color.white)
                        .displayImage(icon);
        }


    }

    class AddViewHolder extends BaseViewHolder {

        AppCompatImageView addBtn;

        public AddViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);

            addBtn = (AppCompatImageView) itemView;

            addBtn.setScaleType(ImageView.ScaleType.CENTER);
            Drawable normalDrawable = DrawableHelper.createShapeStrokeDrawable(com.biz.http.R.color.color_cccccc, com.biz.http.R.color.color_cccccc, 0);
            Drawable pressedDrawable = DrawableHelper.createShapeStrokeDrawable(com.biz.http.R.color.color_d5d5d5, com.biz.http.R.color.color_e5e5e5, 0);
            Drawable background = DrawableHelper.newSelector(getContext(), normalDrawable, pressedDrawable);

            addBtn.setBackgroundDrawable(background);
            addBtn.setImageResource(com.biz.http.R.drawable.ic_add_black_24dp);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(Utils.dip2px(70), Utils.dip2px(70));
            addBtn.setLayoutParams(params);
            bindData();
        }

        public void bindData() {
            addBtn.setOnClickListener(view -> {
                hisSelectedPath = null;
                if (onImageItemClickListener != null)
                    onImageItemClickListener.onClick(UploadImageGridAdapter.this, null);
            });
        }
    }



    protected void addImage(String url) {
        addItem(url);
        if (onImageDataChange != null)
            onImageDataChange.call(mList);
    }

    protected void replace(String url) {
        if (mList == null || TextUtils.isEmpty(url) || TextUtils.isEmpty(hisSelectedPath)) return;
        for (int i = 0; i < mList.size(); i++) {
            if (hisSelectedPath.equals(mList.get(i))) {
                this.mList.set(i, url);
                if (onImageDataChange != null)
                    onImageDataChange.call(mList);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void delUrl(String url) {
        if (mList == null || TextUtils.isEmpty(url)) return;
        for (int i = 0; i < mList.size(); i++) {
            if (url.equals(mList.get(i))) {
                this.mList.remove(i);
                if (onImageDataChange != null)
                    onImageDataChange.call(mList);
                notifyDataSetChanged();
                break;
            }
        }
    }

}

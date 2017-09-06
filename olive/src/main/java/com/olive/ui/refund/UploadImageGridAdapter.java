package com.olive.ui.refund;

import android.content.Context;
import android.content.Intent;
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
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PhotoUtil;
import com.biz.util.Utils;
import com.biz.widget.CustomDraweeView;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.refund.viewModel.ApplyRefundViewModel;
import com.olive.util.LoadImageUtil;

import java.io.File;
import java.util.List;
import java.util.Random;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/7/29.
 */

public class UploadImageGridAdapter extends BaseArrayListAdapter<String> {

    private int CAMERA_SUCCESS_REQUEST = 2082;
    private int PHOTO_SUCCESS_REQUEST = 2083;
    private int CHOOSE_GOODS__SUCCESS_REQUEST = 2084;

    private static final int CAMERA = 0;
    private static final int GALLERY = 1;

    protected final static int TYPE_ADD = 1;
    protected int maxCount = 3;
    protected int chooseMode = CAMERA;
    protected String hisSelectedPath;
    private int hisSelectedPosition;

    protected OnImageItemClickListener onImageItemClickListener;
    protected Action1<List<String>> onImageDataChange;

    protected BaseFragment mFragment;

    private ApplyRefundViewModel viewModel;

    public interface OnImageItemClickListener {
        void onClick(UploadImageGridAdapter adapter, String path);
    }

    public void setOnImageDataChange(Action1<List<String>> onImageDataChange) {
        this.onImageDataChange = onImageDataChange;
    }

    public void setOnImageItemClickListener(OnImageItemClickListener onImageItemClickListener) {
        this.onImageItemClickListener = onImageItemClickListener;
    }

    public UploadImageGridAdapter(Context context, ApplyRefundViewModel viewModel, BaseFragment fragment) {
        super(context);
        mList = Lists.newArrayList();
        mFragment = fragment;
        this.viewModel = viewModel;
    }

    public void setChooseMode(int chooseMode) {
        this.chooseMode = chooseMode;
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
        if (getItemViewType(position) != TYPE_ADD) {
            convertView = inflater(com.biz.http.R.layout.item_icon_del_layout, parent);
            ImageViewHolder holder = new ImageViewHolder(convertView);
            holder.bindData(position);
            holder.del.setTag(position);
            holder.icon.setTag(getItem(position));
            holder.icon.setOnClickListener(view -> {
                if (onImageItemClickListener != null) {
                    hisSelectedPath = view.getTag().toString();
                    onImageItemClickListener.onClick(this, view.getTag().toString());
                }
            });
        } else {
            convertView = new AppCompatImageView(getContext());
            AddViewHolder holder = new AddViewHolder(convertView);
        }

        return convertView;
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
                delUrl(mList.get((Integer) v.getTag()));
                if (onImageDataChange != null)
                    onImageDataChange.call(mList);
            });

            if (getItem(position).startsWith("file"))
                LoadImageUtil.Builder().loadFile(getItem(position)).file()
                        .build().displayImage(icon);
            else
                LoadImageUtil.Builder().load(new File(getItem(position))).build()
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

            addBtn.setScaleType(ImageView.ScaleType.FIT_XY);

            addBtn.setImageResource(R.drawable.vector_return_goods_photo);
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
                hisSelectedPosition = i;
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
                viewModel.imgUrls.remove(i);
                if (onImageDataChange != null)
                    onImageDataChange.call(mList);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == CAMERA_SUCCESS_REQUEST) {
            if (resultCode == -1) {
                mFragment.setProgressVisible(true);
                viewModel.uploadImg(s -> {
                    mFragment.setProgressVisible(false);
                    if(hisSelectedPath == null){
                        addImage(viewModel.getFileUri());
                        viewModel.imgUrls.add(s);
                    }else {
                        replace(viewModel.getFileUri());
                        viewModel.imgUrls.set(hisSelectedPosition, s);
                    }
                });
            }
        } else if (requestCode == PHOTO_SUCCESS_REQUEST) {
            if (data != null) {
                mFragment.setProgressVisible(true);
                viewModel.setFileUri(data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT).get(0));
                viewModel.uploadImg(s -> {
                    mFragment.setProgressVisible(false);
                    if(hisSelectedPath == null){
                        addImage(viewModel.getFileUri());
                        viewModel.imgUrls.add(s);
                    }else {
                        replace(viewModel.getFileUri());
                        viewModel.imgUrls.set(hisSelectedPosition, s);
                    }
                });
            }
        }
    }

}

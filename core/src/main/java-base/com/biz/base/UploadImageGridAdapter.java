package com.biz.base;

/**
 * Title: UploadImageGridAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2016/9/9  11:50
 *
 * @author wangwei
 * @version 1.0
 */

import com.biz.http.R;
import com.biz.image.BaseLoadImageUtil;
import com.biz.util.DrawableHelper;
import com.biz.util.Lists;
import com.biz.util.PhotoUtil;
import com.biz.util.ToastUtils;
import com.biz.util.Utils;
import com.biz.widget.CustomDraweeView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Title: ImageGridAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:9/8/16  9:57 PM
 *
 * @author johnzheng
 * @version 1.0
 */

public class UploadImageGridAdapter extends BaseArrayListAdapter<String> {
    protected final static int TYPE_ADD = 1;
    protected int maxCount = 3;
    protected UploadImageViewModel viewModel;
    protected OnImageItemClickListener onImageItemClickListener;
    protected Action1<List<String>> onImageDataChange;
    protected String hisSelectedPath;
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

    public void setViewModel(UploadImageViewModel uploadImageViewModel) {
        viewModel = uploadImageViewModel;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public UploadImageGridAdapter(Context context, UploadImageViewModel viewModel, BaseFragment fragment) {
        super(context);
        this.viewModel = viewModel;
        mFragment = fragment;
        mList = Lists.newArrayList();
    }

    public void setFragment(BaseFragment fragment) {
        mFragment = fragment;
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
            convertView = inflater(R.layout.item_icon_del_layout, parent);
            ImageViewHolder holder = new ImageViewHolder(convertView);
            holder.bindData(position);
            holder.del.setTag(position);
            holder.icon.setTag(getItem(position));
            holder.icon.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
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
    protected void  loadImage(String url,CustomDraweeView icon)
    {
        BaseLoadImageUtil.Builder().load(url).build()
                .imageOptions(R.color.white)
                .displayImage(icon);
    }
    public boolean onActivityResult(int requestCode, int resultCode, Intent data,Action1<String> onNext) {
        if (viewModel != null)
          return  viewModel.onActivityResult(requestCode, resultCode, data, url -> {
                if (TextUtils.isEmpty(hisSelectedPath))
                    addImage(url);
                else replace(url);
                Observable.just(url).subscribe(onNext);
            });
        return false;
    }

    private void addImage(String url) {
        addItem(url);
        if (onImageDataChange != null)
            onImageDataChange.call(mList);
    }

    private void replace(String url) {
        if (mList == null || TextUtils.isEmpty(url)||TextUtils.isEmpty(hisSelectedPath)) return;
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

    class ImageViewHolder extends BaseViewHolder {
        CustomDraweeView icon;
        ImageView del;

        public ImageViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);
            icon = (CustomDraweeView) itemView.findViewById(R.id.icon);
            del = (ImageView) itemView.findViewById(R.id.del);

        }

        void bindData(int position) {
            del.setVisibility(View.VISIBLE);
            del.setOnClickListener(v -> {
                removeItem((Integer) v.getTag());
                if (onImageDataChange != null)
                    onImageDataChange.call(mList);
            });
            if (getItem(position).startsWith("file"))
                BaseLoadImageUtil.Builder().loadFile(getItem(position)).file()
                        .build().displayImage(icon);
            else
                loadImage(getItem(position),icon);
        }


    }

    class AddViewHolder extends BaseViewHolder {

        AppCompatImageView addBtn;

        public AddViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);

            addBtn = (AppCompatImageView) itemView;

            addBtn.setScaleType(ImageView.ScaleType.CENTER);
            Drawable normalDrawable = DrawableHelper.createShapeStrokeDrawable(R.color.color_efefef, R.color.color_e5e5e5, 0);
            Drawable pressedDrawable = DrawableHelper.createShapeStrokeDrawable(R.color.color_d5d5d5, R.color.color_e5e5e5, 0);
            Drawable background = DrawableHelper.newSelector(getContext(), normalDrawable, pressedDrawable);

            addBtn.setBackgroundDrawable(background);
            addBtn.setImageResource(R.drawable.ic_add_black_24dp);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(Utils.dip2px(70), Utils.dip2px(70));
            addBtn.setLayoutParams(params);
            bindData();
        }

        public void bindData() {
            addBtn.setOnClickListener(view -> {
                hisSelectedPath = null;
                if(onImageItemClickListener!=null)
                    onImageItemClickListener.onClick(UploadImageGridAdapter.this,null);
                selectImage();
            });
        }
    }

    public void selectImage() {
        PhotoUtil.photo(mFragment,uri -> {
            if (uri!=null && viewModel!=null){
                viewModel.setUri(uri);
            }
        });
    }


}

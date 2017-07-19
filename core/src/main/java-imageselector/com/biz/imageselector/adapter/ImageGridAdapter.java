package com.biz.imageselector.adapter;

import com.biz.http.R;
import com.biz.imageselector.bean.Image;
import com.biz.util.LoadLocalImageUtil;
import com.biz.widget.CustomDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Title: ImageGridAdapter
 * Description: 图片Adapter
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:16/7/27  下午2:35
 *
 * @author dengqinsheng
 * @version 1.0
 */

public class ImageGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private Context mContext;

    private LayoutInflater mInflater;
    private boolean showCamera = true;
    private boolean showSelectIndicator = true;

    private List<Image> mImages = new ArrayList<>();
    private List<Image> mSelectedImages = new ArrayList<>();

    final int mGridWidth;

    public ImageGridAdapter(Context context, boolean showCamera, int column) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showCamera = showCamera;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            width = size.x;
        } else {
            width = wm.getDefaultDisplay().getWidth();
        }
        mGridWidth = width / column;
    }

    /**
     * 显示选择指示器
     */
    public void showSelectIndicator(boolean b) {
        showSelectIndicator = b;
    }

    public void setShowCamera(boolean b) {
        if (showCamera == b) return;

        showCamera = b;
        notifyDataSetChanged();
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    /**
     * 选择某个图片，改变选择状态
     */
    public void select(Image image) {
        if (mSelectedImages.contains(image)) {

            mSelectedImages.remove(image);
        } else {
            mSelectedImages.add(image);
        }
    }

    /**
     * 通过图片路径设置默认选择
     */
    public void setDefaultSelected(ArrayList<String> resultList) {
        for (String path : resultList) {
            Image image = getImageByPath(path);
            if (image != null) {
                mSelectedImages.add(image);
            }
        }
        if (mSelectedImages.size() > 0) {
            notifyDataSetChanged();
        }
    }

    private Image getImageByPath(String path) {
        if (mImages != null && mImages.size() > 0) {
            for (Image image : mImages) {
                if (image.path.equalsIgnoreCase(path)) {
                    return image;
                }
            }
        }
        return null;
    }

    /**
     * 设置数据集
     */
    public void setData(List<Image> images) {
        mSelectedImages.clear();

        if (images != null && images.size() > 0) {
            mImages = images;
        } else {
            mImages.clear();
        }
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {

            View view = mInflater.inflate(R.layout.imageselector_list_item_camera, parent, false);
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = mGridWidth;
            lp.width = mGridWidth;
            view.setLayoutParams(lp);
            return new ViewHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.list_item_image, parent, false);
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = mGridWidth;
            lp.width = mGridWidth;
            view.setLayoutParams(lp);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if (getItemViewType(position) != TYPE_CAMERA)
            holder.bindData(getItem(position));
        holder.itemView.setOnClickListener(v -> {
            mOnItemClickListener.onItemClick(null, viewHolder.itemView, position, position);
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera) {
            return position == 0 ? TYPE_CAMERA : TYPE_NORMAL;
        }
        return TYPE_NORMAL;
    }


    public int getCount() {
        return showCamera ? mImages.size() + 1 : mImages.size();
    }


    public Image getItem(int i) {
        if (showCamera) {
            if (i == 0) {
                return null;
            }
            return mImages.get(i - 1);
        } else {
            return mImages.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (mImages != null && mImages.size() > 0) return mImages.size();
        else return 0;
    }


    public View getView(int i, View view, ViewGroup viewGroup) {

        if (isShowCamera()) {
            if (i == 0) {
                view = mInflater.inflate(R.layout.imageselector_list_item_camera, viewGroup, false);
                return view;
            }
        }

        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_image, viewGroup, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (holder != null) {
            holder.bindData(getItem(i));
        }

        return view;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CustomDraweeView image;
        CheckBox indicator;
        View mask;

        ViewHolder(View view) {
            super(view);
            image = (CustomDraweeView) view.findViewById(R.id.image);
            indicator = (CheckBox) view.findViewById(R.id.checkmark);
            mask = view.findViewById(R.id.mask);
            view.setTag(this);
        }

        void bindData(final Image data) {
            if (data == null) return;
            // 处理单选和多选状态
            if (showSelectIndicator) {
                indicator.setVisibility(View.VISIBLE);
                if (mSelectedImages.contains(data)) {
                    // 设置选中状态
                    indicator.setChecked(true);
                    mask.setVisibility(View.VISIBLE);
                } else {
                    // 未选择
                    indicator.setChecked(false);
                    mask.setVisibility(View.GONE);
                }
            } else {
                indicator.setVisibility(View.GONE);
            }
            File imageFile = new File(data.path);
            if (imageFile.exists()) {

                Uri uri = Uri.fromFile(imageFile);


                LoadLocalImageUtil.Builder().load(uri).file().build().displayImage(image);

            } else {
                Uri uri = LoadLocalImageUtil.Builder().load(R.drawable.imageselector_default_error).drawable().getImageLoadUri();
                image.setImageURI(uri);
            }
        }
    }

}

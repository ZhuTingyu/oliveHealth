package com.olive.ui.refund;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biz.base.BaseActivity;
import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.BitmapUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PhotoUtil;
import com.biz.util.PriceUtil;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.biz.widget.ExpandGridView;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.adapter.BottomSheetAdapter;
import com.olive.ui.refund.viewModel.ApplyRefundViewModel;
import com.olive.util.LoadImageUtil;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by TingYu Zhu on 2017/7/29.
 */

public class ApplyRefundFragment extends BaseFragment {
    private int CAMERA_SUCCESS_REQUEST = 2082;
    private int PHOTO_SUCCESS_REQUEST = 2083;
    private int CHOOSE_GOODS__SUCCESS_REQUEST = 2084;

    private static final int CAMERA = 0;
    private static final int GALLERY = 1;

    private List<View> uploadImgs;


    private EditText describe;

    private ApplyRefundViewModel viewModel;

    LinearLayout imgsLinearLayout;

    private int current;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ApplyRefundViewModel(context);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apply_refund, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.title_apply_refund));
        initView();
    }

    private void initView() {
        TextView chooseGoods = findViewById(R.id.choose_goods);
        TextView reason = findViewById(R.id.reason);
        TextView ok = findViewById(R.id.btn_sure);
        imgsLinearLayout = findViewById(R.id.ll_img);
        describe = findViewById(R.id.describe);

        chooseGoods.setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .startParentActivity(getActivity(), ChooseRefundGoodsFragment.class, CHOOSE_GOODS__SUCCESS_REQUEST);
        });

        reason.setOnClickListener(v -> {
            List<String> list = Lists.newArrayList(getContext().getResources().getStringArray(R.array.array_reason));
            select(list, p -> {
                reason.setText(list.get(p));
            });
        });

        initUploadImages();

        bindUi(RxUtil.click(ok), o -> {

        });


    }

    private void initUploadImages() {
        uploadImgs = Lists.newArrayList();
        List<String> list = Lists.newArrayList(getContext().getResources().getStringArray(R.array.array_photo));
        for (int i = 0; i < 2; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_icon_del_layout, imgsLinearLayout, false);
            view.setOnClickListener(v -> {
                current = (int) view.getTag();
                select(list, p -> {
                    if (p == CAMERA) {
                        goCamera();
                    } else {
                        goGallery();
                    }
                });
            });
            view.setTag(i);
            imgsLinearLayout.addView(view);
            uploadImgs.add(view);
        }

    }

    private void setImg(View view, String url) {
        AppCompatImageView imageView = (AppCompatImageView) view.findViewById(R.id.icon);
        imageView.setImageBitmap(BitmapUtil.getCropBitmapFromFile(url, 70, 70).get());
        view.findViewById(R.id.del).setOnClickListener(v -> {
            imageView.setImageResource(R.drawable.vector_return_goods_photo);
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_GOODS__SUCCESS_REQUEST) {
            if (data != null) {
                ProductEntity productEntity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                if (productEntity != null) {
                    initGoodsInfoView(productEntity);
                }
            }
        } else if (requestCode == CAMERA_SUCCESS_REQUEST) {
            if (data == null) {
                setProgressVisible(true);
                viewModel.uploadImg(s -> {
                    setProgressVisible(false);
                    setImg(uploadImgs.get(current), viewModel.getFileUri());
                    viewModel.setImgUrl(current, viewModel.getFileUri());
                });
            }
        } else if (requestCode == PHOTO_SUCCESS_REQUEST) {
            if (data != null) {
                setProgressVisible(true);
                viewModel.setFileUri(data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT).get(0));
                viewModel.uploadImg(s -> {
                    setProgressVisible(false);
                    setImg(uploadImgs.get(current), viewModel.getFileUri());
                    viewModel.setImgUrl(current, viewModel.getFileUri());
                });
            }
        }
    }

    private void initGoodsInfoView(ProductEntity productEntity) {

        RelativeLayout info = findViewById(R.id.goods_info);
        info.setVisibility(View.VISIBLE);

        BaseViewHolder holder = new BaseViewHolder(info);

        LoadImageUtil.Builder()
                .load(productEntity.imgLogo).http().build()
                .displayImage(holder.getView(R.id.icon_img));
        holder.setText(R.id.title, productEntity.name);
        holder.setText(R.id.title_line_2, getString(R.string.text_product_specification, productEntity.standard));
        holder.setText(R.id.title_line_3, PriceUtil.formatRMB(productEntity.price));
        holder.getView(R.id.checkbox).setVisibility(View.GONE);
        holder.getView(R.id.number_layout).setVisibility(View.GONE);
        holder.getView(R.id.text_product_number).setVisibility(View.VISIBLE);
        holder.setText(R.id.text_product_number, "x" + productEntity.quantity);

        RelativeLayout rl = findViewById(R.id.rl_info);
        rl.setPadding(0, 0, Utils.dip2px(24), 0);

        holder.findViewById(R.id.right_icon).setVisibility(View.VISIBLE);

        info.setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .startParentActivity(getActivity(), ChooseRefundGoodsFragment.class, CHOOSE_GOODS__SUCCESS_REQUEST);
        });
    }

    public void select(List<String> list, BottomSheetAdapter.OnItemClickListener onItemClickListener) {
        BottomSheetAdapter.createBottomSheet(getContext(), list, onItemClickListener);
    }

    private void goCamera() {

        PhotoUtil.photo(this, uri -> {
            viewModel.setFileUri(PhotoUtil.getPath(getActivity(), uri));
        });

    }

    private void goGallery() {
        BaseActivity baseActivity = (BaseActivity) getContext();
        MultiImageSelector.create()
                .showCamera(true)
                .single()
                .start(baseActivity, PHOTO_SUCCESS_REQUEST);
    }

}

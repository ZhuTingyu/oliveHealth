package com.olive.ui.refund;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
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
import com.biz.util.ToastUtils;
import com.biz.widget.ExpandGridView;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.BaseErrorFragment;
import com.olive.ui.adapter.BottomSheetAdapter;
import com.olive.ui.adapter.ProductInfoWithNumberAdapter;
import com.olive.ui.refund.viewModel.ApplyRefundViewModel;

import com.olive.widget.LinearLayoutForRecyclerView;


import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by TingYu Zhu on 2017/7/29.
 */

public class ApplyRefundFragment extends BaseErrorFragment {
    private int CAMERA_SUCCESS_REQUEST = 2082;
    private int PHOTO_SUCCESS_REQUEST = 2083;
    public static final int CHOOSE_GOODS__SUCCESS_REQUEST = 2084;

    private static final int CAMERA = 0;
    private static final int GALLERY = 1;

    protected List<RelativeLayout> productInfoViews;


    protected EditText describe;

    private ApplyRefundViewModel viewModel;

    protected GridView imgsGrid;
    protected LinearLayout imgsLinearLayout;
    protected LinearLayoutForRecyclerView info;


    private int current;

    private TextView chooseGoods;
    protected TextView reason;
    protected TextView ok;

    private UploadImageGridAdapter adapter;


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

        chooseGoods = findViewById(R.id.choose_goods);
        reason = findViewById(R.id.reason);
        ok = findViewById(R.id.btn_sure);
        imgsLinearLayout = findViewById(R.id.ll_img);
        imgsGrid = findViewById(R.id.img_grid);
        describe = findViewById(R.id.describe);
        describe.clearFocus();

        initView();
    }

    protected void initView() {
        bindUi(RxUtil.textChanges(describe), viewModel.setDescription());

        chooseGoods.setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .startParentActivity(getActivity(), ChooseRefundGoodsFragment.class, CHOOSE_GOODS__SUCCESS_REQUEST);
        });

        reason.setOnClickListener(v -> {
            viewModel.getRefundReason(refundReasonEntities -> {
                select(viewModel.getRefundNameList(), p -> {
                    reason.setText(viewModel.getRefundNameList().get(p));
                    viewModel.setRefundReasonId(refundReasonEntities.get(p).id);
                });
            });

        });

        initUploadImages();

        bindUi(RxUtil.click(ok), o -> {
            viewModel.applyRefund(s -> {
                ToastUtils.showShort(getContext(), getString(R.string.text_commit_success));
                getActivity().setResult(0, new Intent());
                getActivity().finish();
            });

        });


    }

    private void initUploadImages() {


        List<String> list = Lists.newArrayList(getContext().getResources().getStringArray(R.array.array_photo));
        adapter = new UploadImageGridAdapter(getContext(), viewModel, this);
        imgsGrid.setNumColumns(4);
        imgsGrid.setAdapter(adapter);
        adapter.setOnImageItemClickListener((adapter1, path) -> {
            select(list, p -> {
                if (p == CAMERA) {
                    goCamera();
                } else {
                    goGallery();
                }
            });
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_GOODS__SUCCESS_REQUEST) {
            if (data != null) {
                List<ProductEntity> productEntities = data.getParcelableArrayListExtra(IntentBuilder.KEY_DATA);
                if (productEntities != null && !productEntities.isEmpty()) {
                    viewModel.setChooseRefundProduct(productEntities);
                    initGoodsInfoView(productEntities, false);
                }
            }
        }
    }

    protected void initGoodsInfoView(List<ProductEntity> productEntities, boolean isLook) {

        if (productEntities.isEmpty()) {
            return;
        }

        info = findViewById(R.id.goods_info);
        info.setVisibility(View.VISIBLE);
        productInfoViews = Lists.newArrayList();

        ProductInfoWithNumberAdapter adapter = new ProductInfoWithNumberAdapter(getContext(), productEntities, isLook);
        info.setAdapter(adapter);

        if (!isLook) {
            info.setOnClickListener(v -> {
                IntentBuilder.Builder()
                        .startParentActivity(getActivity(), ChooseRefundGoodsFragment.class, CHOOSE_GOODS__SUCCESS_REQUEST);
            });
        }

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

    @Override
    public void error(int code, String error) {
        if (code == 500) {
            error(getString(R.string.message_perfect_info));
            return;
        }
        super.error(code, error);
    }
}

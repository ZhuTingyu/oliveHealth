package com.olive.ui.refund;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biz.base.BaseActivity;
import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PhotoUtil;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.biz.widget.ExpandGridView;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.adapter.BottomSheetAdapter;
import com.olive.ui.refund.viewModel.ApplyRefundViewModel;
import com.olive.util.LoadImageUtil;

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

    private View uploadImg1;
    private View uploadImg2;


    private EditText describe;

    private ApplyRefundViewModel viewModel;

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
        uploadImg1 = findViewById(R.id.upload1);
        uploadImg2 = findViewById(R.id.upload2);
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

        List<String> list = Lists.newArrayList(getContext().getResources().getStringArray(R.array.array_photo));

        uploadImg1.setOnClickListener(v -> {
            select(list, p -> {
                if (list.get(p).equals(list.get(0))) {
                    goCamera();
                } else {
                    goGallery();
                }
            });
        });

        uploadImg2.setOnClickListener(v -> {

        });


        bindUi(RxUtil.click(ok), o -> {

        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_GOODS__SUCCESS_REQUEST) {
            if(data != null){
                ProductEntity productEntity  = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                if(productEntity != null){
                    initGoodsInfoView(productEntity);
                }
            }
        } else if (requestCode == CAMERA_SUCCESS_REQUEST) {
            if(data == null){
                viewModel.uploadImg(s -> {

                });
            }
        } else if (requestCode == PHOTO_SUCCESS_REQUEST){
            if(data != null){
                viewModel.setFileUri(data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT).get(0));
                viewModel.uploadImg(s -> {

                });
            }
        }
    }

    private void initGoodsInfoView(ProductEntity productEntity) {

        RelativeLayout info = findViewById(R.id.goods_info);
        info.setVisibility(View.VISIBLE);

        BaseViewHolder holder = new BaseViewHolder(info);

        LoadImageUtil.Builder()
                .load("http://img13.360buyimg.com/imgzone/jfs/t6517/304/1921907774/343777/df918f69/595a01f6Ne19fc737.jpg").http().build()
                .displayImage(holder.getView(R.id.icon_img));
        holder.setText(R.id.title, "产品名称");
        holder.setText(R.id.title_line_2, "规格：1000mg*100粒");
        holder.setText(R.id.title_line_3, "¥ 7908.00");
        holder.getView(R.id.checkbox).setVisibility(View.GONE);
        holder.getView(R.id.number_layout).setVisibility(View.GONE);
        holder.getView(R.id.text_product_number).setVisibility(View.VISIBLE);
        holder.setText(R.id.text_product_number, "x3");

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
            viewModel.setFileUri(PhotoUtil.getPath(getActivity(),uri));
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

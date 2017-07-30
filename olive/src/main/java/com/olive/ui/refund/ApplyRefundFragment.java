package com.olive.ui.refund;

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
import android.widget.TextView;

import com.biz.base.BaseActivity;
import com.biz.base.BaseFragment;
import com.biz.util.Lists;
import com.biz.util.PhotoUtil;
import com.biz.util.RxUtil;
import com.biz.widget.ExpandGridView;
import com.olive.R;
import com.olive.ui.adapter.BottomSheetAdapter;

import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by TingYu Zhu on 2017/7/29.
 */

public class ApplyRefundFragment extends BaseFragment {
    private int CAMERA_SUCCESS_REQUEST = 2082;
    private int PHOTO_SUCCESS_REQUEST = 2083;

    private static final int CAMERA = 0;
    private static final int GALLERY = 1;


    private EditText describe;

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
        describe = findViewById(R.id.describe);

        chooseGoods.setOnClickListener(v -> {

        });

        reason.setOnClickListener(v -> {
            List<String> list = Lists.newArrayList(getContext().getResources().getStringArray(R.array.array_reason));
            select(list,p -> {
                reason.setText(list.get(p));
            });
        });

        bindUi(RxUtil.click(ok), o -> {

        });


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void select(List<String> list, BottomSheetAdapter.OnItemClickListener onItemClickListener) {
        BottomSheetAdapter.createBottomSheet(getContext(),list,onItemClickListener);
    }

    private void goCamera() {

        PhotoUtil.photo(this, uri -> {
                /*if (uri != null && viewModel != null) {
                    viewModel.setUri(uri);
                }*/
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

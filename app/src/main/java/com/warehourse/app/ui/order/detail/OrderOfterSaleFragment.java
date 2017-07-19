package com.warehourse.app.ui.order.detail;


import com.biz.base.BaseFragment;
import com.biz.base.BaseRecyclerViewAdapter;
import com.biz.base.BaseViewHolder;
import com.biz.base.FragmentBackHelper;
import com.biz.util.DialogUtil;
import com.biz.util.DrawableHelper;
import com.biz.util.Lists;
import com.biz.util.LogUtil;
import com.biz.util.PhotoUtil;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.biz.widget.CustomDraweeView;
import com.biz.widget.MaterialEditText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.drawable.ScalingUtils;
import com.warehourse.app.R;
import com.warehourse.app.ui.bottomsheet.BottomSheetBuilder;
import com.warehourse.app.ui.bottomsheet.BottomSheetMultipleItem;
import com.warehourse.app.util.LoadImageUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;


import rx.Observable;

/**
 * Created by johnzheng on 5/25/16.
 */
public class OrderOfterSaleFragment extends BaseFragment implements FragmentBackHelper {

    Button btnOk;
    RadioGroup group1;
    RadioGroup group2;
    MaterialEditText editPromo;
    RecyclerView imageList;
    private ImageAdapter adapter;
    BottomSheetDialog dialog;
    Uri uriCamera;
    private OrderApplyRefundViewModel viewModel;


    @Override
    public boolean onBackPressed() {
        if (true) {
            DialogUtil.createDialogViewWithCancel(getActivity(), R.string.dialog_title_notice,
                    R.string.dialog_msg_cancel_order,
                    (dialog, which) -> {
                        getActivity().finish();
                    }, R.string.btn_confirm);
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new OrderApplyRefundViewModel(this);
        initViewModel(viewModel);
        adapter = new ImageAdapter(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_after_sale, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.title_apply_sale);

        group1 = findViewById(R.id.group_1);
        group2 = findViewById(R.id.group_2);
        editPromo = findViewById(R.id.edit_promo);
        imageList = findViewById(R.id.imageList);
        btnOk = findViewById(R.id.btn_ok);
        imageList.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        bindUi(RxUtil.radioGroupCheckedChanges(group1).map(i -> {
            if (i == R.id.radio_1) {
                return OrderApplyRefundViewModel.TYPE_PRODUCT_RETURN;
            } else if (i == R.id.radio_2) {
                return OrderApplyRefundViewModel.TYPE_PRODUCT_CHANGE;
            } else return -1;
        }), viewModel.setType());

        bindUi(RxUtil.radioGroupCheckedChanges(group2).map(i -> {
            if (i == R.id.radio_3) {
                return OrderApplyRefundViewModel.REASON_ADVENT;
            } else if (i == R.id.radio_4) {
                return OrderApplyRefundViewModel.REASON_DAMAGED;
            } else if (i == R.id.radio_5) {
                return OrderApplyRefundViewModel.REASON_PROBLEM;
            } else return -1;
        }), viewModel.setReason());

        bindUi(RxUtil.textChanges(editPromo), viewModel.setDescription());
        bindUi(RxUtil.click(btnOk), o -> {
            setProgressVisible(true);
            viewModel.submitRequest(b -> {
                setProgressVisible(false);
                getActivity().setResult(Activity.RESULT_OK);
                DialogUtil.createDialogViewWithFinish(getContext(), getString(R.string.dialog_apply_sale_success));
            });
        });
        imageList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == PhotoUtil.CAMERA_SUCCESS) {
            setIcon(uriCamera.getPath());
        } else if (requestCode == PhotoUtil.PHOTO_SUCCESS) {
            Uri originalUri = intent.getData();
            setIcon(PhotoUtil.getPath(getActivity(), originalUri));
        }

    }

    private void setIcon(String url) {
        setProgressVisible(true);
        viewModel.upload(url, s -> {
            setProgressVisible(false);
            LogUtil.print("upload:" + s);
            adapter.addImage(s);
        });
    }


    void createBottomSheet(String url) {
        dialog = BottomSheetBuilder.createPhotoBottomSheet(getActivity(),
                (BaseQuickAdapter adapter, View v, int index) -> {
                    BottomSheetMultipleItem item = (BottomSheetMultipleItem) adapter.getItem(index);
                    if (item != null) {
                        switch (item.getItemType()) {
                            case BottomSheetMultipleItem.GALLERY:
                                PhotoUtil.gallery(this);
                                if (dialog != null)
                                    dialog.dismiss();
                                break;
                            case BottomSheetMultipleItem.CAMERA:
                                PhotoUtil.photo(this,uri -> {
                                    uriCamera =uri;
                                });
                                if (dialog != null)
                                    dialog.dismiss();
                                break;
                            case BottomSheetMultipleItem.CANCEL:
                                if (dialog != null)
                                    dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
    }

    public class ViewHolder extends BaseViewHolder {

        CustomDraweeView icon;
        AppCompatImageView btnClose;

        public ViewHolder(View view) {
            super(view);
            icon = findViewById(R.id.icon);
            btnClose = findViewById(R.id.btn_close);
            icon.setPlaceholderId(R.drawable.ic_plus);
            icon.setPlaceholderScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            btnClose.setVisibility(View.INVISIBLE);
            icon.setBackgroundDrawable(DrawableHelper.createShapeStrokeDrawable(R.color.white, R.color.color_9b9b9b, 1, 0));
        }
    }

    public class ImageAdapter extends BaseRecyclerViewAdapter<String> {
        private final static int TYPE_ADD = 12;
        private final static int TYPE_IMAGE = 13;

        protected ImageAdapter(Context context) {
            super(context);
            mList = Lists.newArrayList();
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(inflater(R.layout.item_after_sale_image, parent));
        }

        @Override
        public int getItemViewType(int position) {
            if (mList.size() == 3) return TYPE_IMAGE;
            if (mList.size() == position) return TYPE_ADD;
            return TYPE_IMAGE;
        }

        public void removeList(int position) {
            if (mList.size() > position && mList.size() > 0)
                mList.remove(position);
        }

        @Override
        public int getItemCount() {
            if (mList.size() < 3) return mList.size() + 1;
            return mList.size();
        }

        public void addImage(String image) {
            if (mList.size() >= 3) return;
            mList.add(image);
            Observable.just(mList).subscribe(viewModel.setImage());
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            if (getItemViewType(position) == TYPE_IMAGE) {
                viewHolder.btnClose.setVisibility(View.VISIBLE);
                viewHolder.btnClose.setOnClickListener(e -> {
                    removeList(position);
                    notifyDataSetChanged();
                });
                LoadImageUtil.Builder().load(getItem(position)).build().imageOptions(R.color.white).displayImage(viewHolder.icon);
            } else {
                viewHolder.btnClose.setVisibility(View.INVISIBLE);
                LoadImageUtil.Builder().load("").build().imageOptions(R.drawable.ic_plus).displayImage(viewHolder.icon);
                viewHolder.icon.setOnClickListener(v -> {
                    createBottomSheet("");
                });
            }
        }
    }
}

package com.warehourse.app.ui.register;


/**
 * Created by johnzheng on 3/18/16.
 */


import com.biz.base.BaseViewHolder;
import com.biz.base.FragmentBackHelper;
import com.biz.util.DialogUtil;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.biz.widget.MaterialEditText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.ShopTypeEntity;
import com.warehourse.app.ui.bottomsheet.BottomSheetBuilder;
import com.warehourse.app.ui.my.BaseAddressFragment;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rx.Observable;


public class Register2Fragment extends BaseAddressFragment implements FragmentBackHelper {


    BottomSheetDialog dialog;

    String name;
    String type;
    String area;
    String detail;
    String identity;

    public boolean isChanged() {
        if (name != null && !name.equals(editUsername.getText().toString()))
            return true;
        if (type != null && !type.equals(editType.getText().toString())) return true;
        if (area != null && !area.equals(editArea.getText().toString())) return true;
        if (detail != null && !detail.equals(editDetail.getText().toString()))
            return true;
        return identity != null && !identity.equals(editIdentity.getText().toString());
    }

    @Override
    public boolean onBackPressed() {
        if (isChanged()) {
            DialogUtil.createDialogViewWithCancel(getActivity(), R.string.dialog_title_notice,
                    R.string.dialog_msg_saved,
                    (dialog, which) -> {
                        getActivity().finish();
                    }, R.string.btn_confirm);
            return true;
        } else if (null != getBaseActivity().getSupportFragmentManager()
                .findFragmentByTag(Register1Fragment.class.getName())) {
            ActivityCompat.finishAffinity(getActivity());
            return true;
        } else return false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new EditShopDetailViewModel(this);
        initViewModel(viewModel);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editArea.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NONE);
        editType.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NONE);
        EditShopDetailViewModel editShopDetailViewModel = (EditShopDetailViewModel) viewModel;
//        bindUi(RxUtil.click(editArea), o -> {
//            setProgressVisible(true);
//            viewModel.province(list -> {
//                setProgressVisible(false);
//                createSheet(list);
//
//            });
//        });
        bindUi(RxUtil.click(editType), o -> {
            setProgressVisible(true);
            editShopDetailViewModel.shopTypes(list -> {
                setProgressVisible(false);
                createType(list);
            });
        });
        bindUi(RxUtil.textChanges(editUsername), editShopDetailViewModel.setName());
        bindUi(RxUtil.textChanges(editDetail), editShopDetailViewModel.setAddress());
        bindUi(RxUtil.click(btnOk), o -> {
            setProgressVisible(true);
            editShopDetailViewModel.updateData(b -> {
                setProgressVisible(false);
                if (UserModel.getInstance().getEditPhoto() != 30 &&
                        UserModel.getInstance().getEditPhoto() != 20) {
                    Intent intent = new Intent(getActivity(), Register3Activity.class);
                    startActivity(intent);
                } else {
                    startActivity(RegisterDoneFragment.class);
                }
                finish();
            });

        });
        bindUi(RxUtil.textChanges(editIdentity), editShopDetailViewModel.setIdentity());
        bindData(editShopDetailViewModel.getName(), RxUtil.text(editUsername));
        bindData(editShopDetailViewModel.getType(), RxUtil.text(editType));
        bindData(editShopDetailViewModel.getArea(), RxUtil.text(editArea));
        bindData(editShopDetailViewModel.getAddress(), RxUtil.text(editDetail));
        bindData(editShopDetailViewModel.getCorporateName(), RxUtil.text(editIdentity));
        name = editUsername.getText().toString();
        type = editType.getText().toString();
        area = editArea.getText().toString();
        detail = editDetail.getText().toString();
        identity = editIdentity.getText().toString();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EditShopDetailViewModel editShopDetailViewModel = (EditShopDetailViewModel) viewModel;
        setProgressVisible(true);
        editShopDetailViewModel.requestDetail(b -> {
            setProgressVisible(false);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.title_fill_profile);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    void createType(List<ShopTypeEntity> list) {
        if (dialog != null) {
            dialog.dismiss();
            dialog=null;
        }
        dialog = BottomSheetBuilder.createBottomSheet(getActivity(), new ShopAdapter(list));
        dialog.show();
    }


    class ShopAdapter extends BaseQuickAdapter<ShopTypeEntity, BaseViewHolder> {


        protected ShopAdapter(List list) {
            super(R.layout.item_single_text_layout, list);
        }


        @Override
        protected void convert(BaseViewHolder holder, ShopTypeEntity shopTypeEntity) {
            holder.setText(R.id.title, shopTypeEntity == null ? "" : shopTypeEntity.name);
            bindUi(RxUtil.click(holder.itemView), o -> {
                Observable.just(shopTypeEntity.id).subscribe(((EditShopDetailViewModel) viewModel).setShopType());
                Observable.just(shopTypeEntity.name).subscribe(RxUtil.text(editType));
                if (dialog != null) dialog.dismiss();
            });
        }
    }
}


package com.warehourse.app.ui.coupon;


import com.biz.base.FragmentBackHelper;
import com.biz.base.FragmentParentActivity;
import com.biz.util.IntentBuilder;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.biz.widget.recyclerview.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.VoucherEntity;
import com.warehourse.app.ui.base.EmptyViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import rx.Observable;
import rx.functions.Action1;

import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;


public class ChooseCouponFragment extends BaseCouponListFragment implements FragmentBackHelper {

    XRecyclerView recyclerView;
    RadioButton titleNoUse;
    TextView titleTotal;

    private ChooseCouponViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ChooseCouponViewModel(this);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_money_layout, container, false);
        return view;
    }

    private Action1<String> setPriceTitle() {
        return s -> {
            titleTotal.setText(Html.fromHtml(
                    "<font color='#4a4a4a'>" + getString(R.string.text_money_fit) + "</font>    "
                            + "<font color='red'>" + s + "</font>"));
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = findViewById(R.id.list);
        titleNoUse = findViewById(R.id.title_no_use);
        titleTotal = findViewById(R.id.title_total);
        bindData(viewModel.getPrice(), setPriceTitle());
        addItemDecorationLine(recyclerView.getRecyclerView());
        titleNoUse.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                viewModel.setSelectedVoucherInfo(null);
                adapter.notifyDataSetChanged();
            }
        });

        if (null != mToolbar) {
            mToolbar.getMenu().add(0, 0, 0, R.string.text_use_info)
                    .setShowAsAction(SHOW_AS_ACTION_ALWAYS);
            mToolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == 0) {
                    IntentBuilder.Builder().startParentActivity(getActivity(), PrivateFragment.class);
                }
                return false;
            });
        }
        setTitle(R.string.title_use_money);
        if (adapter == null) {
            adapter = new ChooseAdapter();
        }
        recyclerView.setAdapter(adapter);
        emptyView(adapter);
        initData();
    }

    @Override
    public void error(String error) {
        super.error(error);
    }

    private void initData() {
        setProgressVisible(true);
        viewModel.request(list -> {
            setProgressVisible(false);
            adapter.setNewData(list);
        });
    }
    private void emptyView(BaseQuickAdapter adapter) {
        EmptyViewHolder holder = EmptyViewHolder.createHolder(getContext())
                .setIcon(R.color.color_transparent)
                .setTitle(R.string.text_empty_coupon);
        adapter.setEmptyView(holder.itemView);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.onDestroy();
    }

    @Override
    public boolean onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(IntentBuilder.KEY_DATA, viewModel.getSelectedVoucherInfo());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
        return true;
    }


    class ChooseAdapter extends CouponAdapter {

        @Override
        protected void convert(CouponViewHolder holder, VoucherEntity voucherInfo) {
            super.convert(holder, voucherInfo);
            holder.iconStatus.setVisibility(View.GONE);
            holder.setCheckedLayout(true);
            holder.setExpiredView(false);
            VoucherEntity selectedVoucherInfo = viewModel.getSelectedVoucherInfo();
            boolean _isSelected = false;
            if (selectedVoucherInfo != null && selectedVoucherInfo.voucherTypeId == voucherInfo.voucherTypeId) {
                _isSelected = true;
            }
            final boolean isSelected = _isSelected;
            holder.disableView.setVisibility(View.GONE);
            holder.addView.setVisibility(View.VISIBLE);
            holder.setLayoutEditEnable(isSelected);
            holder.checkbox.setChecked(isSelected);
            holder.editCount.clearTextChangedListeners();
            holder.editCount.setText(voucherInfo.quantity <= 0 ? "" : ("" + voucherInfo.quantity));
            bindUi(RxUtil.textMaxCount(
                    holder.editCount,
                    voucherInfo.maxCount,
                    getString(R.string.text_money_max_count, "" + voucherInfo.maxCount)), s -> {
                int count = Utils.getInteger(s);
                if (voucherInfo.quantity != count)
                    voucherInfo.quantity = count;
                if (holder.checkbox.isChecked())
                    viewModel.setVoucherCount(voucherInfo);
            });
            holder.textCount.setText("x" + voucherInfo.remainAmount);
            bindUi(RxUtil.click(holder.btnAdd), o -> {
                voucherInfo.quantity = voucherInfo.quantity + 1;
                holder.editCount.setText("" + voucherInfo.quantity);
                viewModel.setVoucherCount(voucherInfo);
            });
            bindUi(RxUtil.click(holder.btnMin), o -> {
                voucherInfo.quantity = voucherInfo.quantity - 1;
                if (voucherInfo.quantity < 0) voucherInfo.quantity = 0;
                holder.editCount.setText(voucherInfo.quantity <= 0 ? "0" : (voucherInfo.quantity + ""));
                viewModel.setVoucherCount(voucherInfo);
            });
            bindUi(RxUtil.click(holder.itemView), o -> {
                titleNoUse.setChecked(false);
                viewModel.setSelectedVoucherInfo(voucherInfo);
                notifyDataSetChanged();
            });
        }


    }


}

package com.olive.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.util.PriceUtil;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.event.UpdateCartEvent;
import com.olive.model.OrderModel;
import com.olive.model.UserModel;
import com.olive.model.entity.AccountEntity;
import com.olive.model.entity.AddressEntity;
import com.olive.ui.BaseErrorFragment;
import com.olive.ui.adapter.CheckOrderAdapter;
import com.olive.ui.main.my.address.AddressManageFragment;
import com.olive.ui.main.my.address.AddressViewModel;
import com.olive.ui.order.viewModel.CheckInfoViewModel;
import com.olive.ui.order.viewModel.OrderViewModel;

import de.greenrobot.event.EventBus;

/**
 * Created by TingYu Zhu on 2017/7/26.
 */

public class CheckOrderInfoFragment extends BaseErrorFragment {

    private int ADDRESS_CODE = 0x123;


    private XRecyclerView recyclerView;
    private CheckOrderAdapter adapter;
    private TextView price;
    private TextView number;
    private CheckInfoViewModel viewModel;
    private AddressViewModel addressViewModel;
    private View head;
    private AddressEntity addressEntity;
    private AccountEntity accountEntity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new CheckInfoViewModel(context);
        addressViewModel = new AddressViewModel(context);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_with_button_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.text_order_sure));
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CheckOrderAdapter();
        adapter.setNewData(viewModel.getProductEntities());
        recyclerView.setAdapter(adapter);

        TextView ok = findViewById(R.id.btn_sure);

        viewModel.getAccountInfo(account -> {
            accountEntity = account;
            if (viewModel.isHasDebt()) {
                findViewById(R.id.rl_debt).setVisibility(View.VISIBLE);
                TextView debt = findViewById(R.id.text_debt);
                debt.setText(PriceUtil.formatRMB(accountEntity.debt));
                ok.setText(R.string.text_go_refund_money);
                ok.setOnClickListener(v -> {
                    IntentBuilder.Builder()
                            // TODO: 2017/9/3 获取欠款订单 
                            .startParentActivity(getActivity(), PayDebtFragment.class, true);
                });
            } else {
                ok.setOnClickListener(v -> {
                    setProgressVisible(true);
                    viewModel.setProductEntities(viewModel.getProductEntities());
                    viewModel.setAddressId(addressEntity.id);
                    viewModel.createOrder(orderEntity -> {
                        setProgressVisible(false);
                        IntentBuilder.Builder()
                                .putExtra(IntentBuilder.KEY_VALUE, accountEntity)
                                .putExtra(IntentBuilder.KEY_DATA, orderEntity)
                                .startParentActivity(getActivity(), PayOrderFragment.class, true);
                        EventBus.getDefault().post(new UpdateCartEvent());
                        getActivity().finish();
                    });

                });
            }
        });
        initHeadView();
        initFoodView();
    }

    private void initFoodView() {
        View footer = View.inflate(getContext(), R.layout.item_check_order_footer_layout, null);
        BaseViewHolder holder = new BaseViewHolder(footer);
        holder.setText(R.id.text_price, PriceUtil.formatRMB(viewModel.getTotalPrice()));
        holder.setText(R.id.text_product_number, getString(R.string.text_order_list_info_number, viewModel.getTotalCount() + ""));
        adapter.addFooterView(footer);
    }

    private void initHeadView() {
        head = View.inflate(getContext(), R.layout.item_check_order_head_layout, null);
        head.setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_BOOLEAN, AddressManageFragment.ADDRESS_ITEM_IS_CLICK)
                    .startParentActivity(getActivity(), AddressManageFragment.class, ADDRESS_CODE);
        });
        adapter.addHeaderView(head);

        addressViewModel.getAddressList(addressEntities -> {
            if (addressViewModel.getDefaultAddress() != null) {
                addressEntity = addressViewModel.getDefaultAddress();
                bindHeadData(addressEntity);
            }
        });
    }

    private void bindHeadData(AddressEntity addressEntity) {
        BaseViewHolder holder = new BaseViewHolder(head);
        holder.setText(R.id.tv_shop_name, UserModel.getInstance().getNickName());
        holder.setText(R.id.tv_consignee_name, getString(R.string.text_receiver_name_, addressEntity.consignee));
        holder.setText(R.id.tv_consignee_tel, getString(R.string.text_phone_, addressEntity.mobile));
        holder.setText(R.id.tv_address, getString(R.string.text_address_detail_, addressEntity.provinceText + addressEntity.cityText +
                addressEntity.districtText + addressEntity.detailAddress));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ADDRESS_CODE == requestCode && data != null) {
            addressEntity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
            if (addressEntity != null) {
                bindHeadData(addressEntity);
            }
        }
    }
}

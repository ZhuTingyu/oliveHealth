package com.olive.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.biz.widget.recyclerview.XRecyclerView;
import com.jungly.gridpasswordview.GridPasswordView;
import com.olive.R;
import com.olive.model.UserModel;
import com.olive.model.entity.AccountEntity;
import com.olive.model.entity.OrderEntity;
import com.olive.ui.adapter.PayOrderAdapter;
import com.olive.ui.main.my.UserViewModel;
import com.olive.ui.order.viewModel.PayOrderViewModel;

/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public class PayFragment extends BaseFragment {

    private XRecyclerView recyclerView;
    private PayOrderAdapter adapter;
    private PayOrderViewModel viewModel;

    protected TextView tvOrderNumber;
    protected TextView tvPayPrice;
    protected TextView tvNeedPayPrice;
    protected TextView tvVacancies;
    protected EditText etVacancies;

    private OrderEntity orderEntity;
    private AccountEntity accountEntity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new PayOrderViewModel(context);
        initViewModel(viewModel);
        orderEntity = getBaseActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
        accountEntity = getBaseActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_VALUE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_with_button_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.title_pay_order));
        initView();
    }

    protected  void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PayOrderAdapter(recyclerView.getRecyclerView());
        recyclerView.setAdapter(adapter);

        viewModel.getBankCards(bankEntities -> {
            adapter.setNewData(bankEntities);
        });

        initHeadView();

        findViewById(R.id.btn_sure).setOnClickListener(v -> {
            createDialog();
        });

    }

    protected void initHeadView() {
        View head = View.inflate(getContext(), R.layout.item_pay_order_head_layout, null);

        tvOrderNumber = findViewById(head,R.id.order_number);
        tvPayPrice = findViewById(head,R.id.order_price);
        tvVacancies = findViewById(head,R.id.account_vacancies);
        tvNeedPayPrice = findViewById(head,R.id.need_pay);
        etVacancies = findViewById(head,R.id.input_account_vacancies);

        tvOrderNumber.setText(getString(R.string.text_order_number, orderEntity.orderNo));
        tvPayPrice.setText(getString(R.string.text_pay_by_account_vacancies, PriceUtil.formatRMB(orderEntity.amount)));
        tvVacancies.setText(PriceUtil.formatRMB(accountEntity.balance));
        tvNeedPayPrice.setText(PriceUtil.formatRMB(orderEntity.amount));


        etVacancies.clearFocus();
        adapter.addHeaderView(head);
    }

    private void createDialog(){
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.dailog_pay_for_order_layout);
        dialog.show();
        initDialog(dialog);
    }

    private void initDialog(BottomSheetDialog dialog) {
        dialog.findViewById(R.id.close).setOnClickListener(v -> {
            dialog.dismiss();
        });


        TextView title = (TextView) dialog.findViewById(R.id.title);

        TextView userNumber = (TextView) dialog.findViewById(R.id.user_number);
        TextView payWay = (TextView) dialog.findViewById(R.id.pay_way);
        TextView payPrice = (TextView) dialog.findViewById(R.id.pay_price);
        TextView btnOk = (TextView) dialog.findViewById(R.id.btn_ok);

        userNumber.setText(getString(R.string.text_account_number, UserModel.getInstance().getUserId()));
        payWay.setText(getString(R.string.text_account_balance));
        payPrice.setText(orderEntity.amount+"");

        btnOk.setOnClickListener(v -> {

            title.setText(getString(R.string.text_input_account_pay_password));
            dialog.findViewById(R.id.rl_info).setVisibility(View.GONE);

            TextView forgetPassword = (TextView) dialog.findViewById(R.id.text_forget_password);
            forgetPassword.setVisibility(View.GONE);

            GridPasswordView passwordView = (GridPasswordView) dialog.findViewById(R.id.password);
            passwordView.setVisibility(View.VISIBLE);

            TextView passwordError = (TextView) dialog.findViewById(R.id.text3);
            btnOk.setText(getString(R.string.text_make_sure_pay));
            btnOk.setOnClickListener(v1 -> {

            });
        });
    }
}

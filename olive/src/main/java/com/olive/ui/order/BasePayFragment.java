package com.olive.ui.order;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.biz.util.RxUtil;
import com.biz.util.ToastUtils;
import com.biz.util.ValidUtil;
import com.biz.widget.recyclerview.XRecyclerView;
import com.jungly.gridpasswordview.GridPasswordView;
import com.olive.R;
import com.olive.event.OrderListUpdateEvent;
import com.olive.event.WeiPayResultEvent;
import com.olive.model.UserModel;
import com.olive.model.entity.AccountEntity;
import com.olive.model.entity.BankEntity;
import com.olive.model.entity.OrderEntity;
import com.olive.ui.BaseErrorFragment;
import com.olive.ui.adapter.PayOrderAdapter;
import com.olive.ui.main.my.UserViewModel;
import com.olive.ui.main.my.account.viewModel.AccountViewModel;
import com.olive.ui.order.viewModel.OrderListViewModel;
import com.olive.ui.order.viewModel.PayOrderViewModel;
import com.olive.util.CashierInputFilter;
import com.olive.util.Utils;
import com.tencent.mm.sdk.modelbase.BaseResp;

import java.math.BigDecimal;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public abstract class BasePayFragment extends BaseErrorFragment {

    private XRecyclerView recyclerView;
    private PayOrderAdapter adapter;
    protected PayOrderViewModel viewModel;

    protected TextView tvOrderNumber;
    protected TextView tvPayPrice;
    protected TextView tvNeedPayPrice;
    protected TextView tvVacancies;
    protected EditText etVacancies;

    protected View head;

    protected TextView btnOk;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        EventBus.getDefault().register(this);
        viewModel = new PayOrderViewModel(context);
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
        setTitle(getString(R.string.title_pay_order));

        getBaseActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (viewModel.accountEntity == null) {
            setProgressVisible(true);
            viewModel.getAccountInfo(accountEntity1 -> {
                setProgressVisible(false);
                viewModel.accountEntity = accountEntity1;
                initView();
            });
        }else {
            initView();
        }
    }

    protected void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PayOrderAdapter(getContext());
        adapter.setViewModel(viewModel);
        recyclerView.setAdapter(adapter);

        viewModel.getBankCards(bankEntities -> {
            bankEntities.add(new BankEntity());
            bankEntities.add(new BankEntity());
            adapter.setNewData(bankEntities);
        });


        btnOk = findViewById(R.id.btn_sure);

        btnOk.setOnClickListener(v -> {
            createDialog();
        });

        initHeadView();

    }

    protected void initHeadView() {
        head = View.inflate(getContext(), R.layout.item_pay_order_head_layout, null);

        tvOrderNumber = findViewById(head, R.id.order_number);
        tvPayPrice = findViewById(head, R.id.order_price);
        tvVacancies = findViewById(head, R.id.account_vacancies);
        tvNeedPayPrice = findViewById(head, R.id.need_pay);
        etVacancies = findViewById(head, R.id.input_account_vacancies);
        InputFilter[] filters = {new CashierInputFilter()};
        etVacancies.setFilters(filters);

        etVacancies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double price = 0;
                if (!s.toString().isEmpty()) {
                    price = Double.valueOf(s.toString()) * 100d;
                }
                if (price > (int) viewModel.orderEntity.amount) {
                    price = viewModel.orderEntity.amount;
                    etVacancies.setText(String.valueOf(price / 100d));
                }

                viewModel.setBalancePayAmount((int) price);
                tvNeedPayPrice.setText(PriceUtil.formatRMB(viewModel.orderEntity.amount - price));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etVacancies.clearFocus();
        adapter.addHeaderView(head);
    }

    private void createDialog() {
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

        userNumber.setText(getString(R.string.text_account_number, Utils.setPhoneString(UserModel.getInstance().getMobile())));
        payWay.setText(viewModel.isBalanceEnough() ? getString(R.string.text_pay_by_account_vacancies_1) : adapter.payWay);
        payPrice.setText(PriceUtil.formatRMB(viewModel.orderEntity.amount));

        btnOk.setOnClickListener(v -> {

            title.setText(getString(R.string.text_input_account_pay_password));
            dialog.findViewById(R.id.rl_info).setVisibility(View.GONE);

            TextView forgetPassword = (TextView) dialog.findViewById(R.id.text_forget_password);
            forgetPassword.setVisibility(View.VISIBLE);

            EditText passwordView = (EditText) dialog.findViewById(R.id.password);
            passwordView.setVisibility(View.VISIBLE);

            TextView passwordError = (TextView) dialog.findViewById(R.id.text3);
            btnOk.setText(getString(R.string.text_make_sure_pay));
            btnOk.setOnClickListener(v1 -> {
                setProgressVisible(true);
                checkPayPassword(dialog, passwordView, passwordError);
            });
        });
    }

    private void checkPayPassword(Dialog dialog, EditText passwordView, TextView passwordError) {
        viewModel.setPayPassword(passwordView.getText().toString());
        viewModel.checkPayPassword(s -> {
            setProgressVisible(false);
            if (s == PayOrderViewModel.PAY_PASSWORD_CORRECT) {
                payOrder();
                dialog.dismiss();
            } else {
                passwordError.setVisibility(View.VISIBLE);
                passwordView.setText("");
            }
        });
    }

    private void payOrder() {
        if (viewModel.isPayHasBalance() && viewModel.isBalanceEnough()) {
            submitOrder();
        } else {
            if (viewModel.payType == PayOrderViewModel.PAY_TYPE_BALANCE) {
                error(getString(R.string.message_pay_price_not_enough));
            } else {
                if (viewModel.isPayWithAli()) {
                    viewModel.getAliPayOrderInfoAndPay(s -> {
                        if (getString(R.string.message_pay_success).equals(s)) {
                            submitOrder();
                        } else {
                            error(s);
                        }
                    });
                } else if (viewModel.isPayWithWei()) {
                    viewModel.getWeiXinOrderInfoAndPay();
                } else {
                    // TODO: 2017/8/18 银行支付
                }
            }
        }
    }

    private void submitOrder() {
        viewModel.payOrder(s -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_BOOLEAN, true)
                    .putExtra(IntentBuilder.KEY_DATA, viewModel.orderEntity)
                    .startParentActivity(getActivity(), PayResultFragment.class);
            EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_WAIT_RECEIVE));
            EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_ALL));
            EventBus.getDefault().post(new OrderListUpdateEvent(OrderListViewModel.TYPE_WAIT_PAY));
            getActivity().finish();
        }, throwable -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_BOOLEAN, false)
                    .putExtra(IntentBuilder.KEY_DATA, viewModel.orderEntity)
                    .startParentActivity(getActivity(), PayResultFragment.class);
            getActivity().finish();
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(WeiPayResultEvent event){
        if(event.req.errCode == WeiPayResultEvent.SUCCESS){
            submitOrder();
        }else if(event.req.errCode == WeiPayResultEvent.CANCEL){
            error(getString(R.string.title_pay_cancel));
        }else if(event.req.errCode == WeiPayResultEvent.ERROR){
            error(getString(R.string.title_pay_failed));
        }
    }
}

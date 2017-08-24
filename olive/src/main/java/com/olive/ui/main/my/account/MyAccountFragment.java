package com.olive.ui.main.my.account;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.PriceUtil;
import com.olive.R;
import com.olive.ui.main.my.account.viewModel.AccountViewModel;
import com.olive.ui.order.PayDebtFragment;

/**
 * Created by TingYu Zhu on 2017/7/30.
 */

public class MyAccountFragment extends BaseFragment {

    private AccountViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new AccountViewModel(context);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_accout_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.title_my_account));
        initView();
    }

    private void initView() {
        TextView balance = findViewById(R.id.balance);
        TextView consume = findViewById(R.id.consume);
        TextView debt = findViewById(R.id.debt);
        TextView btnOk = findViewById(R.id.btn_ok);

        viewModel.getAccountInfo(accountEntity -> {
            balance.setText(PriceUtil.formatRMB(accountEntity.balance));
            consume.setText(PriceUtil.formatRMB(accountEntity.consumeAmount));
            debt.setText(PriceUtil.formatRMB(accountEntity.debt));
        });

        findViewById(R.id.rl_consume).setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_TYPE, ConsumeDetailFragment.TYPE_CONSUME)
                    .startParentActivity(getActivity(), ConsumeDetailFragment.class, true);
        });

        findViewById(R.id.rl_debt).setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_TYPE, ConsumeDetailFragment.TYPE_REFUND)
                    .startParentActivity(getActivity(), ConsumeDetailFragment.class, true);
        });

        btnOk.setOnClickListener(v -> {
            viewModel.getDebtDetails(orderEntities -> {
                if(orderEntities == null || orderEntities.isEmpty()){
                    error(getString(R.string.message_account_no_debt));
                }else {
                    IntentBuilder.Builder()
                            .putExtra(IntentBuilder.KEY_DATA, orderEntities.get(0))
                            .startParentActivity(getActivity(), PayDebtFragment.class, true);
                }
            });
        });
    }
}

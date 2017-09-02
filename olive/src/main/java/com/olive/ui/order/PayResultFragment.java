package com.olive.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.PriceUtil;
import com.olive.R;
import com.olive.model.entity.OrderEntity;
import com.olive.ui.BaseErrorFragment;
import com.olive.ui.main.MainActivity;
import com.olive.ui.main.my.address.AddressViewModel;

import org.w3c.dom.Text;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class PayResultFragment extends BaseErrorFragment {

    private AppCompatImageView imageView;
    private TextView result;
    private TextView address;
    private TextView price;
    private RelativeLayout rlInof;
    private TextView btnOk;
    private TextView urge;

    private boolean isSuccess;
    private OrderEntity orderEntity;

    private AddressViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new AddressViewModel(context);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pay_result_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isSuccess = getBaseActivity().getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN,false);
        orderEntity = getBaseActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);

        initView();
    }

    private void initView() {
        imageView = findViewById(R.id.icon_img);
        result = findViewById(R.id.result);
        urge = findViewById(R.id.urge);
        rlInof = findViewById(R.id.rl_info);
        address = findViewById(R.id.address);
        price = findViewById(R.id.price);
        btnOk = findViewById(R.id.btn_ok);

        mToolbar.setNavigationOnClickListener(null);
        mToolbar.setNavigationIcon(null);

        mToolbar.getMenu().clear();
        mToolbar.getMenu().add(null).setIcon(R.drawable.vector_home_white)
                .setOnMenuItemClickListener(item -> {
                    MainActivity.startMainWithAnim(getActivity(), MainActivity.TAB_HOME);
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        setView();

    }

    private void setView() {
        if(isSuccess){
            mToolbar.setTitle(R.string.message_pay_success);
            result.setText(getString(R.string.text_already_pay));
            viewModel.getAddressList(addressEntities -> {

                address.setText(getString(R.string.text_my_address_is, viewModel.getDefaultAddress().detailAddress));
            });
            price.setText(getString(R.string.text_pay_order_amount, PriceUtil.format(orderEntity.amount)));
            btnOk.setText(getString(R.string.text_back_cart));
            btnOk.setOnClickListener(v -> {
                MainActivity.startMainWithAnim(getActivity(), MainActivity.TAB_CART);
            });
        }else {
            mToolbar.setTitle(R.string.title_pay_failed);
            urge.setVisibility(View.VISIBLE);
            rlInof.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.vector_pay_failed);
            result.setText(getString(R.string.text_pay_failed));
            btnOk.setOnClickListener(v -> {
                getActivity().finish();
            });
        }
    }
}

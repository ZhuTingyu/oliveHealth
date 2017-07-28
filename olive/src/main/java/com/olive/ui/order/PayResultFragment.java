package com.olive.ui.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.olive.R;

import org.w3c.dom.Text;

/**
 * Created by TingYu Zhu on 2017/7/28.
 */

public class PayResultFragment extends BaseFragment {

    private AppCompatImageView imageView;
    private TextView result;
    private TextView address;
    private TextView price;
    private RelativeLayout rlInof;
    private TextView btnOk;
    private TextView urge;

    private boolean isSuccess;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pay_result_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isSuccess = getBaseActivity().getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN,true);
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

        setView();

    }

    private void setView() {
        if(isSuccess){
            result.setText(getString(R.string.text_already_pay));
            address.setText("收货地址：四川省武侯区科华路那段");
            price.setText("实付款：￥1232.123");
            btnOk.setOnClickListener(v -> {

            });
        }else {
            urge.setVisibility(View.VISIBLE);
            rlInof.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.vector_pay_failed);
            result.setText(getString(R.string.text_pay_failed));
            btnOk.setText(getString(R.string.text_back_cart));
            btnOk.setOnClickListener(v -> {

            });
        }
    }
}

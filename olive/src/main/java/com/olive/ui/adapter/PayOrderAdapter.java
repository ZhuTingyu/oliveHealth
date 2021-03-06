package com.olive.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.BankEntity;
import com.olive.ui.order.viewModel.PayOrderViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public class PayOrderAdapter extends BaseQuickAdapter<BankEntity, BaseViewHolder> {

    private int fixationPosition = 0;
    private PayOrderViewModel viewModel;
    public String payWay;

    private SparseBooleanArray sparseBooleanArray;



    private List<Integer> payWayIcon = Lists.newArrayList(R.drawable.vector_china_bank
            , R.drawable.vector_icbc
            , R.drawable.vector_bbc);

    private String[] payWayName;



    private String[] fixationPayWayName;
    private List<Integer> fixationPayIcon = Lists.newArrayList(R.drawable.vector_pay_wei, R.drawable.vector_alipay);


    private Map<String, Integer> way;


    public PayOrderAdapter(Context context) {
        super(R.layout.item_pay_order_layout, Lists.newArrayList());
        payWayName = context.getResources().getStringArray(R.array.array_bank_pay);
        fixationPayWayName = context.getResources().getStringArray(R.array.array_fixation_pay);

        way = new HashMap();
        for (int i = 0; i < payWayIcon.size(); i++) {
            way.put(payWayName[i], payWayIcon.get(i));
        }

        sparseBooleanArray = new SparseBooleanArray();

        payWay = context.getString(R.string.text_pay_by_account_vacancies_1);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 2;
    }

    @Override
    protected void convert(BaseViewHolder holder, BankEntity bankEntity) {
        TextView view = holder.findViewById(R.id.bank_name);
        AppCompatImageView icon = holder.findViewById(R.id.icon);
        if (bankEntity.cardNumber == null || bankEntity.cardNumber.isEmpty()) {
            if(fixationPosition == 2){
                fixationPosition = 0;
            }
            view.setText(fixationPayWayName[fixationPosition]);
            icon.setImageResource(fixationPayIcon.get(fixationPosition));
            fixationPosition++;
        } else {
            view.setText(mContext.getString(R.string.text_bank_pay, bankEntity.bankName) + "(" + getCardNumberFour(bankEntity.cardNumber) + ")");
            holder.setImageResource(R.id.icon, way.get(bankEntity.bankName));
        }
        view.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            setSingleSelected(position);
            setPayType(position - 1);
        });

        if (sparseBooleanArray.get(holder.getAdapterPosition())) {
            holder.setViewDrawableRight(view, R.drawable.vector_address_manage_choose);
        }else {
            view.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }

    }

    private void setPayType(int position) {
        BankEntity bankEntity = getItem(position);
        if (bankEntity.cardNumber == null || bankEntity.cardNumber.isEmpty()) {
            if (position == mData.size() - 2) {
                payWay = fixationPayWayName[0];
                viewModel.setPayType(PayOrderViewModel.PAY_TYPE_WEI, bankEntity.id);
            } else {
                payWay = fixationPayWayName[1];
                viewModel.setPayType(PayOrderViewModel.PAY_TYPE_ALI, bankEntity.id);
            }
        } else {
            payWay = bankEntity.bankName;
            viewModel.setPayType(PayOrderViewModel.PAY_TYPE_BANK, bankEntity.id);
        }
    }

    private String getCardNumberFour(String number) {
        return number.substring(number.length() - 5, number.length() - 1);
    }

    public void setSingleSelected(int position) {
        fixationPosition = 0;
        sparseBooleanArray.clear();
        sparseBooleanArray.put(position, true);
        notifyDataSetChanged();
    }

    public void setViewModel(PayOrderViewModel viewModel) {
        this.viewModel = viewModel;
    }

}

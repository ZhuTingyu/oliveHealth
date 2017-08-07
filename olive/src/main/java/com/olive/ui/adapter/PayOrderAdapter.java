package com.olive.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.BankEntity;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public class PayOrderAdapter extends BaseQuickAdapter<BankEntity, BaseViewHolder> {

    private RecyclerView recyclerView;
    private int choosePosition = 1;

    public PayOrderAdapter(RecyclerView recyclerView) {
        super(R.layout.item_pay_order_layout, Lists.newArrayList());
        this.recyclerView = recyclerView;
    }

    @Override
    protected void convert(BaseViewHolder holder, BankEntity bankEntity) {
        holder.setImageResource(R.id.bank_icon, R.drawable.vector_category_all);
        TextView view = holder.findViewById(R.id.bank_name);
        view.setText(bankEntity.bankName+"("+getCardNumberFour(bankEntity.cardNumber)+")");
        view.setOnClickListener(v -> {
            TextView textView = (TextView) getViewByPosition(recyclerView, choosePosition, R.id.bank_name);
            textView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            choosePosition = holder.getAdapterPosition();
            holder.setViewDrawableRight(view, R.drawable.vector_address_manage_choose);
        });
    }

    private String getCardNumberFour(String number){
        return number.substring(number.length()-5, number.length() -1);
    }
}

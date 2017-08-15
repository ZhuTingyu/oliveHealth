package com.olive.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.BankEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public class PayOrderAdapter extends BaseChooseAdapter<BankEntity, BaseViewHolder> {

    private RecyclerView recyclerView;
    private int fixationPosition = 0;

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
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 2;
    }

    @Override
    protected void initBooleanList(List<BankEntity> data) {
        sparseBooleanArray.clear();
        for(int i = 0; i < data.size() + 2; i++){
            sparseBooleanArray.put(i, false);
        }
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
            /*TextView textView = (TextView) getViewByPosition(recyclerView, choosePosition, R.id.bank_name);
            textView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            choosePosition = holder.getAdapterPosition();
            holder.setViewDrawableRight(view, R.drawable.vector_address_manage_choose);*/
            setSingleSelected((holder.getAdapterPosition()));
        });

        if (sparseBooleanArray.get(holder.getAdapterPosition(), false)) {
            holder.setViewDrawableRight(view, R.drawable.vector_address_manage_choose);
        }else {
            view.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }

    }

    private String getCardNumberFour(String number) {
        return number.substring(number.length() - 5, number.length() - 1);
    }

    @Override
    public void setSingleSelected(int position) {
        fixationPosition = 0;
        super.setSingleSelected(position);
    }
}

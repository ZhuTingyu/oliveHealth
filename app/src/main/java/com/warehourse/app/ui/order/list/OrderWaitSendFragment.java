package com.warehourse.app.ui.order.list;

import com.warehourse.app.R;
import com.warehourse.app.model.entity.OrderEntity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Title: OrderDoneFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:15/05/2017  15:31
 *
 * @author johnzheng
 * @version 1.0
 */

public class OrderWaitSendFragment extends BaseOrderListFragment {


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mViewModel  = new OrderListViewModel(getActivity(), OrderEntity.STATUS_WAIT_RECEIPT_GOODS);
        initViewModel(mViewModel);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.tab_wait_receive_order);
    }

}

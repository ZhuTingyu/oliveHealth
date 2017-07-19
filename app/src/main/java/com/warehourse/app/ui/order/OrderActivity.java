package com.warehourse.app.ui.order;

import com.biz.base.FragmentParentActivity;
import com.biz.util.IntentBuilder;
import com.biz.util.Utils;
import com.warehourse.app.model.entity.OrderEntity;
import com.warehourse.app.ui.order.detail.OrderDetailFragment;
import com.warehourse.app.ui.order.list.OrderAllFragment;
import com.warehourse.app.ui.order.list.OrderDoneFragment;
import com.warehourse.app.ui.order.list.OrderWaitPayFragment;
import com.warehourse.app.ui.order.list.OrderWaitReceiveFragment;
import com.warehourse.app.ui.order.list.OrderWaitSendFragment;
import com.warehourse.app.ui.product.ProductDetailFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Title: MessageActivity
 * Description: 如果来自Scheme jump  这个activity 作为Parent Activity。
 * 其他情况FragmentParentActivity 作为Parent Activity
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:27/05/2017  16:22
 *
 * @author johnzheng
 * @version 1.0
 */


public class OrderActivity extends FragmentParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent!=null && intent.getData()!=null){
            String statusString = intent.getData().getQueryParameter("status");
            String id = intent.getData().getQueryParameter("id");
            if (!TextUtils.isEmpty(id)){
                intent.putExtra(KEY_FRAGMENT, OrderDetailFragment.class);
                intent.putExtra(IntentBuilder.KEY_ID, id);
            }else {
                int status = Utils.getInteger(statusString);
                switch (status) {
                    case OrderEntity.STATUS_ALL:
                        intent.putExtra(KEY_FRAGMENT, OrderAllFragment.class);
                        break;
                    case OrderEntity.STATUS_NO_PAY:
                        intent.putExtra(KEY_FRAGMENT, OrderWaitPayFragment.class);
                        break;
                    case OrderEntity.STATUS_WAIT_DELIVER:
                        intent.putExtra(KEY_FRAGMENT, OrderWaitReceiveFragment.class);
                        break;
                    case OrderEntity.STATUS_COMPLETE:
                        intent.putExtra(KEY_FRAGMENT, OrderDoneFragment.class);
                        break;
                    case OrderEntity.STATUS_WAIT_RECEIPT_GOODS:
                        intent.putExtra(KEY_FRAGMENT, OrderWaitSendFragment.class);
                        break;
                    default:
                        intent.putExtra(KEY_FRAGMENT, OrderAllFragment.class);
                        break;
                }
            }
        }else {

            getIntent().putExtra(KEY_FRAGMENT, OrderAllFragment.class);
        }
        super.onCreate(savedInstanceState);
    }
}

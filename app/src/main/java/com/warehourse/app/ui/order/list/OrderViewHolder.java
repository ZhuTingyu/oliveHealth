package com.warehourse.app.ui.order.list;

import com.biz.base.BaseViewHolder;
import com.biz.util.DialogUtil;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.biz.widget.CustomDraweeView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.OrderEntity;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.ui.home.HomeViewHolder;
import com.warehourse.app.util.LoadImageUtil;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


/**
 * Created by johnzheng on 3/15/16.
 */
@SuppressWarnings("deprecation")
public class OrderViewHolder extends BaseViewHolder {

    @Nullable
    public RecyclerView itemList;
    public TextView textPrice;

    public LinearLayout layout, mBtnLayout;

    @Nullable
    public CustomDraweeView icon;

    @Nullable
    public TextView productName;


    public TextView textTopView, textTopRightView, textSale;

    private OrderListener mOrderApplySaleListener;
    private OrderListener mOrderCancelListener;
    private OrderListener mOrderPayListener;

    public void setOrderPayListener(OrderListener orderPayListener) {
        mOrderPayListener = orderPayListener;
    }

    public void setOrderApplySaleListener(OrderListener orderApplySaleListener) {
        mOrderApplySaleListener = orderApplySaleListener;
    }

    public void setOrderCancelListener(OrderListener orderCancelListener) {
        mOrderCancelListener = orderCancelListener;
    }

    public OrderViewHolder(View itemView) {
        super(itemView);
        itemList = getView(R.id.item_list);
        textPrice = getView(R.id.text_price);
        mBtnLayout=getView(R.id.btn_layout);
        layout = getView(R.id.layout);
        icon = getView(R.id.icon);
        productName = getView(R.id.title);
        textTopView = getView(R.id.text);
        textTopRightView = getView(R.id.text1);
        textSale = getView(R.id.text2);
        if (null != itemList) {
            itemList.setLayoutManager(
                    new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            itemList.addItemDecoration(new VerticalDividerItemDecoration.Builder(
                    itemList.getContext())
                    .color(getColors(R.color.color_transparent))
                    .size(Utils.dip2px(10))
                    .build());
        }
    }

    public void setTopViewVisible(boolean visible) {
        View view = (View) textTopView.getParent();
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setBtn1Sale(boolean isRefund,OrderEntity entity) {
        if (isRefund) {
            TextView btn1 = createBtn();
            btn1.setVisibility(View.VISIBLE);
            btn1.setText(R.string.title_apply_sale);
            btn1.setBackgroundResource(R.drawable.btn_gray_selector);
            RxUtil.click(btn1).subscribe(s -> {
                if(mOrderApplySaleListener!=null){
                    mOrderApplySaleListener.call(entity);
                }
            });
        }
    }

    public void setBtn1Cancel(boolean cancelable, OrderEntity orderEntity) {
        if (cancelable) {
            TextView btn1 = createBtn();
            btn1.setVisibility(View.VISIBLE);
            btn1.setText(R.string.text_order_list_cancel);
            btn1.setBackgroundResource(R.drawable.btn_gray_selector);
            RxUtil.click(btn1).subscribe(s -> {
                DialogUtil.createDialogViewWithCancel(itemView.getContext(), R.string.dialog_title_notice,
                        R.string.dialog_msg_cancel_order,
                        (dialog, which) -> {
                            if (mOrderCancelListener != null) {
                                mOrderCancelListener.call(orderEntity);
                            }
                        }, R.string.btn_confirm);
            });
        }
    }

    public void setBtn1Contact(OrderEntity entity) {
        if (entity.contactable) {
            TextView btn1 = createBtn();
            btn1.setVisibility(View.VISIBLE);
            btn1.setText(R.string.text_order_list_tell);
            btn1.setBackgroundResource(R.drawable.btn_gray_selector);
            RxUtil.click(btn1).subscribe(s -> {
                UserModel.getInstance().createContactDialog(btn1.getContext());
            });
        }
    }

    public void setBtn2Contact(boolean isContact) {
        if (isContact) {
            TextView btn2 = createBtn();
            btn2.setVisibility(View.VISIBLE);
            btn2.setText(R.string.text_order_list_tell);
            btn2.setTextColor(getColors(R.color.color_4a4a4a));
            btn2.setBackgroundResource(R.drawable.btn_gray_selector);
            RxUtil.click(btn2).subscribe(s -> {
                UserModel.getInstance().createContactDialog(btn2.getContext());
            });
        }
    }

    public void setBtn2Pay(boolean payable,OrderEntity entity) {
        if (payable) {
            TextView btn2 = createBtn();
            btn2.setVisibility(View.VISIBLE);
            btn2.setText(R.string.text_order_list_pay);
            btn2.setTextColor(getColors(R.color.color_red));
            btn2.setBackgroundResource(R.drawable.btn_red_selector);
            RxUtil.click(btn2).subscribe(s -> {
                if(mOrderPayListener!=null){
                    mOrderPayListener.call(entity);
                }
            });
        }
    }

    public void setOrderId(String orderId) {
        textTopView.setText(textTopView.getResources().getString(R.string.text_order_code) + orderId);
    }

    public void setOrderStatus(String orderStatus) {
        textTopRightView.setText(orderStatus);
    }

    public void setOrderSaleStatus(String s) {
        if (TextUtils.isEmpty(s)) {
            textSale.setVisibility(View.GONE);
        } else {
            textSale.setVisibility(View.VISIBLE);
            textSale.setText(s);
        }
    }

    public void setItemList(List<ProductEntity> list) {
        itemList.setAdapter(new IconAdapter(list));
    }

    public void setIconAndName(String url, String name) {
        LoadImageUtil.Builder().load(url).http().build().
                imageOptions(R.drawable.ic_product_default).displayImage(icon);
        productName.setText(name);
    }

    public void setOrderPrice(String price) {
        textPrice.setText(price);
    }


    private class IconAdapter extends BaseQuickAdapter<ProductEntity, HomeViewHolder>
            implements
            FlexibleDividerDecoration.PaintProvider,
            FlexibleDividerDecoration.SizeProvider {

        public IconAdapter(@Nullable List<ProductEntity> data) {
            super(R.layout.view_icon_layout, data);
        }

        @Override
        protected void convert(HomeViewHolder holder, ProductEntity entity) {
            holder.setIconView(entity.getLogo());
        }

        @Override
        public Paint dividerPaint(int position, RecyclerView parent) {
            Paint paint = new Paint();
            paint.setColor(getColors(R.color.color_transparent));
            paint.setStrokeWidth(Utils.dip2px(10));
            return paint;
        }

        @Override
        public int dividerSize(int position, RecyclerView parent) {
            return Utils.dip2px(10);
        }
    }

    private TextView createBtn(){
        TextView btn1 = new TextView(itemView.getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(Utils.dip2px(80), Utils.dip2px(32));
        btn1.setGravity(Gravity.CENTER);
        btn1.setTextSize(TypedValue.COMPLEX_UNIT_PX,Utils.dip2px(16));
        btn1.setTextColor(getColors(R.color.color_4a4a4a));

        btn1.setLayoutParams(lp);
        mBtnLayout.addView(btn1);
        return btn1;
    }




    public interface OrderListener {
        void call(OrderEntity orderEntity);
    }
}

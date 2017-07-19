package com.warehourse.app.ui.order.detail;


import com.biz.base.BaseFragment;
import com.biz.base.FragmentBackHelper;
import com.biz.share.ShareHelper;
import com.biz.util.DrawableHelper;
import com.biz.util.IntentBuilder;
import com.warehourse.app.R;
import com.warehourse.app.event.ShareEvent;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.entity.ShareEntity;
import com.warehourse.app.ui.main.MainActivity;
import com.warehourse.app.ui.order.OrderActivity;
import com.warehourse.app.ui.order.list.OrderAllFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import rx.Observable;

/**
 * Created by johnzheng on 3/22/16.
 */
public class OrderPayStatusFragment extends BaseFragment implements FragmentBackHelper {

    protected
    TextView title;
    protected
    TextView titleLine2;
    protected
    Button btn2;
    protected
    ImageView icon;
    protected
    ImageView icon1;
    private OrderDoneViewModel viewModel;
    private boolean isOrderDetail = false;
    private OrderShareFragment orderShareFragment;
    private ShareHelper shareHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
        viewModel = new OrderDoneViewModel(this);
        initViewModel(viewModel);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // viewModel.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(ShareEvent event) {
        if (SystemModel.getInstance().getShare()!=null){
            ShareEntity entity = SystemModel.getInstance().getShare();
            shareHelper = ShareHelper.shareDialog(getActivity(),
                    entity.getIcon(), entity.getShareUrl(),
                    entity.getTitle(), entity.getContent());

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_info_layout, container, false);
        view.setBackgroundColor(Color.WHITE);

        return view;
    }


    private void initView() {
        if (viewModel.isMoneyPay()) {
            if (!TextUtils.isEmpty(viewModel.getPayStatusName()))
                setTitle(viewModel.getPayStatusName());
            if (viewModel.isPayOk()) {
                icon.setImageDrawable(DrawableHelper.getDrawable(icon.getContext(), R.drawable.ic_success_64));
            } else {
                icon.setImageDrawable(DrawableHelper.getDrawable(icon.getContext(), R.drawable.ic_failed_64));
            }
            title.setText(viewModel.getPayStatusName());
        }
        if (viewModel.isArrived()) {
            setTitle(R.string.text_pay_way_cash);
            icon.setImageDrawable(DrawableHelper.getDrawable(icon.getContext(), R.drawable.ic_success_64));
            title.setText(R.string.text_pay_arrived);
        }
//        if (viewModel.isMouth()) {
//            setTitle(R.string.text_pay_zsgf);
//            icon.setImageDrawable(DrawableHelper.getDrawable(icon.getContext(), R.drawable.ic_success_64));
//            title.setText(R.string.text_mouth_pay_arrived);
//        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        icon = (ImageView) findViewById(R.id.icon);
        title = (TextView) findViewById(R.id.title);
        titleLine2 = (TextView) findViewById(R.id.title_line_2);
        btn2 = (Button) findViewById(R.id.btn_2);
        icon1 = (ImageView) findViewById(R.id.icon1);
        TextViewCompat.setTextAppearance(title, R.style.style_text_user_1);
        TextViewCompat.setTextAppearance(titleLine2, R.style.style_text_user_2);
        isOrderDetail = getActivity().getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN, false);
        btn2.setVisibility(View.GONE);
        icon1.setImageResource(R.mipmap.ic_share_money_1);
        icon.setImageDrawable(DrawableHelper.getDrawable(icon.getContext(), R.drawable.ic_success_64));
        mToolbar.setNavigationOnClickListener(e -> {
            if (!isOrderDetail) {
                MainActivity.startMain(getActivity(), 0);
                ActivityCompat.finishAffinity(getBaseActivity());
            } else {
                getActivity().setResult(Activity.RESULT_OK);
                finish();
            }
        });
        mToolbar.getMenu().clear();
        mToolbar.getMenu().add(0, 0, 0, R.string.title_order)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 0) {
                finishActivity();
            }
            return false;
        });
        orderShareFragment = new OrderShareFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, orderShareFragment, OrderShareFragment.class.getName())
                .hide(orderShareFragment)
                .commitAllowingStateLoss();
        icon1.setOnClickListener(e -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .show(orderShareFragment)
                    .commitAllowingStateLoss();
        });


        subscription.add(
                Observable.just(0).delay(1500, TimeUnit.MILLISECONDS)
                        .map(s->{return  orderShareFragment.isAdded();})
                .subscribe(s->{
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .show(orderShareFragment)
                            .commitAllowingStateLoss();
                })
        );

        request();
    }

    protected void request() {
        setProgressVisible(true);
        viewModel.request(b -> {
            setProgressVisible(false);
            initView();
        });
    }

    private void finishActivity() {
        IntentBuilder.Builder()
                .overridePendingTransition(R.anim.left_in, R.anim.right_out)
                .finish(getActivity())
                .startParentActivity(getActivity(), OrderAllFragment.class);
    }


    @Override
    public boolean onBackPressed() {
        if (null != getBaseActivity().getSupportFragmentManager()
                .findFragmentByTag(OrderShareFragment.class.getName())) {
            getBaseActivity().getSupportFragmentManager().beginTransaction()
                    .hide(getBaseActivity().getSupportFragmentManager()
                            .findFragmentByTag(OrderShareFragment.class.getName()))
                    .commitAllowingStateLoss();
            return true;
        }
        finishActivity();
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (shareHelper != null)
            shareHelper.onActivityResult(requestCode, resultCode, data);
    }
}

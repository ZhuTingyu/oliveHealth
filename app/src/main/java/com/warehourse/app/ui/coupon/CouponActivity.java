package com.warehourse.app.ui.coupon;


import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.biz.base.BaseActivity;
import com.biz.base.FragmentAdapter;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.RxUtil;
import com.warehourse.app.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;

public class CouponActivity extends BaseActivity {


    TabLayout mTabLayout;
    ViewPager mViewPager;
    List<String> titles;
    FragmentAdapter fragmentAdapter;
    Button btn1;

    private MoneyViewModel viewModel;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setBackgroundDrawableResource(R.color.color_fbfbfb);
        setContentView(R.layout.activity_tab_layout);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        btn1 = (Button) findViewById(R.id.btn_1);
        viewModel = new MoneyViewModel(this);
        initViewModel(viewModel);
        mToolbar.getMenu().add(0, 0, 0, R.string.text_use_info)
                .setShowAsAction(SHOW_AS_ACTION_ALWAYS);
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 0) {
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_BOOLEAN, true)
                        .startParentActivity(getActivity(), PrivateFragment.class);
            }
            return false;
        });
        mToolbar.setTitle(R.string.title_money);
        titles = Lists.newArrayList(getResources().getStringArray(R.array.tab_money));
        bindUi(RxUtil.click(btn1), o -> {
            IntentBuilder
                    .Builder(IntentBuilder.ACTION_VIEW_WEB, Uri.parse("https://www.depotnextdoor.com/share.do"))
                    .startActivity(getActivity());
        });
        initData();
    }

    private void initData() {
        initPager();
    }

    private void initPager() {
        setProgressVisible(true);
        viewModel.request(voucherMainEntity -> {
            setProgressVisible(false);

            List<Fragment> fragments = new ArrayList<>();
            NoUseFragment noUseFragment = new NoUseFragment();
            BaseCouponListFragment.putListData(noUseFragment, voucherMainEntity.getUnused());
            fragments.add(noUseFragment);
            UsedFragment usedFragment = new UsedFragment();
            BaseCouponListFragment.putListData(usedFragment, voucherMainEntity.getUsed());
            fragments.add(usedFragment);
            ExpiredFragment expiredFragment = new ExpiredFragment();
            BaseCouponListFragment.putListData(expiredFragment, voucherMainEntity.getExpired());
            fragments.add(expiredFragment);
            if (voucherMainEntity.getUnused() != null)
                titles.set(0, titles.get(0) + "(" + voucherMainEntity.getUnused().size() + ")");
            if (voucherMainEntity.getUsed() != null)
                titles.set(1, titles.get(1) + "(" + voucherMainEntity.getUsed().size() + ")");
            if (voucherMainEntity.getExpired() != null)
                titles.set(2, titles.get(2) + "(" + voucherMainEntity.getExpired().size() + ")");
            fragmentAdapter =
                    new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
            mViewPager.setAdapter(fragmentAdapter);
            mViewPager.setOffscreenPageLimit(fragments.size());
            mTabLayout.setupWithViewPager(mViewPager);
        });
    }
}

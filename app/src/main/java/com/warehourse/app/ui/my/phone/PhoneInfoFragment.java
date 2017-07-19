package com.warehourse.app.ui.my.phone;

import com.biz.util.DrawableHelper;
import com.biz.util.IntentBuilder;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.ui.my.BaseInfoFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class PhoneInfoFragment extends BaseInfoFragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        icon.setImageDrawable(DrawableHelper.getDrawableWithBounds(getContext(), R.drawable.ic_change_phone));
        title.setText(R.string.text_for_phone_number);
        btn2.setText(R.string.text_change_phone);
        btn2.setOnClickListener(e -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), PhoneChangeFragment.class);
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.text_person_phone);
        titleLine2.setText(UserModel.getInstance().getMobile());
    }
}
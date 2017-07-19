package com.warehourse.app.ui.my.receiver;

import com.biz.base.FragmentParentActivity;
import com.biz.util.DrawableHelper;
import com.biz.util.IntentBuilder;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.ui.my.BaseInfoFragment;
import com.warehourse.app.ui.my.phone.PhoneChangeFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class ReceiverInfoFragment extends BaseInfoFragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        icon.setImageDrawable(DrawableHelper.getDrawableWithBounds(getContext(), R.drawable.ic_change_address));
        title.setText(R.string.text_for_receiver);
        btn2.setText(R.string.text_change_receiver);
        btn2.setOnClickListener(e->{
            IntentBuilder.Builder()
                    .startParentActivity(getActivity(), ReceiverChangeFragment.class, 1);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.text_person_receiver);
        titleLine2.setText(UserModel.getInstance().getUserEntity().deliveryName==null?"":UserModel.getInstance().getUserEntity().deliveryName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

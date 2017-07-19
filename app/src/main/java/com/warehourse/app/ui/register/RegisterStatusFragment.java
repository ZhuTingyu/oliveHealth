package com.warehourse.app.ui.register;


/**
 * Created by johnzheng on 3/18/16.
 */


import com.biz.base.BaseFragment;
import com.biz.base.FragmentBackHelper;
import com.biz.util.DrawableHelper;
import com.biz.util.IdsUtil;
import com.biz.util.IntentBuilder;
import com.warehourse.app.R;
import com.warehourse.app.event.UserEvent;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.ui.main.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import de.greenrobot.event.EventBus;


public class RegisterStatusFragment extends BaseFragment implements FragmentBackHelper {


    TextView titleLine1;
    TextView titleLine2;
    TextView titleLine3;
    TextView titleLine4;
    Button btn1;
    Button btn2;
    private RegisterStatusViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new RegisterStatusViewModel(this);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_status_layout, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        titleLine1 = findViewById(R.id.title_line_1);
        titleLine2 = findViewById(R.id.title_line_2);
        titleLine3 = findViewById(R.id.title_line_3);
        titleLine4 = findViewById(R.id.title_line_4);
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);


        setTitle(R.string.title_result);
        defaultView();
        btn1.setOnClickListener(e -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
            ActivityCompat.finishAffinity(getActivity());
        });
//        btn2.setOnClickListener(e -> {
//            UserModel.getInstance().createContactDialog(e.getContext());
//            //getBaseActivity().createContactDialog();
//        });
    }

    private void defaultView() {

        switch (UserModel.getInstance().getEditAddress()) {
            case 0:
                titleLine2.setText(IdsUtil.toString(UserModel.getInstance()
                        .getUserEntity().detailRejectReasons, "\r\n"));
                setProfile(false);
                titleLine1.setOnClickListener(e -> {
                    goProfile2();
                });
                btn2.setOnClickListener(v -> {
                    goProfile2();
                });
                break;
            case 10:
                titleLine2.setText(R.string.user_status_10);
                setProfile(false);
                titleLine1.setOnClickListener(e -> {
                    goProfile2();
                });
                btn2.setOnClickListener(v -> {
                    goProfile2();
                });
                break;
            case 20:
                titleLine2.setText(R.string.user_status_20);
                setProfile(true);
                break;
            case 25:
                titleLine2.setText(R.string.user_status_25);
                setProfile(true);
                break;
            default:
                titleLine2.setText(R.string.user_status_30);
                setProfile(true);
                break;
        }


        switch (UserModel.getInstance().getEditPhoto()) {
            case 0:
                titleLine4.setText(IdsUtil.toString(UserModel.getInstance()
                        .getUserEntity().qualificationRejectReasons, "\r\n"));
                setIdentity(false);
                titleLine3.setOnClickListener(e -> {
                    if (UserModel.getInstance().getEditAddress() == 0 ||
                            UserModel.getInstance().getEditAddress() == 10) {
                        goProfile2();

                    } else {
                        goProfile3();
                    }
                });
                btn2.setOnClickListener(v -> {
                    if (UserModel.getInstance().getEditAddress() == 0 ||
                            UserModel.getInstance().getEditAddress() == 10) {
                        goProfile2();

                    } else {
                        goProfile3();
                    }
                });
                break;
            case 10:
                titleLine4.setText(R.string.user_status_10);
                setIdentity(false);
                titleLine3.setOnClickListener(e -> {
                    if (UserModel.getInstance().getEditAddress() == 0 ||
                            UserModel.getInstance().getEditAddress() == 10)
                        goProfile2();
                    else goProfile3();
                });
                btn2.setOnClickListener(v -> {
                    if (UserModel.getInstance().getEditAddress() == 0 ||
                            UserModel.getInstance().getEditAddress() == 10) {
                        goProfile2();

                    } else {
                        goProfile3();
                    }
                });
                break;
            case 20:
                titleLine4.setText(R.string.user_status_20);
                setIdentity(true);
                break;
            case 25:
                titleLine4.setText(R.string.user_status_25);
                setIdentity(true);
                break;
            default:
                titleLine4.setText(R.string.user_status_30);
                setIdentity(true);
                break;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void goProfile2() {
        IntentBuilder.Builder().startParentActivity(getActivity(), Register2Fragment.class);

    }

    private void goProfile3() {
        IntentBuilder.Builder().setClass(getActivity(), Register3Activity.class).startActivity();
    }

    public void setProfile(boolean isSuccess) {
        if (isSuccess) {
            titleLine1.setTextColor(getColor(R.color.base_color));
            titleLine2.setTextColor(getColor(R.color.base_color));
            titleLine1.setCompoundDrawablesWithIntrinsicBounds(
                    DrawableHelper.getDrawableWithBounds(titleLine1.getContext(), R.drawable.ic_success_64)
                    , null, null, null);
            titleLine1.setBackgroundResource(R.drawable.shape_success_background);
            titleLine2.setVisibility(View.GONE);
        } else {
            titleLine1.setTextColor(getColor(R.color.color_ee6e6e));
            titleLine2.setTextColor(getColor(R.color.color_ee6e6e));
            titleLine1.setCompoundDrawablesWithIntrinsicBounds(
                    DrawableHelper.getDrawableWithBounds(titleLine1.getContext(), R.drawable.ic_failed_64)
                    , null, null, null);
            titleLine1.setBackgroundResource(R.drawable.shape_failed_background);
            titleLine2.setVisibility(View.VISIBLE);
        }
    }

    public void setIdentity(boolean isSuccess) {
        if (isSuccess) {
            titleLine3.setTextColor(getColor(R.color.base_color));
            titleLine4.setTextColor(getColor(R.color.base_color));
            titleLine3.setCompoundDrawablesWithIntrinsicBounds(
                    DrawableHelper.getDrawableWithBounds(titleLine3.getContext(), R.drawable.ic_success_64)
                    , null, null, null);
            titleLine3.setBackgroundResource(R.drawable.shape_success_background);
            titleLine4.setVisibility(View.GONE);
        } else {
            titleLine3.setTextColor(getColor(R.color.color_ee6e6e));
            titleLine4.setTextColor(getColor(R.color.color_ee6e6e));
            titleLine3.setCompoundDrawablesWithIntrinsicBounds(
                    DrawableHelper.getDrawableWithBounds(titleLine3.getContext(), R.drawable.ic_failed_64)
                    , null, null, null);
            titleLine3.setBackgroundResource(R.drawable.shape_failed_background);
        }
    }


    @Override
    public boolean onBackPressed() {
        if (UserModel.getInstance().isEditDetail()) {
            EventBus.getDefault().post(new UserEvent(UserEvent.TYPE_CHANGE_REGISTER));
        }
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
        ActivityCompat.finishAffinity(getActivity());
        return true;
    }
}


package com.warehourse.app.ui.register;


/**
 * Created by johnzheng on 3/18/16.
 */

import com.biz.base.BaseFragment;
import com.biz.base.FragmentBackHelper;
import com.biz.util.DrawableHelper;
import com.warehourse.app.R;
import com.warehourse.app.ui.main.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class RegisterDoneFragment extends BaseFragment implements FragmentBackHelper {


    TextView title;
    TextView titleLine2;
    Button btn2;
    ImageView icon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_done_layout, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        icon =  findViewById(R.id.icon);
        title = findViewById(R.id.title);
        titleLine2 =  findViewById(R.id.title_line_2);
        btn2 =  findViewById(R.id.btn_2);
        icon.setImageDrawable(DrawableHelper.getDrawable(icon.getContext(), R.drawable.ic_success_72));
        btn2.setOnClickListener(e -> {

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
            ActivityCompat.finishAffinity(getActivity());

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public boolean onBackPressed() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
        ActivityCompat.finishAffinity(getActivity());
        return true;
    }
}


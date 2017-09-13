package com.olive.ui.upgrade;

import com.biz.base.BaseViewModel;
import com.olive.R;
import com.olive.model.VersionModel;
import com.olive.model.entity.VersionEntity;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/9/12.
 */

public class UpgradeViewModel extends BaseViewModel {

    public UpgradeViewModel(Object activity) {
        super(activity);
    }

    public void update(Action1<VersionEntity> action1) {
        submitRequest(VersionModel.versionInfo(), action1, throwable -> {
            if(getActivity() != null){
                getActivity().error(getString(R.string.message_get_last_version_fail));
            }
        });
    }

}

package com.biz.widget;



import com.biz.http.R;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

public class CustomCountDownTimer extends CountDownTimer {

        TextView tt;
        Activity act;
        int resTickId;
        int resFinishId;

        /*
         * @Parameter 
         * resFinishId  Enable textview text;
         * resTickId    disable textview and count the timer text;
         * 
         */
        public CustomCountDownTimer(Activity act, TextView tt, int resFinishId, int resTickId, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.tt=tt;
            this.act = act;
            this.resFinishId = resFinishId;
            this.resTickId = resTickId;
        }

        @Override
        public void onFinish() {
            tt.setEnabled(true);
            tt.setText(resFinishId);
            tt.setTextColor(tt.getResources().getColor(R.color.color_515151));
        }

        
        @Override
        public void onTick(long millisUntilFinished) {
        	tt.setEnabled(false);
            tt.setTextColor(tt.getResources().getColor(R.color.color_929292));
            tt.setText(act.getString(resTickId, millisUntilFinished / 1000 +"") );
        }

    }
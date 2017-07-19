
package com.biz.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.GridView;


public class ExpandGridView extends GridView {

    private int stateToSave;


    public ExpandGridView(Context context) {
        super(context);
        init();
    }

    public ExpandGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public ExpandGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        //setSelector(R.color.color_transparent);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.stateToSave = this.stateToSave;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        try {
            if (!(state instanceof SavedState)) {
                super.onRestoreInstanceState(state);
                return;
            }

            SavedState ss = (SavedState) state;

            super.onRestoreInstanceState(ss.getSuperState());


            this.stateToSave = ss.stateToSave;
        } catch (Exception e) {
        }
    }

    static class SavedState extends BaseSavedState {
        int stateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
        }

        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}

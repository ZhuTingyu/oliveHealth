package com.biz.util;

import com.biz.http.RxNet;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;

public class OperatorEditTextInput implements Observable.OnSubscribe<String[]> {
    public static Observable<String[]> create(Observable<List<EditText>> observable) {
        return Observable.create(new OperatorEditTextInput(observable.toBlocking().single()));
    }

    private List<EditText> editarray;
    private String[] strArray;

    public OperatorEditTextInput(final List<EditText> editarray) {
        this.editarray = editarray;
        strArray = new String[editarray.size()];
        for (int i = 0; i < editarray.size(); i++) {
            if (editarray.get(i) != null) {
                strArray[i] = editarray.get(i).getText().toString();
            }
        }
    }

    @Override
    public void call(final Subscriber<? super String[]> observer) {
        for (int i = 0; i < editarray.size(); i++) {
            EditText input = editarray.get(i);

            final EditSimpleTextWatcher simple = mCachedListeners.getFromViewOrCreate(input, i);
            final TextWatcher watcher = new EditSimpleTextWatcher(i) {
                @Override
                public void afterTextChanged(final Editable editable) {
                    strArray[getIndex()] = editable.toString();
                    observer.onNext(strArray);
                }
            };
            final Subscription subscription = RxNet.unsubscribeInUiThread(new Action0() {
                @Override
                public void call() {
                    simple.removeTextWatcher(watcher);
                }
            });
            simple.addTextWatcher(watcher);
            observer.add(subscription);
        }
    }


    private static class mCachedListeners {
        private static final Map<EditText, EditSimpleTextWatcher> sCachedListeners = new WeakHashMap<EditText, EditSimpleTextWatcher>();

        public static EditSimpleTextWatcher getFromViewOrCreate(final EditText view, int index) {
            final EditSimpleTextWatcher cached = sCachedListeners.get(view);

            if (cached != null) {
                return cached;
            }

            final EditSimpleTextWatcher listener = new EditSimpleTextWatcher(index);

            sCachedListeners.put(view, listener);
            view.addTextChangedListener(listener);

            return listener;
        }
    }


    private static class EditSimpleTextWatcher implements TextWatcher {
        private final List<TextWatcher> listeners = new ArrayList<TextWatcher>();

        public boolean addTextWatcher(final TextWatcher listener) {
            return listeners.add(listener);
        }

        public boolean removeTextWatcher(final TextWatcher listener) {
            return listeners.remove(listener);
        }

        public EditSimpleTextWatcher(int index) {
            this.index = index;
        }

        private int index;

        public int getIndex() {
            return index;
        }

        @Override
        public void beforeTextChanged(final CharSequence sequence, final int start, final int count, final int after) {
            // nothing to do
        }

        @Override
        public void onTextChanged(final CharSequence sequence, final int start, final int before, final int count) {
            // nothing to do
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            for (final TextWatcher listener : listeners) {
                listener.afterTextChanged(editable);
            }
        }
    }
}

package com.biz.util;

import com.biz.http.RxNet;

import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

/**
 * Created by wangwei on 2016/3/19.
 */
public class RxUtil {
    public static Observable<Object> click(View view) {
        return Observable.create(subscriber -> {
            subscriber.add(RxNet.unsubscribeInUiThread(() -> {
                if (view != null) view.setOnClickListener(null);
            }));
            view.setOnClickListener(v -> {
                v.setEnabled(false);
                v.postDelayed(() -> {
                    v.setEnabled(true);
                }, 350);
                subscriber.onNext(new Object());
            });
        });
    }

    public static Observable<Object> clickQuick(View view) {
        return Observable.create(subscriber -> {
            subscriber.add(RxNet.unsubscribeInUiThread(() -> {
                if (view != null) view.setOnClickListener(null);
            }));
            view.setOnClickListener(v -> {
                v.setEnabled(false);
                v.postDelayed(() -> {
                    v.setEnabled(true);
                }, 200);
                subscriber.onNext(new Object());
            });
        });
    }

    public static Observable<Object> longClick(View view) {
        return Observable.create(subscriber -> {
            subscriber.add(RxNet.unsubscribeInUiThread(() -> {
                if (view != null) view.setOnLongClickListener(null);
            }));
            view.setOnLongClickListener(v -> {
                v.setEnabled(false);
                v.postDelayed(() -> {
                    v.setEnabled(true);
                }, 350);
                subscriber.onNext(new Object());
                return true;
            });
        });
    }

    public static Observable<String> textChanges(TextView view) {
        return Observable.create(subscriber -> {
            final TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    subscriber.onNext(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            };

            Subscription subscription = RxNet.unsubscribeInUiThread(() -> {
                if (view != null) view.removeTextChangedListener(watcher);
            });
            subscriber.add(subscription);
            view.addTextChangedListener(watcher);
            subscriber.onNext(view.getText().toString());
        });
    }

//    public static Observable<String> textCount(EditText textView, int minCount, int maxCount, String maxStr) {
//        return textCount(textView, minCount, textView.getResources().getString(R.string.text_product_min_count, "" + minCount)
//                , maxCount, maxStr);
//    }

    private static Observable<String> textCount(EditText textView, int minCount, String txtMin, int maxCount, String txtMax) {
        return Observable.create(subscriber -> {
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(textView.getText().toString())) {
                        int num = Integer.parseInt(textView.getText().toString());
                        if (num < minCount && num > 0) {
                            textView.setText(minCount + "");
                            textView.setSelection(String.valueOf(minCount).toString().length());
                            Snackbar.make(textView, txtMin, Snackbar.LENGTH_LONG).show();
                        }
                        if (num > maxCount) {
                            textView.setText(maxCount + "");
                            textView.setSelection(String.valueOf(maxCount).toString().length());
                            Snackbar.make(textView, txtMax, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            };
            final View.OnFocusChangeListener focusChange = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        try {
                            String str = ((EditText) v).getText().toString();
                            if (!TextUtils.isEmpty(str)) {
                                Integer d = Integer.valueOf(str);
                                if (d == 0) {
                                    ((EditText) v).setText("");
                                }
                            }
                        } catch (Exception e) {
                        }
                    } else {
                        textView.postDelayed(runnable, 200);
                    }
                }
            };
            final TextWatcher watcher = new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    subscriber.onNext(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(s)) {
                        int num = Integer.parseInt(textView.getText().toString());
                        if (num > maxCount) {
                            textView.setText(maxCount + "");
                            textView.setSelection(String.valueOf(maxCount).toString().length());
                            Snackbar.make(textView, txtMax, Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        if (!("" + num).equals(textView.getText().toString()))
                            textView.setText("" + num);
                    }
                }
            };
            Subscription subscription = RxNet.unsubscribeInUiThread(() -> {
                if (textView != null) {
                    textView.removeTextChangedListener(watcher);
                    textView.setOnFocusChangeListener(null);
                }
            });
            subscriber.add(subscription);
            textView.setOnFocusChangeListener(focusChange);
            textView.addTextChangedListener(watcher);
            subscriber.onNext(textView.getText().toString());
        });
    }

    public static Observable<String> textMaxCount(EditText textView, int maxCount, String txt) {
        return Observable.create(subscriber -> {
            final TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    subscriber.onNext(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(s)) {
                        int num = Integer.parseInt(s.toString());
                        if (num > maxCount) {
                            textView.setText(maxCount + "");
                            textView.setSelection(String.valueOf(maxCount).toString().length());
                            Snackbar.make(textView, txt, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            };
            Subscription subscription = RxNet.unsubscribeInUiThread(() -> {
                if (textView != null) textView.removeTextChangedListener(watcher);
            });
            subscriber.add(subscription);
            textView.addTextChangedListener(watcher);
            subscriber.onNext(textView.getText().toString());
        });
    }


    public static Action1<? super Boolean> enabled(final View view) {
        return b -> {
            if (view != null)
                view.setEnabled(b);
        };
    }

    public static Action1<? super Boolean> visibility(final View view) {
        return b -> {
            if (view != null)
                view.setVisibility(b ? View.VISIBLE : View.GONE);
        };
    }

    public static Action1<? super String> text(final TextView view) {
        return s -> {
            if (view != null)
                view.setText(s);
        };
    }

    public static Action1<? super Long> time(final TextView view, String f) {
        return s -> {
            if (view != null)
                view.setText(TimeUtil.format(s, TextUtils.isEmpty(f) ? TimeUtil.FORMAT_YYYYMMDD : f));
        };
    }

    public static Action1<? super String> textE(final EditText view) {
        return s -> {
            if (view != null) {
                view.setText(s);
                if (s.length() > 0)
                    try {
                        view.setSelection(s.toString().length());
                    } catch (Exception e) {
                    }
            }
        };
    }

    public static Action1<? super String> html(final TextView view) {
        return s -> {
            if (view != null)
                view.setText(Html.fromHtml(s));
        };
    }

    public static Observable<Integer> radioGroupCheckedChanges(RadioGroup radioGroup) {
        return Observable.create(subscriber -> {
            RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    subscriber.onNext(checkedId);
                }
            };

            Subscription subscription = Subscriptions.create(new Action0() {
                @Override
                public void call() {
                    radioGroup.setOnCheckedChangeListener(null);
                }
            });
            subscriber.add(subscription);

            radioGroup.setOnCheckedChangeListener(listener);
        });
    }

    public static Action1<? super Integer> radioGroupChecked(final RadioGroup view) {
        return new Action1<Integer>() {
            @Override
            public void call(Integer value) {
                if (value == -1) {
                    view.clearCheck();
                } else {
                    view.check(value);
                }
            }
        };
    }
}

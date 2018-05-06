package com.licrafter.managingstatewithrxjava;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by lijx on 2018/5/6.
 * Gmail: shellljx@gmail.com
 */

public class RxView {

    public static Observable<View> click(View view) {
        PublishSubject<View> subject = PublishSubject.create();
        view.setOnClickListener(subject::onNext);
        return subject.asObservable();
    }

    public static Observable<String> afterTextChanges(EditText editText) {
        PublishSubject<String> subject = PublishSubject.create();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    subject.onNext(s.toString());
                }
            }
        });
        return subject.asObservable();
    }
}

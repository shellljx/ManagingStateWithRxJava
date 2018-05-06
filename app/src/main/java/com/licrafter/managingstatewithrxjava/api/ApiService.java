package com.licrafter.managingstatewithrxjava.api;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Emitter;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lijx on 2018/5/6.
 * Gmail: shellljx@gmail.com
 */

public class ApiService {

    Pattern patPunc = Pattern.compile("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]$");

    public Observable<String> setName(String name) {
        return Observable.create((Action1<Emitter<String>>) emitter -> {
            try {
                Thread.sleep(2000);
                emitter.onNext(name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, Emitter.BackpressureMode.LATEST).subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> checkName(String name) {
        return Observable.create((Action1<Emitter<Boolean>>) stringEmitter -> {
            try {
                Thread.sleep(1300);
                Matcher matcher = patPunc.matcher(name);
                stringEmitter.onNext(!matcher.find());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, Emitter.BackpressureMode.LATEST).subscribeOn(Schedulers.io());
    }
}

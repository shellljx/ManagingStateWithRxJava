package com.licrafter.managingstatewithrxjava.model;

/**
 * Created by lijx on 2018/5/6.
 * Gmail: shellljx@gmail.com
 */

public class CheckNameEvent extends SubmitUiEvent {

    public String name;

    public CheckNameEvent(String name) {
        this.name = name;
    }
}

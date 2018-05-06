package com.licrafter.managingstatewithrxjava.model;

/**
 * Created by lijx on 2018/5/6.
 * Gmail: shellljx@gmail.com
 */

public class SubmitEvent extends SubmitUiEvent{
    public String name;

    public SubmitEvent(String name) {
        this.name = name;
    }
}

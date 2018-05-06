package com.licrafter.managingstatewithrxjava.model;

/**
 * Created by lijx on 2018/5/6.
 * Gmail: shellljx@gmail.com
 */

public class SubmitUiModel {
    public final boolean inProgress;
    public final boolean success;
    public final String errorMessage;


    protected SubmitUiModel(boolean inProgress, boolean success, String errorMessage) {
        this.inProgress = inProgress;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public static SubmitUiModel inProgress() {
        return new SubmitUiModel(true, false, null);
    }

    public static SubmitUiModel success() {
        return new SubmitUiModel(false, true, null);
    }

    public static SubmitUiModel failure(String message) {
        return new SubmitUiModel(false, false, message);
    }
}

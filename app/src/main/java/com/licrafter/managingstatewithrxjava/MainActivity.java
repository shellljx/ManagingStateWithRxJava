package com.licrafter.managingstatewithrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.licrafter.managingstatewithrxjava.api.ApiService;
import com.licrafter.managingstatewithrxjava.model.CheckNameEvent;
import com.licrafter.managingstatewithrxjava.model.SubmitEvent;
import com.licrafter.managingstatewithrxjava.model.SubmitUiEvent;
import com.licrafter.managingstatewithrxjava.model.SubmitUiModel;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    private EditText mInputView;
    private Button mSubmitView;
    private ProgressBar mProgressBarView;

    private ApiService mService = new ApiService();
    private CompositeSubscription mSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInputView = findViewById(R.id.input);
        mSubmitView = findViewById(R.id.submit);
        mProgressBarView = findViewById(R.id.progressBar);

        mSubscription.add(Observable.merge(
                RxView.click(mSubmitView).map(ignored -> new SubmitEvent(mInputView.getText().toString())),
                RxView.afterTextChanges(mInputView).map(CheckNameEvent::new))
                .compose(getModelTransFormer())
                .subscribe(model -> {
                    mSubmitView.setEnabled(!model.inProgress);
                    mProgressBarView.setVisibility(model.inProgress ? View.VISIBLE : View.GONE);
                    if (model.success) {
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                    } else if (!TextUtils.isEmpty(model.errorMessage)) {
                        Toast.makeText(MainActivity.this, model.errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    throw new OnErrorNotImplementedException(throwable);
                }));
    }

    private Observable.Transformer<SubmitUiEvent, SubmitUiModel> getModelTransFormer() {
        Observable.Transformer<SubmitEvent, SubmitUiModel> submit = observable -> observable
                .flatMap(event -> mService.setName(event.name)
                        .map(response -> SubmitUiModel.success())
                        .onErrorReturn(t -> SubmitUiModel.failure(t.getMessage()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .startWith(SubmitUiModel.inProgress()));

        Observable.Transformer<CheckNameEvent, SubmitUiModel> checkName = observable -> observable
                .sample(200, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .switchMap((Func1<CheckNameEvent, Observable<? extends SubmitUiModel>>) checkNameEvent -> {
                    return mService.checkName(checkNameEvent.name)
                            .map(response -> SubmitUiModel.success())
                            .onErrorReturn(t -> SubmitUiModel.failure(t.getMessage()))
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(SubmitUiModel.inProgress());
                });

        return observable -> observable.publish(shared -> Observable.merge(
                shared.ofType(SubmitEvent.class).compose(submit),
                shared.ofType(CheckNameEvent.class).compose(checkName)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription.clear();
    }
}

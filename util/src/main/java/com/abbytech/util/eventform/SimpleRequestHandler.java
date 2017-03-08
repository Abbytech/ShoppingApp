package com.abbytech.util.eventform;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import abbytech.util.R;
import rx.android.schedulers.AndroidSchedulers;

public class SimpleRequestHandler extends SimpleObserver<SimpleResponse> {
    Activity activity;
    private AlertDialog progressDialog;

    public SimpleRequestHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onNext(SimpleResponse response) {
        onSubmissionResponse(response);
    }

    public void doRequest(rx.Observable<SimpleResponse> responseObservable) {
        if (responseObservable == null)
            throw new IllegalStateException("observable is null");
        showProgress();
        responseObservable.observeOn(AndroidSchedulers.from(getContext().getMainLooper())).subscribe(this);
    }

    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = createProgressDialog();
        }
        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public Context getContext() {
        return activity;
    }

    protected AlertDialog createProgressDialog() {
        return new ProgressDialog.Builder(getContext())
                .setMessage(setProgressDialogMessage())
                .create();
    }

    protected String setProgressDialogMessage() {
        return activity.getString(R.string.progress_dialog_message_generic);
    }

    public void onSubmissionResponse(SimpleResponse response) {
        hideProgress();

        if (response.isSuccessful()) {
            onSuccessfulSubmission(response.getMessage());
        } else {
            onFailedSubmission(response.getMessage());
        }
    }

    protected void onFailedSubmission(String cause) {
        Toast.makeText(getContext(), cause, Toast.LENGTH_SHORT).show();
    }

    protected void onSuccessfulSubmission(String response) {
        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
    }
}

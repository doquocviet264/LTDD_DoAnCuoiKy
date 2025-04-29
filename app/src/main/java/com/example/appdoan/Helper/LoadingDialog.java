package com.example.appdoan.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.example.appdoan.R;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
        builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog, null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public void startLoadingDialog() {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public void setDialogDuration(int durationInMillis) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Thread.sleep(durationInMillis);
                activity.runOnUiThread(this::dismissDialog);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}


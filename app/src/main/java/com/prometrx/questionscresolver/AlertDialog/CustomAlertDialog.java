package com.prometrx.questionscresolver.AlertDialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.prometrx.questionscresolver.R;

public class CustomAlertDialog {

    private Activity activity;
    private android.app.AlertDialog alertDialog;

    public CustomAlertDialog(Activity activity) {
        this.activity = activity;
    }

    public void startDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();

        builder.setView(layoutInflater.inflate(R.layout.custom_alert , null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

    }

    public void dismissDialog() {

        alertDialog.dismiss();

    }


}

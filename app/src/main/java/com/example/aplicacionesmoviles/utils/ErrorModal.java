package com.example.aplicacionesmoviles.utils;


import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class ErrorModal {

    public static void createErrorDialog(Context context, String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void createErrorLocationDialog(Context context, String msg, Activity activity){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    dialog.cancel();
                    activity.onBackPressed();
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}

package com.example.aplicacionesmoviles.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;


import com.example.aplicacionesmoviles.R;

public class LoadingBar {
    private Context context;
    private Dialog dialog;


    public LoadingBar(Context context) {
        this.context = context;
    }

    public void startDialog(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.loading_bar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.create();
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }
}

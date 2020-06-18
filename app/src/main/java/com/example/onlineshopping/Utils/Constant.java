package com.example.onlineshopping.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.onlineshopping.R;


public class Constant {
    public static AlertDialog alertDialog1;
    public static String BASEURL = "http://baitykw.com/";


    public static void ShowAlert(String errorMsg,Context context) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog, null, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.ErrorDialog);
        builder.setView(view);
        alertDialog1 = builder.create();
        alertDialog1.show();
        alertDialog1.setCancelable(false);
        AppCompatTextView error = view.findViewById(R.id.error_message);
        error.setText(errorMsg);
        Button ok;
        ok = view.findViewById(R.id.btn_error_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
    }
}

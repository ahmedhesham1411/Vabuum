package com.example.onlineshopping.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.annotation.RequiresApi;

@SuppressLint("AppCompatCustomView")
public class MyButtonBold extends Button {
    public MyButtonBold(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "jozoor.ttf");
        this.setTypeface(face,Typeface.BOLD);
    }

    public MyButtonBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "jozoor.ttf");
        this.setTypeface(face,Typeface.BOLD);
    }

    public MyButtonBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "jozoor.ttf");
        this.setTypeface(face,Typeface.BOLD);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyButtonBold(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "jozoor.ttf");
        this.setTypeface(face,Typeface.BOLD);
    }
}

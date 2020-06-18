package com.example.onlineshopping.Utils;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyInstance extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        sendtoken(FirebaseInstanceId.getInstance().getToken());
    }

    private void sendtoken(String token) {
        Log.d("TOKEN: ", String.valueOf(token));
    }
}

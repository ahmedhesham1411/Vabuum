package com.example.onlineshopping.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.onlineshopping.R;
import com.example.onlineshopping.Utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class Login extends AppCompatActivity implements View.OnClickListener {
    AlertDialog alertDialog;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    AppCompatTextView sign_up;
    Button sign_in;
    AppCompatEditText txt_emailaa,txt_passwordaa;
    Context context;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            Intent intent = new Intent(Login.this,Home.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        init();

        FirebaseMessaging.getInstance().subscribeToTopic("all");

/*        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( Login.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);
                Toast.makeText(context, newToken, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    void init(){
        auth = FirebaseAuth.getInstance();
        sign_up = findViewById(R.id.txt_signup1);
        sign_in = findViewById(R.id.sign_inaa);
        txt_emailaa = findViewById(R.id.txt_emailaa);
        txt_passwordaa = findViewById(R.id.txt_passwordaa);
        sign_up.setOnClickListener(this);
        sign_in.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_signup1:
                Intent intent = new Intent(Login.this, Sign_up.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
            case R.id.sign_inaa:

                String txt_mail = txt_emailaa.getText().toString();
                String txt_password = txt_passwordaa.getText().toString();
                if (TextUtils.isEmpty(txt_mail) || TextUtils.isEmpty(txt_password)) {
                    Constant.ShowAlert("All Fields are Required",context);
                    //Toast.makeText(Login.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if (!isNetworkAvailable()){
                    Constant.ShowAlert("Check Internet Connection",context);
                }
                else {
                    showLoadingDialog();
                    auth.signInWithEmailAndPassword(txt_mail, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                alertDialog.dismiss();
                                Intent intent = new Intent(Login.this, Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                alertDialog.dismiss();
                                Constant.ShowAlert("Incorrect Email or password",context);
                            }
                        }
                    });
                }
                break;

        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showLoadingDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.loading, null, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.SheetDialog);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Sign_up extends AppCompatActivity implements View.OnClickListener {
    AlertDialog alertDialog;
    Context context;
    AppCompatTextView sign_in;
    FirebaseAuth auth;
    DatabaseReference reference;
    Button register;
    AppCompatEditText txt_name,txt_email,txt_password,txt_confirm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    void init(){
        context = this;
        sign_in = findViewById(R.id.txt_signin1);
        register = findViewById(R.id.register);
        txt_name = findViewById(R.id.txt_name);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        txt_confirm_password = findViewById(R.id.txt_confirm_password);
        register.setOnClickListener(this);
        sign_in.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_signin1:
                Intent intent = new Intent(Sign_up.this, Login.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.register:
                String username = txt_name.getText().toString();
                String email = txt_email.getText().toString();
                String password = txt_password.getText().toString();
                String confirm_password = txt_confirm_password.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm_password)){
                    Constant.ShowAlert("All fields are required",this);
                }
                else if (txt_password.length() <6){
                    Constant.ShowAlert("password must be at least 6 characters",this);
                }
                else if (!password.equals(confirm_password) || !confirm_password.equals(password)){
                    Constant.ShowAlert("password doesn't match",this);
                }
                else if (!isNetworkAvailable()){
                    Constant.ShowAlert("Check Internet Connection",this);
                }
                else {
                    register(username,email,password);
                    showLoadingDialog();
                }
        }
    }

    private void register (final String username, final String email, final String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userId = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("users").child(userId);

                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("id",userId);
                    hashMap.put("username",username);
                    hashMap.put("Password",password);
                    hashMap.put("email",email);
                    hashMap.put("imageURL","defult");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                alertDialog.dismiss();
                                Intent intent = new Intent(Sign_up.this, Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });
                }
                else {
                    alertDialog.dismiss();
                    Constant.ShowAlert("can't register with this email or password",context);
                }
            }
        });
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

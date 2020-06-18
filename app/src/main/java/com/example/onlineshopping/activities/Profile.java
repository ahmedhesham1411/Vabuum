package com.example.onlineshopping.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.onlineshopping.Model.User;
import com.example.onlineshopping.R;
import com.example.onlineshopping.Utils.Constant;
import com.example.onlineshopping.Utils.GradientTextView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    AlertDialog alertDialog,alertDialog1;
    DatabaseReference reference;
    FirebaseUser fuser;
    CircleImageView image_profile;
    GradientTextView username;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    AppCompatImageView go_back;
    StorageReference storageReference;
    FrameLayout upload;
    RelativeLayout fullimageee;
    User user;
    AppCompatImageView fullImage,closeImage;
    String imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context = this;
        init();
        getDataFromFirebase();

    }

    private void init(){
        image_profile = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        go_back = findViewById(R.id.go_back);
        upload = findViewById(R.id.upload);
        fullImage = findViewById(R.id.fullimg);
        closeImage = findViewById(R.id.close_image);
        fullimageee = findViewById(R.id.fullimageee);
        fullImage.setOnClickListener(this);
        closeImage.setOnClickListener(this);
        image_profile.setOnClickListener(this);
        upload.setOnClickListener(this);
        go_back.setOnClickListener(this);
    }

    private void getDataFromFirebase(){
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
        showLoadingDialog();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("defult")){
                    alertDialog.dismiss();
                    image_profile.setImageResource(R.drawable.profile);
                } else {
                    alertDialog.dismiss();
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(image_profile);
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(fullImage);
                    imageurl = user.getImageURL();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        showLoadingDialog();
        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }
                    alertDialog.dismiss();
                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", ""+mUri);
                        reference.updateChildren(map);
                        alertDialog.dismiss();
                    } else {
                        Constant.ShowAlert("Failed",getApplicationContext());
                        alertDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Constant.ShowAlert(e.getMessage(),getApplicationContext());
                    alertDialog.dismiss();
                }
            });
        } else {
            alertDialog.dismiss();
            Constant.ShowAlert("No image selected",getApplicationContext());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getApplicationContext(), "Upload in preogress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_back:
                finish();

                break;
            case R.id.upload:
                openImage();

                break;
            case R.id.profile_image:
                showFullImage();
                Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.pop_up);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
                myAnim.setInterpolator(interpolator);
                fullimageee.startAnimation(myAnim);

                break;
            case R.id.close_image:
                fullimageee.setVisibility(View.GONE);

        }
    }

    private void showFullImage() {
        fullimageee.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {
        if (fullimageee.getVisibility() == View.VISIBLE){
            fullimageee.setVisibility(View.GONE);
        }
        else {
            super.onBackPressed();
        }
    }

    class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }

}

package com.example.onlineshopping.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.onlineshopping.Model.Item;
import com.example.onlineshopping.R;
import com.example.onlineshopping.Utils.MyButtonBold;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;

public class ItemDetails extends AppCompatActivity implements View.OnClickListener {

    TextView cat,txtname,txtprice,txtdesc;
    String cat_name,item_id,user_id,itemid,name,price,desc,imageURL,userid;
    FirebaseUser fuser,firebaseUser;
    DatabaseReference reference;
    AppCompatImageView imggg;
    Context context;
    AlertDialog alertDialog;
    Button add_to_cart,add_to_favourite;
    AppCompatImageView increase,decrease,go_back;
    AppCompatTextView counter;
    int count;
    String cattt;
    MyButtonBold done,done1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Intent intent = getIntent();
        cat_name = intent.getStringExtra("cat");
        item_id = intent.getStringExtra("item_id");

        init();
        getData();
    }

    void init(){
        context = this;
        cat = findViewById(R.id.cat);
        txtname = findViewById(R.id.txtname);
        txtprice = findViewById(R.id.txtprice);
        txtdesc = findViewById(R.id.txtdesc);
        cat.setText(cat_name);
        imggg = findViewById(R.id.imggg);
        add_to_cart = findViewById(R.id.add_to_cart);
        increase = findViewById(R.id.increase);
        decrease = findViewById(R.id.decrease);
        counter = findViewById(R.id.counter);
        add_to_favourite = findViewById(R.id.add_to_fav);
        add_to_favourite.setOnClickListener(this);
        increase.setOnClickListener(this);
        decrease.setOnClickListener(this);
        add_to_cart.setOnClickListener(this);
        count = 1;
        go_back = findViewById(R.id.go_back);
        go_back.setOnClickListener(this);

    }

    void getData(){
        showLoadingDialog();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        user_id = fuser.getUid();

        reference = FirebaseDatabase.getInstance().getReference(cat_name).child(item_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Item items = dataSnapshot.getValue(Item.class);
                    txtname.setText(items.getItem_name());
                    txtprice.setText(items.getItem_price() + " L.E");
                    txtdesc.setText(items.getItem_desc());
                    Glide.with(getApplicationContext()).load(items.getImageURL()).into(imggg);
                    alertDialog.dismiss();

                itemid = items.getItem_id();
                name = items.getItem_name();
                price = items.getItem_price();
                desc = items.getItem_desc();
                imageURL = items.getImageURL();
                cattt = items.getCat();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void addToCart(){

        String ccc = String.valueOf(count);
        showLoadingDialog();
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("My Cart").child(user_id);
        String item_id_cart = reference1.push().getKey();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userid",userid);
        hashMap.put("item_name",name);
        hashMap.put("item_price",price);
        hashMap.put("item_desc",desc);
        hashMap.put("imageURL", imageURL);
        hashMap.put("item_id", itemid);
        hashMap.put("count", ccc);
        hashMap.put("cat", cattt);
        hashMap.put("item_id_cart", item_id_cart);


        reference1.child(item_id_cart).setValue(hashMap);
        alertDialog.dismiss();
        cartDialog();

    }

    void addToFav(){

        showLoadingDialog();
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("My Favourite").child(user_id);
        String item_id_cart = reference1.push().getKey();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userid",userid);
        hashMap.put("item_name",name);
        hashMap.put("item_price",price);
        hashMap.put("item_desc",desc);
        hashMap.put("imageURL", imageURL);
        hashMap.put("item_id", itemid);
        hashMap.put("cat", cattt);
        hashMap.put("item_id_cart", item_id_cart);

        reference1.child(item_id_cart).setValue(hashMap);
        alertDialog.dismiss();
        favDialog();

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_to_cart:
                addToCart();

                break;
            case R.id.increase:
                count = count+1;
                String currentCount = String.valueOf(count);
                counter.setText(currentCount);

                break;
            case R.id.decrease:
                if (count == 1){}
                else {
                    count = count-1;
                    String currentCount1 = String.valueOf(count);
                    counter.setText(currentCount1);
                }

                break;
            case R.id.add_to_fav:
                addToFav();
                break;

            case R.id.go_back:
                finish();
        }
    }

    private void favDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_success_favourite, null, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.ErrorDialog);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();;
        done = view.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void cartDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_success_cart, null, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.ErrorDialog);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        done = view.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}

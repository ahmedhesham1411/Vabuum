package com.example.onlineshopping.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.onlineshopping.Adapter.SubCatAdapter;
import com.example.onlineshopping.Model.Item;
import com.example.onlineshopping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubCat extends AppCompatActivity implements SubCatAdapter.OnClickHander, View.OnClickListener {

    TextView title;
    String titlee;
    FirebaseUser fuser;
    DatabaseReference reference;
    List<Item> items;
    Context context;
    SubCatAdapter subCatAdapter;
    RecyclerView items_recycler;
    AlertDialog alertDialog;
    Button low_to_high,high_to_low,best_match;
    AppCompatImageView go_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat);

        Intent intent = getIntent();
        titlee = intent.getStringExtra("cat");
        init();
        getData();
    }

    void init(){
        context = this;
        title = findViewById(R.id.cat);
        items_recycler = findViewById(R.id.items_recycler);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());

        title.setText(titlee);
        low_to_high = findViewById(R.id.low_to_high);
        high_to_low = findViewById(R.id.high_to_low);
        best_match = findViewById(R.id.best_match);
        go_back = findViewById(R.id.go_back);
        go_back.setOnClickListener(this);
        low_to_high.setOnClickListener(this);
        high_to_low.setOnClickListener(this);
        best_match.setOnClickListener(this);
    }

    private void getData(){
        showLoadingDialog();
        items = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference(titlee);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                items.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()){
                    Item item = Snapshot.getValue(Item.class);
                    items.add(item);
                    items_recycler.setLayoutManager(new LinearLayoutManager(context));
                    items_recycler.setHasFixedSize(true);

                    subCatAdapter = new SubCatAdapter(items,getApplicationContext(), SubCat.this);
                    items_recycler.setAdapter(subCatAdapter);

                }
                alertDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(int position) {
        String catt = titlee;
        String item_id = items.get(position).getItem_id();
        Intent intent = new Intent(SubCat.this, ItemDetails.class);
        intent.putExtra("cat",catt);
        intent.putExtra("item_id",item_id);
        startActivity(intent);
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
            case R.id.low_to_high:
                showLoadingDialog();
                Collections.sort(items, (lhs, rhs) -> lhs.getItem_price().compareTo(rhs.getItem_price()));
                subCatAdapter = new SubCatAdapter(items,getApplicationContext(), SubCat.this);
                items_recycler.setAdapter(subCatAdapter);
                alertDialog.dismiss();

                break;
            case R.id.high_to_low:
                showLoadingDialog();
                Collections.sort(items, (lhs, rhs) -> lhs.getItem_price().compareTo(rhs.getItem_price()));
                Collections.reverse(items);
                subCatAdapter = new SubCatAdapter(items,getApplicationContext(), SubCat.this);
                items_recycler.setAdapter(subCatAdapter);
                alertDialog.dismiss();

                break;
            case R.id.best_match:
                getData();
                break;
            case R.id.go_back:
                finish();
                break;
        }
    }
}

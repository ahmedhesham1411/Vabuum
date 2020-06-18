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

import com.example.onlineshopping.Adapter.CartAdapter;
import com.example.onlineshopping.Adapter.FavAdapter;
import com.example.onlineshopping.Adapter.SubCatAdapter;
import com.example.onlineshopping.Model.Item;
import com.example.onlineshopping.Model.ItemCart;
import com.example.onlineshopping.Model.ItemFav;
import com.example.onlineshopping.R;
import com.example.onlineshopping.Utils.MyButtonBold;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Favourite extends AppCompatActivity implements SubCatAdapter.OnClickHander, FavAdapter.OnClickHander {

    FirebaseUser fuser;
    DatabaseReference reference;
    AlertDialog alertDialog;
    Context context;
    String user_id;
    List<ItemFav> listitems;
    FavAdapter favAdapter;
    RecyclerView items_recycler;
    MyButtonBold yes,no;
    AppCompatImageView go_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        context = this;
        items_recycler = findViewById(R.id.myfav);
        go_back = findViewById(R.id.go_back);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();

    }

    private void getData() {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        user_id = fuser.getUid();
        showLoadingDialog();
        listitems = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("My Favourite").child(user_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listitems.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()){
                    ItemFav item = Snapshot.getValue(ItemFav.class);
                    listitems.add(item);
                    items_recycler.setLayoutManager(new LinearLayoutManager(context));
                    items_recycler.setHasFixedSize(true);

                    favAdapter = new FavAdapter(listitems,getApplicationContext(), Favourite.this);
                    items_recycler.setAdapter(favAdapter);

                }
                alertDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
    public void onClick(int position) {
        String catt = listitems.get(position).getCat();
        String item_id = listitems.get(position).getItem_id();
        Intent intent = new Intent(Favourite.this, ItemDetails.class);
        intent.putExtra("cat",catt);
        intent.putExtra("item_id",item_id);
        startActivity(intent);

    }

    @Override
    public void onRemoveClick(int position) {
        remove_Dialog(position);
    }

    void remove_Dialog(int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_confirm, null, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.ErrorDialog);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        yes = view.findViewById(R.id.btnYes);
        no = view.findViewById(R.id.btnNo);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                reference.child(listitems.get(position).getItem_id_cart()).setValue(null);
                reload();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}

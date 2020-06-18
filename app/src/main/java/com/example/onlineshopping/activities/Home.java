package com.example.onlineshopping.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlineshopping.Adapter.Home_adapter;
import com.example.onlineshopping.Adapter.Home_slider_adapter;
import com.example.onlineshopping.Model.Home_model;
import com.example.onlineshopping.Model.Slider_model;
import com.example.onlineshopping.R;
import com.example.onlineshopping.Utils.MyButtonBold;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements Home_adapter.OnClickHander,NavigationView.OnNavigationItemSelectedListener {
    RecyclerView home_recycler;
    Home_adapter home_adapter;
    ArrayList<Home_model> home_models;
    ArrayList<Slider_model> slider_models;
    DrawerLayout drawer;
    Context context;
    MyButtonBold btnYes,btnNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;
        createData();
        init();
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Log.i(TAG, "onDrawerSlide");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                //Log.i(TAG, "onDrawerStateChanged");
            }
        });
    }

    void createData(){
        home_models = new ArrayList<>();
        home_models.add(new Home_model("men_fashion","Men Fashion",R.drawable.men_fashion));
        home_models.add(new Home_model("women_fashion","Women Fashion",R.drawable.women_fashion));
        home_models.add(new Home_model("watches","Watches",R.drawable.watches));
        home_models.add(new Home_model("mobiles","Mobiles",R.drawable.mobile));
        home_models.add(new Home_model("tv","TV",R.drawable.tv));
        home_models.add(new Home_model("laptop","Laptop",R.drawable.eee));

        slider_models = new ArrayList<>();
        slider_models.add(new Slider_model(1,R.drawable.eee));
        slider_models.add(new Slider_model(1,R.drawable.mobile));
        slider_models.add(new Slider_model(1,R.drawable.men_fashion));
        slider_models.add(new Slider_model(1,R.drawable.women_fashion));
        slider_models.add(new Slider_model(1,R.drawable.watches));
    }

    void init(){
        home_recycler = findViewById(R.id.home_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext() ,LinearLayoutManager.VERTICAL, false);
        home_recycler.setLayoutManager(linearLayoutManager);
        home_recycler.setHasFixedSize(true);


        home_adapter = new Home_adapter(home_models,this,this);
        home_recycler.setAdapter(home_adapter);


        SliderView sliderView = findViewById(R.id.imageSliderHome);
        Home_slider_adapter adapterr = new Home_slider_adapter(this,slider_models);
        sliderView.setSliderAdapter(adapterr);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(Home.this, Profile.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_goto_mycart) {
            Intent intent = new Intent(Home.this, Cart.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_favourite) {
            Intent intent = new Intent(Home.this, Favourite.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_contactus) {
            Intent intent = new Intent(Home.this, Contact_us.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_aboutus) {
            Intent intent = new Intent(Home.this, About_us.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout) {
            showLogOutDialog();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(int position) {
        if (home_models.get(position).getName().equals("men_fashion")){
            Intent intent = new Intent(getApplicationContext(), SubCat.class);
            intent.putExtra("cat","men fashion");
            startActivity(intent);
        }
        else if (home_models.get(position).getName().equals("women_fashion")){
            Intent intent = new Intent(getApplicationContext(), SubCat.class);
            intent.putExtra("cat","women fashion");
            startActivity(intent);
        }
        else if (home_models.get(position).getName().equals("watches")){
            Intent intent = new Intent(getApplicationContext(), SubCat.class);
            intent.putExtra("cat","watches");
            startActivity(intent);
        }
        else if (home_models.get(position).getName().equals("mobiles")){
            Intent intent = new Intent(getApplicationContext(), SubCat.class);
            intent.putExtra("cat","mobiles");
            startActivity(intent);
        }
        else if (home_models.get(position).getName().equals("tv")){
            Intent intent = new Intent(getApplicationContext(), SubCat.class);
            intent.putExtra("cat","tv");
            startActivity(intent);
        }
        else if (home_models.get(position).getName().equals("laptop")){
            Intent intent = new Intent(getApplicationContext(), SubCat.class);
            intent.putExtra("cat","laptop");
            startActivity(intent);
        }
    }

    private void showLogOutDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.log_out_dialog, viewGroup, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        btnYes = dialogView.findViewById(R.id.btnYes);
        btnNo= dialogView.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Home.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


}

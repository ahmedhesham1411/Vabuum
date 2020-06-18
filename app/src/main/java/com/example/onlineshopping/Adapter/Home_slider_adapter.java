package com.example.onlineshopping.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.onlineshopping.Model.Slider_model;
import com.example.onlineshopping.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class Home_slider_adapter extends SliderViewAdapter<Home_slider_adapter.SliderAdapterVH> {

    Context context;
    ArrayList<Slider_model> imagesList;

    public Home_slider_adapter(Context context, ArrayList<Slider_model> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
    }

    @Override
    public Home_slider_adapter.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, null);
        return new Home_slider_adapter.SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(Home_slider_adapter.SliderAdapterVH viewHolder, int position) {

        viewHolder.imageViewBackground.setImageResource(imagesList.get(position).getImage());
        viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        AppCompatImageView imageViewBackground;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.imageHome);
            this.itemView = itemView;
        }
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
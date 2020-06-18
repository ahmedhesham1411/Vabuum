package com.example.onlineshopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshopping.Model.Item;
import com.example.onlineshopping.Model.ItemCart;
import com.example.onlineshopping.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    List<ItemCart> items;
    Context context;
    private final OnClickHander onClickHander;



    public interface OnClickHander {
        void onClick(int position);
        void onRemoveClick(int position);

    }

    public CartAdapter(List<ItemCart> items, Context context, OnClickHander onClickHander) {
        this.items = items;
        this.context = context;
        this.onClickHander = onClickHander;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cart, null);
        return new CartAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(items.get(position).getImageURL()).into(holder.image);
        holder.name.setText(items.get(position).getItem_name());
        holder.price.setText(items.get(position).getItem_price() + " L.E");
        holder.count.setText(items.get(position).getCount());
        //holder.txt_details.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatImageView image,remove;
        TextView name, price,count;
        Button  txt_details;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageee);
            name = itemView.findViewById(R.id.nameeee);
            price = itemView.findViewById(R.id.priceeee);
            count = itemView.findViewById(R.id.countttt);
            txt_details = itemView.findViewById(R.id.txt_details);
            cardView = itemView.findViewById(R.id.card);
            remove = itemView.findViewById(R.id.remove);
            txt_details.setOnClickListener(this);
            remove.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.txt_details:
                    int position = getAdapterPosition();
                    onClickHander.onClick(position);
                    notifyDataSetChanged();
                    break;

                case R.id.remove:
                    int position1 = getAdapterPosition();
                    onClickHander.onRemoveClick(position1);
                    break;
            }

        }
    }
}
package com.example.onlineshopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshopping.Model.Home_model;
import com.example.onlineshopping.R;

import java.util.List;

public class Home_adapter extends RecyclerView.Adapter<Home_adapter.ViewHolder> {
    Context context;
    List<Home_model> home_models;
    private final OnClickHander onClickHander;

    public interface OnClickHander {
        void onClick(int position);
    }

    public Home_adapter(List<Home_model> home_models, Context context,OnClickHander onClickHander) {
        this.context = context;
        this.home_models = home_models;
        this.onClickHander = onClickHander;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(home_models.get(position).getDiscription());
        holder.imageView.setImageResource(home_models.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return home_models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatTextView textView;
        AppCompatImageView imageView;
        FrameLayout frame_id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            frame_id = itemView.findViewById(R.id.frame_id);
            frame_id.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onClickHander.onClick(position);
            notifyDataSetChanged();
        }
    }
}

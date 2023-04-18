package com.cubes.miletic.events.ui.adapters.detail.items;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.miletic.events.databinding.RvItemDetailNewsItemBinding;
import com.cubes.miletic.events.model.News;
import com.cubes.miletic.events.ui.WebViewActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RvNewsAdapter extends RecyclerView.Adapter<RvNewsAdapter.MyHolder>{

    private ArrayList<News> list;

    public RvNewsAdapter(ArrayList<News> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvItemDetailNewsItemBinding binding = RvItemDetailNewsItemBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.binding.tvDate.setText(((News)list.get(position)).date);
        holder.binding.tvTitle.setText(((News)list.get(position)).title);
        holder.binding.tvText.setText(((News)list.get(position)).text);
        Picasso.get().load(((News)list.get(position)).imageUrl).into(holder.binding.imageView);

        holder.binding.tvReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((News) list.get(position)).address != null){
                    Intent intent = new Intent(holder.binding.getRoot().getContext(), WebViewActivity.class);
                    intent.putExtra("addr", ((News) list.get(position)).address);
                    holder.binding.getRoot().getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        private RvItemDetailNewsItemBinding binding;

        public MyHolder(@NonNull RvItemDetailNewsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

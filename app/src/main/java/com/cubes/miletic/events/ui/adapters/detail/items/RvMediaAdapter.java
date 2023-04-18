package com.cubes.miletic.events.ui.adapters.detail.items;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.miletic.events.R;
import com.cubes.miletic.events.databinding.RvItemDetailMediaItemBinding;
import com.cubes.miletic.events.model.Media;
import com.cubes.miletic.events.ui.ShowImageActivity;
import com.cubes.miletic.events.ui.WebViewActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RvMediaAdapter extends RecyclerView.Adapter<RvMediaAdapter.MediaViewHolder> {

    private ArrayList<Media> list;

    public RvMediaAdapter(ArrayList<Media> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvItemDetailMediaItemBinding binding =  RvItemDetailMediaItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MediaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        Media media = list.get(position);

        if(media.videoUrl != null){
            Picasso.get().load(media.imageUrl).into(holder.binding.imageViewPicture);
            holder.binding.imageViewPlay.setVisibility(View.VISIBLE);
            holder.binding.imageViewPlay.setColorFilter(ContextCompat.getColor(
                    ((RvItemDetailMediaItemBinding)holder.binding).getRoot().getContext(),
                    R.color.white), PorterDuff.Mode.SRC_IN);

            //sending video to another activity
            ((RvItemDetailMediaItemBinding)holder.binding).getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(((RvItemDetailMediaItemBinding)holder.binding).getRoot().getContext(),
                            WebViewActivity.class);
                    intent.putExtra("addr", media.videoUrl);
                    ((RvItemDetailMediaItemBinding)holder.binding).getRoot().getContext().startActivity(intent);
                }
            });
        }
        else{
            Picasso.get().load(media.imageUrl).into(holder.binding.imageViewPicture);
            holder.binding.imageViewPlay.setVisibility(View.INVISIBLE);

            //sending image to another activity
            ((RvItemDetailMediaItemBinding)holder.binding).getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(((RvItemDetailMediaItemBinding)holder.binding).getRoot().getContext(),
                            ShowImageActivity.class);
                    intent.putExtra("image", media.imageUrl);
                    ((RvItemDetailMediaItemBinding)holder.binding).getRoot().getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder{

        RvItemDetailMediaItemBinding binding;

        public MediaViewHolder(@NonNull RvItemDetailMediaItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

package com.cubes.miletic.events.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.miletic.events.R;
import com.cubes.miletic.events.databinding.RvItemEventBigBinding;
import com.cubes.miletic.events.databinding.RvItemEventSmallBinding;
import com.cubes.miletic.events.databinding.RvItemEventSquareBinding;
import com.cubes.miletic.events.model.Event;
import com.cubes.miletic.events.ui.EventDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyHolder> {

    private Context context;
    private ArrayList<Event> list;
    private int layoutId;

    public EventsAdapter(Context context, ArrayList<Event> list, int layoutId) {
        this.list = list;
        this.layoutId = layoutId;
        this.context = context;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MyHolder myHolder = null;

        if(layoutId == R.layout.rv_item_event_big){
            RvItemEventBigBinding binding = RvItemEventBigBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            myHolder = new MyHolder(binding);
        }
        else if(layoutId == R.layout.rv_item_event_small){
            RvItemEventSmallBinding binding = RvItemEventSmallBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);
            myHolder = new MyHolder(binding);
        }
        else{
            RvItemEventSquareBinding binding = RvItemEventSquareBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            myHolder = new MyHolder(binding);
        }

        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Event modelEvent = list.get(position);

        if(layoutId == R.layout.rv_item_event_big){
            ((RvItemEventBigBinding)holder.binding).textViewTitle.setText(modelEvent.name);
            ((RvItemEventBigBinding)holder.binding).textViewDate.setText(modelEvent.date);
            Picasso.get().load(modelEvent.imageUrl).into(((RvItemEventBigBinding)holder.binding).imageView);
        }
        else if(layoutId == R.layout.rv_item_event_small){
            ((RvItemEventSmallBinding)holder.binding).textViewTitle.setText(modelEvent.name);
            ((RvItemEventSmallBinding)holder.binding).textViewDate.setText(modelEvent.date);
            Picasso.get().load(modelEvent.imageUrl).into(((RvItemEventSmallBinding)holder.binding).imageView);
        }
        else{
            ((RvItemEventSquareBinding)holder.binding).textViewTitle.setText(modelEvent.name);
            ((RvItemEventSquareBinding)holder.binding).textViewDate.setText(modelEvent.date);
            Picasso.get().load(modelEvent.imageUrl).into(((RvItemEventSquareBinding)holder.binding).imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetailActivity.class);
                intent.putExtra("id", modelEvent.id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list == null){
            return 0;
        }
        return list.size();
    }

    public void setList(ArrayList<Event> events){
        this.list = events;

        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private ViewBinding binding;

        public MyHolder(ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

package com.cubes.miletic.events.ui.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.miletic.events.R;
import com.cubes.miletic.events.databinding.RvItemHorizontalEventRvBinding;
import com.cubes.miletic.events.model.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyHolder> {

    private ArrayList<Event> sportList;
    private ArrayList<Event> topList;
    private ArrayList<Event> concertlist;
    private ArrayList<Event> latestList;
    private ArrayList<Event> theaterList;
    private Context context;

    public HomeAdapter(Context context, ArrayList<Event> sportList, ArrayList<Event> topList,
                       ArrayList<Event> concertlist, ArrayList<Event> latestList, ArrayList<Event> theaterList) {
        this.sportList = sportList;
        this.topList = topList;
        this.concertlist = concertlist;
        this.latestList = latestList;
        this.theaterList = theaterList;
        this.context = context;
    }

    public HomeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RvItemHorizontalEventRvBinding binding = RvItemHorizontalEventRvBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent, false);

        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if(position == 0){
            holder.binding.textViewCategory.setText(R.string.events_top);
            holder.binding.imageViewIndicator.setColorFilter(ContextCompat.getColor(
                    holder.binding.getRoot().getContext(), R.color.blue), PorterDuff.Mode.SRC_IN);
            holder.binding.recyclerView.setAdapter(new EventsAdapter(context, topList, R.layout.rv_item_event_big));
        }
        else if(position == 1){
            holder.binding.textViewCategory.setText(R.string.event_concert);
            holder.binding.imageViewIndicator.setColorFilter(ContextCompat.getColor(
                    holder.binding.getRoot().getContext(), R.color.blue), PorterDuff.Mode.SRC_IN);
            holder.binding.recyclerView.setAdapter(new EventsAdapter(context,concertlist, R.layout.rv_item_event_big));
        }
        else if(position == 2){
            holder.binding.textViewCategory.setText(R.string.event_theater);
            holder.binding.imageViewIndicator.setColorFilter(ContextCompat.getColor(
                    holder.binding.getRoot().getContext(), R.color.blue), PorterDuff.Mode.SRC_IN);
            holder.binding.recyclerView.setAdapter(new EventsAdapter(context,theaterList, R.layout.rv_item_event_small));
        }
        else if(position == 3){
            holder.binding.textViewCategory.setText(R.string.event_sport);
            holder.binding.imageViewIndicator.setColorFilter(ContextCompat.getColor(
                    holder.binding.getRoot().getContext(), R.color.blue), PorterDuff.Mode.SRC_IN);
            holder.binding.recyclerView.setAdapter(new EventsAdapter(context,sportList, R.layout.rv_item_event_big));
        }
        else if(position == 4){
            holder.binding.textViewCategory.setText(R.string.event_latest);
            holder.binding.imageViewIndicator.setColorFilter(ContextCompat.getColor(
                    holder.binding.getRoot().getContext(), R.color.blue), PorterDuff.Mode.SRC_IN);
            holder.binding.recyclerView.setAdapter(new EventsAdapter(context,latestList, R.layout.rv_item_event_square));
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public void setEventsList(ArrayList<Event> events){
        this.topList = new ArrayList<>();
        this.sportList = new ArrayList<>();
        this.concertlist = new ArrayList<>();
        this.latestList = new ArrayList<>();
        this.theaterList = new ArrayList<>();

        for(Event event : events){
            if(event.category.equalsIgnoreCase("Koncert")){
                concertlist.add(event);
            }
            else if(event.category.equalsIgnoreCase("Sport")){
                sportList.add(event);
            }
            else if(event.category.equalsIgnoreCase("Pozoriste")){
                theaterList.add(event);
            }
        }

        this.topList = (ArrayList<Event>) events.clone();
        this.latestList = (ArrayList<Event>) events.clone();

        Collections.sort(this.topList, new EventTopComparator());
        Collections.sort(this.latestList, new EventDateComparator());

        notifyDataSetChanged();
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        private RvItemHorizontalEventRvBinding binding;

        public MyHolder(RvItemHorizontalEventRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(),
                    LinearLayoutManager.HORIZONTAL, false));
        }
    }

    public class EventTopComparator implements Comparator<Event>{

        @Override
        public int compare(Event event, Event t1) {
            return t1.viewCount - event.viewCount;
        }
    }

    public class EventDateComparator implements Comparator<Event>{

        @Override
        public int compare(Event event, Event t1) {
            return t1.date.compareTo(event.date);
        }
    }
}

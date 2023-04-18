package com.cubes.miletic.events.ui.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.miletic.events.Constants;
import com.cubes.miletic.events.R;
import com.cubes.miletic.events.RoomDB.EventDataSource;
import com.cubes.miletic.events.RoomDB.EventDatabase;
import com.cubes.miletic.events.databinding.RvItemEventDetailHeaderBinding;
import com.cubes.miletic.events.databinding.RvItemHorizontalEventRvBinding;
import com.cubes.miletic.events.model.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.MyHolder> {

    private ArrayList<Event> list;

    public FavouritesAdapter(ArrayList<Event> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvItemEventDetailHeaderBinding binding = RvItemEventDetailHeaderBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent, false);

        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Event event = list.get(position);

        holder.binding.ivBack.setVisibility(View.INVISIBLE);
        holder.binding.ivUp.setVisibility(View.INVISIBLE);
        holder.binding.ivFavourite.setVisibility(View.INVISIBLE);

        holder.binding.tvLocation.setText(event.location);
        holder.binding.tvDate.setText(event.date);
        holder.binding.tvTitle.setText(event.name);
        holder.binding.tvCategory.setText(event.category);
        Picasso.get().load(event.imageUrl).into(holder.binding.iv);

        //setting on click for deleting event from favourites
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.binding.getRoot().getContext());
                builder.setTitle(R.string.text_remove_event)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EventDataSource source = new EventDataSource(
                                        EventDatabase.getInstance(holder.binding.getRoot().getContext()).eventDAO());
                                source.deleteEvent(event, new EventDataSource.OnEventCompleteListener() {
                                    @Override
                                    public void onComplete() {
                                        //updating firebase
                                        FirebaseFirestore.getInstance().collection(Constants.TABLE_EVENTS).document(event.id)
                                                .update("favourite", false);

                                        //removing event from list
                                        list.remove(event);
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public void setList(ArrayList<Event> list){
        this.list = list;
        notifyDataSetChanged();
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        private RvItemEventDetailHeaderBinding binding;

        public MyHolder(RvItemEventDetailHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

package com.cubes.miletic.events.ui.adapters.detail.items;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.cubes.miletic.events.R;
import com.cubes.miletic.events.RoomDB.EventDataSource;
import com.cubes.miletic.events.RoomDB.EventDatabase;
import com.cubes.miletic.events.databinding.RvItemDetailHeaderBinding;
import com.cubes.miletic.events.model.Event;
import com.cubes.miletic.events.ui.adapters.detail.EventDetailAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class RvItemDetailHeader implements RvItemDetail{

    private Event event;

    public RvItemDetailHeader(Event event) {
        this.event = event;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getPriority() {
        return 1;
    }


    @Override
    public void bind(EventDetailAdapter.EventViewHolder holder) {

        RvItemDetailHeaderBinding binding = (RvItemDetailHeaderBinding) holder.binding;

        binding.textViewType.setText(event.category);
        binding.textViewTitle.setText(event.name);
        binding.textViewDate.setText(event.date);
        binding.textViewPlace.setText(event.location);
        Picasso.get().load(event.imageUrl).into(binding.imageView);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)binding.getRoot().getContext()).finish();
            }
        });

        //setting click for saving event in room db
        if(event.favourite){
            binding.imageViewAddToFavorites.setEnabled(false);
            binding.imageViewAddToFavorites.setColorFilter(R.color.gray);
        }
        else {
            binding.imageViewAddToFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventDataSource source = new EventDataSource(
                            EventDatabase.getInstance(binding.getRoot().getContext()).eventDAO());

                    source.saveEvent(event, new EventDataSource.OnEventCompleteListener() {
                        @Override
                        public void onComplete() {
                            FirebaseFirestore.getInstance().collection("events")
                                    .document(event.id).update("favourite", true);

                            binding.imageViewAddToFavorites.setEnabled(false);
                            binding.imageViewAddToFavorites.setColorFilter(R.color.gray);

                            Toast.makeText(binding.getRoot().getContext(),
                                    R.string.text_saved, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }
}


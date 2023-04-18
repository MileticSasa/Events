package com.cubes.miletic.events.ui.adapters.detail.items;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.miletic.events.databinding.RvItemDetailMediaBinding;
import com.cubes.miletic.events.model.Media;
import com.cubes.miletic.events.ui.adapters.detail.EventDetailAdapter;

import java.util.ArrayList;

public class RvItemDetailMedia implements RvItemDetail{

    private ArrayList<Media> list;

    public RvItemDetailMedia(ArrayList<Media> list) {
        this.list = list;
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public void bind(EventDetailAdapter.EventViewHolder holder) {
        RvItemDetailMediaBinding binding = (RvItemDetailMediaBinding) holder.binding;

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(),
                RecyclerView.HORIZONTAL,false));
        binding.recyclerView.setAdapter(new RvMediaAdapter(list));
    }
}

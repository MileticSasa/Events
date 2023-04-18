package com.cubes.miletic.events.ui.adapters.detail.items;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.miletic.events.databinding.RvItemDetailNewsBinding;
import com.cubes.miletic.events.model.News;
import com.cubes.miletic.events.ui.adapters.detail.EventDetailAdapter;

import java.util.ArrayList;

public class RvItemDetailNews implements RvItemDetail {

    private ArrayList<News> list;

    public RvItemDetailNews(ArrayList<News> list) {
        this.list = list;
    }

    @Override
    public int getType() {
        return 5;
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public void bind(EventDetailAdapter.EventViewHolder holder) {
        RvItemDetailNewsBinding binding = (RvItemDetailNewsBinding) holder.binding;

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(),
                RecyclerView.VERTICAL, false));
        binding.recyclerView.setAdapter(new RvNewsAdapter(list));
    }
}

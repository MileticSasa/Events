package com.cubes.miletic.events.ui.adapters.detail.items;


import android.graphics.PorterDuff;

import androidx.core.content.ContextCompat;

import com.cubes.miletic.events.R;
import com.cubes.miletic.events.databinding.RvItemDetailTitleBinding;
import com.cubes.miletic.events.ui.adapters.detail.EventDetailAdapter;

public class RvItemDetailTitle implements RvItemDetail{

    private String title;
    private int priority;

    public RvItemDetailTitle(String title) {
        this.title = title;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    @Override
    public void bind(EventDetailAdapter.EventViewHolder holder) {
        RvItemDetailTitleBinding binding = (RvItemDetailTitleBinding) holder.binding;
        binding.textViewTitle.setText(title);
        binding.indicator.setColorFilter(ContextCompat.getColor(
                holder.binding.getRoot().getContext(), R.color.blue), PorterDuff.Mode.SRC_IN);;
    }
}

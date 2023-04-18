package com.cubes.miletic.events.ui.adapters.detail.items;


import com.cubes.miletic.events.ui.adapters.detail.EventDetailAdapter;

public interface RvItemDetail {

    int getType();
    int getPriority();
    void bind(EventDetailAdapter.EventViewHolder holder);
}

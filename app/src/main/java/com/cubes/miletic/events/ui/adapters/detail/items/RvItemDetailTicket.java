package com.cubes.miletic.events.ui.adapters.detail.items;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.miletic.events.databinding.RvItemDetailTicketBinding;
import com.cubes.miletic.events.model.Ticket;
import com.cubes.miletic.events.ui.adapters.detail.EventDetailAdapter;

import java.util.ArrayList;

public class RvItemDetailTicket implements RvItemDetail{

    //private Ticket ticket;
    private ArrayList<Ticket> tickets;

    public RvItemDetailTicket(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public void bind(EventDetailAdapter.EventViewHolder holder) {
        RvItemDetailTicketBinding binding = (RvItemDetailTicketBinding) holder.binding;

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(),
                RecyclerView.VERTICAL, false));
        binding.recyclerView.setAdapter(new TicketAdapter(tickets));
       //setovati odgovarajuce vrednosti za karte
    }
}

package com.cubes.miletic.events.ui.adapters.detail.items;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.miletic.events.databinding.RvItemDetailTicketItemBinding;
import com.cubes.miletic.events.model.Ticket;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private ArrayList<Ticket> tickets;

    public TicketAdapter(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvItemDetailTicketItemBinding binding = RvItemDetailTicketItemBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false);
        return new TicketViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);

        holder.binding.textViewTitle.setText(ticket.title);
        holder.binding.textViewPrice.setText(ticket.price);
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder {

        private RvItemDetailTicketItemBinding binding;

        public TicketViewHolder(@NonNull RvItemDetailTicketItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

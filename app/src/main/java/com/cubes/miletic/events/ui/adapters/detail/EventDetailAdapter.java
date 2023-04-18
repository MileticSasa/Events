package com.cubes.miletic.events.ui.adapters.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.miletic.events.databinding.RvItemDetailAuthorBinding;
import com.cubes.miletic.events.databinding.RvItemDetailHeaderBinding;
import com.cubes.miletic.events.databinding.RvItemDetailMediaBinding;
import com.cubes.miletic.events.databinding.RvItemDetailNewsBinding;
import com.cubes.miletic.events.databinding.RvItemDetailTicketBinding;
import com.cubes.miletic.events.databinding.RvItemDetailTitleBinding;
import com.cubes.miletic.events.model.Artist;
import com.cubes.miletic.events.model.Event;
import com.cubes.miletic.events.model.Media;
import com.cubes.miletic.events.model.News;
import com.cubes.miletic.events.model.Ticket;
import com.cubes.miletic.events.ui.adapters.detail.items.RvItemDetail;
import com.cubes.miletic.events.ui.adapters.detail.items.RvItemDetailAuthor;
import com.cubes.miletic.events.ui.adapters.detail.items.RvItemDetailHeader;
import com.cubes.miletic.events.ui.adapters.detail.items.RvItemDetailMedia;
import com.cubes.miletic.events.ui.adapters.detail.items.RvItemDetailNews;
import com.cubes.miletic.events.ui.adapters.detail.items.RvItemDetailTicket;
import com.cubes.miletic.events.ui.adapters.detail.items.RvItemDetailTitle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventDetailAdapter extends RecyclerView.Adapter<EventDetailAdapter.EventViewHolder> {

    private Event event;
    private ArrayList<RvItemDetail> items;
    private Context context;

    public EventDetailAdapter(Context context){
        this.items = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewBinding binding;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case 0:
                binding = RvItemDetailHeaderBinding.inflate(inflater,parent,false);
                break;
            case 2:
                binding = RvItemDetailTicketBinding.inflate(inflater,parent,false);
                break;
            case 3:
                binding = RvItemDetailMediaBinding.inflate(inflater,parent,false);
                break;
            case 4:
                binding = RvItemDetailAuthorBinding.inflate(inflater,parent,false);
                break;
            case 5:
                binding = RvItemDetailNewsBinding.inflate(inflater, parent, false);
                break;
            default:
                binding = RvItemDetailTitleBinding.inflate(inflater,parent,false);
        }

        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        this.items.get(position).bind(holder);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position).getType();
    }

    public void addEvent(Event event, ArrayList<Ticket> tickets, ArrayList<Media> media,
                         Artist artist, ArrayList<News> newsList){
        this.event = event;

       if(event != null){
           //kada dodamo event prvo ubacujem header
           this.items.add(new RvItemDetailHeader(this.event));
       }

        //Onda ako ima karata ubaacujem karte
        //ako ih ima prvo ubacim header za ulaznice
        if(tickets != null && tickets.size()>0){

            RvItemDetailTitle rvItemDetailTitle = new RvItemDetailTitle("Ulaznice");
            rvItemDetailTitle.setPriority(2);
            this.items.add(rvItemDetailTitle);

            this.items.add(new RvItemDetailTicket(tickets));
        }

        //ako ima media ubacim media i sadrzaj
        if(media != null && media.size()>0){

            RvItemDetailTitle rvItemDetailTitle = new RvItemDetailTitle("Slike i Video");
            rvItemDetailTitle.setPriority(3);
            this.items.add(rvItemDetailTitle);

            this.items.add(new RvItemDetailMedia(media));

        }

        if(artist != null){

            RvItemDetailTitle rvItemDetailTitle = new RvItemDetailTitle("O autoru");
            rvItemDetailTitle.setPriority(4);
            this.items.add(rvItemDetailTitle);
            this.items.add(new RvItemDetailAuthor(artist));
        }

        if(newsList != null && newsList.size() > 0){

            RvItemDetailTitle rvItemDetailTitle = new RvItemDetailTitle("Vesti");
            rvItemDetailTitle.setPriority(5);
            this.items.add(rvItemDetailTitle);
            this.items.add(new RvItemDetailNews(newsList));
        }

        //sort list
        Collections.sort(items, new Comparator<RvItemDetail>() {
            @Override
            public int compare(RvItemDetail item1, RvItemDetail item2) {
                return Integer.valueOf(item1.getPriority()).compareTo(item2.getPriority());
            }
        });


        notifyDataSetChanged();
    }


    public class EventViewHolder extends RecyclerView.ViewHolder{

        public ViewBinding binding;

        public EventViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}


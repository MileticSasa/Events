package com.cubes.miletic.events.ui.adapters.detail.items;

import android.content.Intent;
import android.view.View;

import com.cubes.miletic.events.databinding.RvItemDetailAuthorBinding;
import com.cubes.miletic.events.model.Artist;
import com.cubes.miletic.events.ui.WebViewActivity;
import com.cubes.miletic.events.ui.adapters.detail.EventDetailAdapter;
import com.squareup.picasso.Picasso;

public class RvItemDetailAuthor implements RvItemDetail {

    private Artist artist;

    public RvItemDetailAuthor(Artist artist){
        this.artist = artist;
    }


    @Override
    public int getType() {
        return 4;
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public void bind(EventDetailAdapter.EventViewHolder holder) {
        RvItemDetailAuthorBinding binding = (RvItemDetailAuthorBinding) holder.binding;

        Picasso.get().load(artist.imageUrl).into(binding.imageView);
        binding.textViewDescription.setText(artist.about);

        //sending address for displaying in webView
        ((RvItemDetailAuthorBinding)holder.binding).textViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(((RvItemDetailAuthorBinding)holder.binding).getRoot().getContext(),
                        WebViewActivity.class);
                intent.putExtra("addr", artist.address);
                ((RvItemDetailAuthorBinding)holder.binding).getRoot().getContext().startActivity(intent);
            }
        });
    }
}

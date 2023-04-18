package com.cubes.miletic.events.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.cubes.miletic.events.Constants;
import com.cubes.miletic.events.databinding.ActivityEventDetailBinding;
import com.cubes.miletic.events.model.Artist;
import com.cubes.miletic.events.model.Event;
import com.cubes.miletic.events.model.Media;
import com.cubes.miletic.events.model.News;
import com.cubes.miletic.events.model.Ticket;
import com.cubes.miletic.events.ui.adapters.detail.EventDetailAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity {

    private ActivityEventDetailBinding binding;

    private EventDetailAdapter adapter;

    Event modelEvent;
    ArrayList<Ticket> tickets;
    ArrayList<Media> media;
    ArrayList<News> newsList;
    Artist artist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //making status bar transparent
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        String eventId = getIntent().getStringExtra("id");

        modelEvent = new Event();
        tickets = new ArrayList<>();
        media = new ArrayList<>();
        newsList = new ArrayList<>();
        artist = new Artist();

        adapter = new EventDetailAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        binding.pb.setVisibility(View.VISIBLE);


        FirebaseFirestore.getInstance().collection(Constants.TABLE_EVENTS).
                whereEqualTo("id", eventId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()){
                            modelEvent = ds.toObject(Event.class);
                        }

                        if(modelEvent != null){
                            adapter.addEvent(modelEvent, null, null, null,null);
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection(Constants.TABLE_EVENTS).document(""+eventId)
                .collection(Constants.SEARCH_CATEGORY_TICKETS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot ds : queryDocumentSnapshots){
                            Ticket ticket = ds.toObject(Ticket.class);

                            if(ticket != null) {
                                tickets.add(ticket);
                            }
                        }

                        if(tickets.size() > 0 ){
                            adapter.addEvent(null, tickets, null, null, null);
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection(Constants.TABLE_EVENTS).document(""+eventId)
                .collection(Constants.SUBCOLLECTION_MEDIA).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()){
                            Media mediaModel = ds.toObject(Media.class);

                            if(mediaModel != null){
                                media.add(mediaModel);
                            }
                        }

                        if(media.size() > 0 ){
                            adapter.addEvent(null, null, media, null, null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EventDetailActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseFirestore.getInstance().collection(Constants.TABLE_EVENTS).document(""+eventId)
                .collection(Constants.SUBCOLLECTION_ARTIST).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot ds : queryDocumentSnapshots){
                            artist = ds.toObject(Artist.class);

                            if(artist != null) {
                                adapter.addEvent(null, null, null, artist, null);
                            }
                        }

                    }
                });

        FirebaseFirestore.getInstance().collection(Constants.TABLE_EVENTS).document(""+eventId)
                .collection(Constants.SUBCOLLECTION_NEWS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot ds : queryDocumentSnapshots){
                            News news = ds.toObject(News.class);

                            if(news != null) {
                                newsList.add(news);
                            }
                        }

                        if(newsList.size() > 0){
                            adapter.addEvent(null, null, null, null, newsList);
                        }
                    }
                });

        binding.pb.setVisibility(View.INVISIBLE);
    }
}
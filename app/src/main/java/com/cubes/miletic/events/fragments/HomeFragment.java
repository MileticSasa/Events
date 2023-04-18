package com.cubes.miletic.events.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.miletic.events.Constants;
import com.cubes.miletic.events.ui.adapters.HomeAdapter;
import com.cubes.miletic.events.databinding.FragmentHomeBinding;
import com.cubes.miletic.events.model.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private HomeAdapter adapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new HomeAdapter(getContext());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.INVISIBLE);


        FirebaseFirestore.getInstance().collection(Constants.TABLE_EVENTS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ArrayList<Event> events = new ArrayList<>();

                        for(DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()){
                            Event event = ds.toObject(Event.class);
                            events.add(event);
                            //Log.i("Events: ",  event.toString());
                        }

                        adapter.setEventsList(events);

                        binding.progressBar.setVisibility(View.INVISIBLE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        binding.progressBar.setVisibility(View.INVISIBLE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                });
    }
}
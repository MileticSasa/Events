package com.cubes.miletic.events.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cubes.miletic.events.Constants;
import com.cubes.miletic.events.ui.adapters.EventsAdapter;
import com.cubes.miletic.events.R;
import com.cubes.miletic.events.databinding.FragmentSearchBinding;
import com.cubes.miletic.events.model.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private EventsAdapter adapter;
    private ArrayList<Event> events;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new EventsAdapter(getContext(), null, R.layout.rv_item_event_small);
        binding.recyclerView.setAdapter(adapter);

        binding.imageViewSearch.setEnabled(false);

        FirebaseFirestore.getInstance().collection(Constants.TABLE_EVENTS).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                events = new ArrayList<>();

                                for(DocumentSnapshot ds : queryDocumentSnapshots){
                                    Event modelEvent = ds.toObject(Event.class);
                                    events.add(modelEvent);
                                }

                                binding.imageViewSearch.setEnabled(true);
                            }
                        });

        binding.imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.editTextSearch.getText().length() > 0){

                    //crating temp list
                    ArrayList<Event> searchList = new ArrayList<>();

                    for(Event event : events){
                        if(event.name.toLowerCase().contains(binding.editTextSearch.getText().toString().toLowerCase())){
                            searchList.add(event);
                        }
                    }

                    adapter.setList(searchList);
                    binding.textViewResultCount.setText(""+searchList.size()+" "+getString(R.string.text_result));
                }
                else{
                    Toast.makeText(getContext(), R.string.message_search_validation, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
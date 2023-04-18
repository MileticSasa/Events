package com.cubes.miletic.events.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.miletic.events.Constants;
import com.cubes.miletic.events.Marker.CustomMarker;
import com.cubes.miletic.events.Marker.MyRenderer;
import com.cubes.miletic.events.R;
import com.cubes.miletic.events.databinding.FragmentMapBinding;
import com.cubes.miletic.events.model.Event;
import com.cubes.miletic.events.ui.EventDetailActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapBinding binding;

    private ArrayList<Event> eventArrayList = new ArrayList<>();

    private SupportMapFragment mapFragment;
    private GoogleMap map;

    private ClusterManager clusterManager;
    private MyRenderer renderer;
    private ArrayList<CustomMarker> markers = new ArrayList<>();

    private Event currentEvent = null;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        binding.linearL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EventDetailActivity.class);
                intent.putExtra("id", currentEvent.id);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;

        FirebaseFirestore.getInstance().collection(Constants.TABLE_EVENTS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot doc : queryDocumentSnapshots){
                            Event event = doc.toObject(Event.class);
                            eventArrayList.add(event);
                        }

                        if(eventArrayList.size() > 0){
                            addMarkers(map, eventArrayList);
                        }
                    }
                });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                currentEvent = null;

                TransitionManager.beginDelayedTransition(binding.parentLinear);
                binding.linearL.setVisibility(View.GONE);
            }
        });
    }

    private void addMarkers(GoogleMap map, ArrayList<Event> events){
        if(map != null){
            if(clusterManager == null){
                clusterManager = new ClusterManager<CustomMarker>(getActivity().getApplicationContext(), map);
            }

            if(renderer == null){
                renderer = new MyRenderer(getActivity().getApplicationContext(), map, clusterManager);
                clusterManager.setRenderer(renderer);
            }

            for(Event event : events){
                switch (event.category){
                    case Constants.SEARCH_CATEGORY_CONCERT:
                        CustomMarker marker = new CustomMarker(event, R.drawable.concert);
                        clusterManager.addItem(marker);
                        markers.add(marker);
                        break;
                    case Constants.SEARCH_CATEGORY_SPORT:
                        CustomMarker marker1 = new CustomMarker(event, R.drawable.sport);
                        clusterManager.addItem(marker1);
                        markers.add(marker1);
                        break;
                    case Constants.SEARCH_CATEGORY_THEATER:
                        CustomMarker marker2 = new CustomMarker(event, R.drawable.pozoriste);
                        clusterManager.addItem(marker2);
                        markers.add(marker2);
                        break;
                }
            }

            clusterManager.cluster();

            clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener() {
                @Override
                public boolean onClusterItemClick(ClusterItem item) {
                    currentEvent = ((CustomMarker) item).getEvent();
                    displayEventDetails(currentEvent);

                    return false;
                }
            });
        }
    }

    private void displayEventDetails(Event currentEvent) {

        if(currentEvent != null){
            binding.linearL.setVisibility(View.VISIBLE);
            binding.tvCategory.setText(currentEvent.category);
            binding.tvLocation.setText(currentEvent.location);
            binding.tvTitle.setText(currentEvent.name);

            ObjectAnimator animator = ObjectAnimator.ofFloat(binding.linearL, "alpha", 0, 1);
            animator.setDuration(1000);
            animator.start();
        }
    }
}
package com.cubes.miletic.events.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cubes.miletic.events.Constants;
import com.cubes.miletic.events.R;
import com.cubes.miletic.events.RoomDB.EventDataSource;
import com.cubes.miletic.events.RoomDB.EventDatabase;
import com.cubes.miletic.events.databinding.FragmentFavouritesBinding;
import com.cubes.miletic.events.model.Event;
import com.cubes.miletic.events.ui.adapters.FavouritesAdapter;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;
    private FavouritesAdapter adapter;

    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new FavouritesAdapter(null);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        EventDataSource source = new EventDataSource(EventDatabase.getInstance(getContext()).eventDAO());
        source.getAllEvents(new EventDataSource.OnGetEventsListener() {
            @Override
            public void onSuccess(ArrayList<Event> events) {
                adapter.setList(events);
            }
        });

        binding.imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMenu();
            }
        });
    }

    private void makeMenu() {
        PopupMenu menu = new PopupMenu(getContext(), binding.imgFilter);
        menu.getMenuInflater().inflate(R.menu.search_events_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.concert:
                        filterResults(Constants.SEARCH_CATEGORY_CONCERT);
                        return true;
                    case R.id.sport:
                        filterResults(Constants.SEARCH_CATEGORY_SPORT);
                        return true;
                    case R.id.theater:
                        filterResults(Constants.SEARCH_CATEGORY_THEATER);
                        return true;
                    default:
                        return true;
                }
            }
        });

        menu.show();
    }

    private void filterResults(String s) {
        EventDataSource source = new EventDataSource(EventDatabase.getInstance(getContext()).eventDAO());
        source.findEventsByCategory(s, new EventDataSource.OnGetEventsListener() {
            @Override
            public void onSuccess(ArrayList<Event> events) {
                adapter.setList(events);
            }
        });
    }
}
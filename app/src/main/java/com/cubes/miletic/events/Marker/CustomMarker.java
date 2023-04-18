package com.cubes.miletic.events.Marker;


import androidx.annotation.NonNull;

import com.cubes.miletic.events.model.Event;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class CustomMarker implements ClusterItem {

    private Event event;
    private int image;

    public CustomMarker(Event event, int image) {
        this.event = event;
        this.image = image;
    }

    public CustomMarker() {
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return new LatLng(event.latLng.getLatitude(), event.latLng.getLongitude());
    }

    @Override
    public String getTitle() {
        return event.name;
    }

    @Override
    public String getSnippet() {
        return "";
    }

}

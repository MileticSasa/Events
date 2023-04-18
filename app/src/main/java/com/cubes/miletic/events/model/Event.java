package com.cubes.miletic.events.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

@Entity
public class Event extends Detail{

    @Exclude
    @PrimaryKey(autoGenerate = true)
    public int eventId;

    public String id; //this is id in firebase
    public String name;
    public String date;
    public String time;
    public String imageUrl;
    public String category;
    public String location;
    public int viewCount;
    public boolean favourite;
    @Ignore
    public GeoPoint latLng;

    public Event() {
    }

    @Override
    public String toString() {
        return "Event{" +
                name + '\'' +
                '}';
    }
}

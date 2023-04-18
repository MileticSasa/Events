package com.cubes.miletic.events.RoomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cubes.miletic.events.model.Event;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface EventDAO {

    @Insert
    void addEvent(Event event);

    @Delete
    void removeEvent(Event event);

    @Query("Select * from Event")
    List<Event> getAll();

    @Query("Select * from Event where category like :category")
    List<Event> findByCategory(String category);
}

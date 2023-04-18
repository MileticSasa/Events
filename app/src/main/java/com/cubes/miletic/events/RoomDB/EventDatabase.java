package com.cubes.miletic.events.RoomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cubes.miletic.events.model.Event;

@Database(entities = {Event.class}, version = 1)
public abstract class EventDatabase extends RoomDatabase {

    private static EventDatabase instance;

    public static EventDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    EventDatabase.class, "event_db").build();
        }

        return instance;
    }

    public abstract EventDAO eventDAO();
}

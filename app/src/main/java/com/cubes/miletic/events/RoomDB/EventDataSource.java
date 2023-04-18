package com.cubes.miletic.events.RoomDB;

import android.os.Handler;
import android.os.Looper;

import com.cubes.miletic.events.model.Event;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventDataSource {

    private EventDAO dao;
    private Executor mainThreadExecutor;
    private ExecutorService service;

    public EventDataSource(EventDAO dao) {
        this.dao = dao;
        mainThreadExecutor = new MainThreadExecutor();
        service = Executors.newSingleThreadExecutor();
    }

    public void getAllEvents(OnGetEventsListener listener){
        service.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Event> events = (ArrayList<Event>) dao.getAll();

                mainThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess(events);
                    }
                });
            }
        });
    }

    public void saveEvent(Event event, OnEventCompleteListener listener){
        service.execute(new Runnable() {
            @Override
            public void run() {
                dao.addEvent(event);

                if(listener != null){
                    mainThreadExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            listener.onComplete();
                        }
                    });
                }
            }
        });
    }

    public void deleteEvent(Event event, OnEventCompleteListener listener){
        service.execute(new Runnable() {
            @Override
            public void run() {
                dao.removeEvent(event);

                if(listener != null){
                    mainThreadExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            listener.onComplete();
                        }
                    });
                }
            }
        });
    }

    public void findEventsByCategory(String category, OnGetEventsListener listener){
        service.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Event> events = (ArrayList<Event>) dao.findByCategory(category);

                if(listener != null){
                    mainThreadExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(events);
                        }
                    });
                }
            }
        });
    }

    public interface OnEventCompleteListener {
        void onComplete();
    }

    public interface OnGetEventsListener {
        void onSuccess(ArrayList<Event> events);
    }

    private class MainThreadExecutor implements Executor {

        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            handler.post(runnable);
        }
    }
}

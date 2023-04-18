package com.cubes.miletic.events;

import android.app.Application;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;


public class EventsApplication extends Application {

    private static FirebaseAnalytics analytics;

    @Override
    public void onCreate() {
        super.onCreate();

        analytics = FirebaseAnalytics.getInstance(this);
    }

    public synchronized static void sendAnalythicsEvent(String name, String type){
        if(analytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type);
            analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }

    public synchronized static void sendAnalythicsEvent(String name){
        if(analytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "event");
            analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }
}

package com.cubes.miletic.events.model;

public class News extends Detail{
    public String imageUrl;
    public String date;
    public String title;
    public String text;
    public String address;

    public News() {
    }

    public News(String imageUrl, String date, String title, String text) {
        this.imageUrl = imageUrl;
        this.date = date;
        this.title = title;
        this.text = text;
    }

}

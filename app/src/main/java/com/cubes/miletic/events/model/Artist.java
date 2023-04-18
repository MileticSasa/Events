package com.cubes.miletic.events.model;

public class Artist extends Detail{
    public String imageUrl;
    public String about;
    public String address;

    public Artist() {
    }

    public Artist(String imageUrl, String about) {
        this.imageUrl = imageUrl;
        this.about = about;
    }

}

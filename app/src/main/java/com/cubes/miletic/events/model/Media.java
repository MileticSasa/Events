package com.cubes.miletic.events.model;

public class Media extends Detail {
    public String imageUrl;
    public String videoUrl;

    public Media() {
    }

    public Media(String imageUrl, String videoUrl) {
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

}

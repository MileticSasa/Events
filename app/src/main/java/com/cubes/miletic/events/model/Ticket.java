package com.cubes.miletic.events.model;

public class Ticket extends Detail {

    public String title;
    public String price;

    public Ticket() {
    }

    public Ticket(String title, String price) {
        this.title = title;
        this.price = price;
    }

}

package com.cubes.miletic.events.model;


import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public String email;
    public String name;
    public String surname;
    public String location;
    public String image;

    public User() {
    }

    public User(String email, String name, String surname) {
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    protected User(Parcel in) {
        email = in.readString();
        name = in.readString();
        surname = in.readString();
        location = in.readString();
        image = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(name);
        parcel.writeString(surname);
        parcel.writeString(location);
        parcel.writeString(image);
    }
}

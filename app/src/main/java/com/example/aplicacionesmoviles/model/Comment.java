package com.example.aplicacionesmoviles.model;

import com.google.gson.annotations.SerializedName;

public class Comment {
    public int id;
    public String text;

    @SerializedName("user")
    public int user_id;

    @SerializedName("place")
    public int place_id;

    public String createdDate;
    public String userName;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", user_id=" + user_id +
                ", place_id=" + place_id +
                ", createdDate='" + createdDate + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
